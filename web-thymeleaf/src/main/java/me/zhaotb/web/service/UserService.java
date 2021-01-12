package me.zhaotb.web.service;


import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import me.zhaotb.web.config.FilePathConfig;
import me.zhaotb.web.config.JwtConfig;
import me.zhaotb.web.config.Tables;
import me.zhaotb.web.dto.account.RegisterAccount;
import me.zhaotb.web.dto.account.UserAccount;
import me.zhaotb.web.dto.account.UserDto;
import me.zhaotb.web.dto.account.UserInfo;
import me.zhaotb.web.dto.account.UserProfilePhoto;
import me.zhaotb.web.util.CRFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @author zhaotangbo
 * @since 2020/12/29
 */
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
@Slf4j
public class UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EmailService emailService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private FilePathConfig filePathConfig;

    private static final String AUTH_CODE_PREFIX = "email::auth::";

    private static final int DEFAULT_EXPIRE_MINUTES = 3;

    private int expireMinutes = DEFAULT_EXPIRE_MINUTES;

    @Autowired
    private JwtConfig jwtConfig;

    public void setExpireMinutes(int expireMinutes) {
        this.expireMinutes = expireMinutes;
    }

    /**
     * 发送授权码
     *
     * @param account 账号信息
     */
    public void sendAuthCode(UserAccount account) {
        if (StringUtils.isNotBlank(account.getEmail())) {
            try {
                String authCode = emailService.sendAuthCode(account.getEmail(), expireMinutes);
                redisTemplate.opsForValue().set(AUTH_CODE_PREFIX + account.getEmail(), authCode, expireMinutes, TimeUnit.MINUTES);
            } catch (Exception e) {
                log.error("发送验证码到邮箱失败：" + account.getEmail(), e);
            }
        } else {
            //TODO 手机短信验证码
        }
    }

    @Transactional(rollbackFor = Throwable.class, noRollbackFor = {RegisterException.NeedNotRollbackException.class, IllegalArgumentException.class})
    public void register(RegisterAccount account) {
        Long uaId;
        if (StringUtils.isNotBlank(account.getEmail())) {
            validateAccount(account.getEmail());
            uaId = validateAuthCode(account.getEmail(), account.getAuthCode());
            jdbcTemplate.update("insert into user_account(id, `password`, email) values (?, ?, ?)",
                    uaId, account.getPassword(), account.getEmail());
        } else if (StringUtils.isNotBlank(account.getPhoneNumber())) {
            validateAccount(account.getPhoneNumber());
            uaId = validateAuthCode(account.getPhoneNumber(), account.getAuthCode());
            jdbcTemplate.update("insert into user_account(`password`, phone_number) values (?, ?)",
                    account.getPassword(), account.getPhoneNumber());
        } else {
            throw new IllegalArgumentException("邮箱和电话号码均为空");
        }
        try {
            jdbcTemplate.update("insert into user_info(ua_id, nick_name) values (?, ?)", uaId, account.getNickName());
        } catch (DuplicateKeyException e) {
            log.error(CRFactory.DUP_NICK_NAME + account.getNickName(), e);
            throw new RegisterException.DuplicateNickNameException(account.getNickName());
        } catch (Exception e) {
            log.error("新建用户信息", e);
            //SQL 执行异常，抛出异常执行回滚操作
            throw e;
        }
    }

    /**
     * 校验验证码
     *
     * @param accountKey 邮箱或手机号
     * @param authCode   验证码
     * @return 校验成功返回user_account主键，否则抛出异常 {@link me.zhaotb.web.service.RegisterException.InvalidAuthCodeException}
     */
    private Long validateAuthCode(String accountKey, String authCode) {
        String tmp = redisTemplate.opsForValue().get(AUTH_CODE_PREFIX + accountKey);
        if (StringUtils.isBlank(tmp) || !tmp.equals(authCode)) {
            throw new RegisterException.InvalidAuthCodeException(authCode);
        }
        redisTemplate.delete(AUTH_CODE_PREFIX + accountKey);
        return sequenceService.next(Tables.User.USER_ACCOUNT);
    }

    /**
     * 验证邮箱或电话号码是否已经被注册, 已经注册则抛出异常 {@link RegisterException.AlreadyRegisterException}
     *
     * @param emailOrPhoneNumber 邮箱或电话号码
     */
    public void validateAccount(String emailOrPhoneNumber) {
        List<Integer> exists = jdbcTemplate.query("select 1 from user_account where email=? or phone_number=?",
                new Object[]{emailOrPhoneNumber, emailOrPhoneNumber}, new SingleColumnRowMapper<>());
        if (exists.size() > 0) {
            throw new RegisterException.AlreadyRegisterException("账号已被注册：" + emailOrPhoneNumber);
        }
    }


    /**
     * 根据邮箱密码或者电话号码密码查询用户基本信息
     *
     * @return 返回JWT
     */
    public String login(UserAccount account) {
        UserInfo userInfo;
        UserAccount userAccount;
        String msg = "";
        try {
            if (StringUtils.isNotBlank(account.getEmail())) {
                msg = account.getEmail();
                userInfo = jdbcTemplate.queryForObject("select id, ua_id, nick_name, birthday, sex, signature from user_info where ua_id=(select id from user_account where email=? and password=?)"
                        , new Object[]{account.getEmail(), account.getPassword()}, new BeanPropertyRowMapper<>(UserInfo.class));
            } else {
                msg = account.getPhoneNumber();
                userInfo = jdbcTemplate.queryForObject("select id, ua_id, nick_name, birthday, sex, signature from user_info where ua_id=(select id from user_account where phone_number=? and password=?)"
                        , new Object[]{account.getPhoneNumber(), account.getPassword()}, new BeanPropertyRowMapper<>(UserInfo.class));
            }
        } catch (Exception e) {
            log.error("查询账号信息异常：" + msg, e);
            return null;
        }
        Calendar instance = Calendar.getInstance();
        //过期时间: 月底23点59分59秒. 先设置下个月1号 00:00:00 ,然后减1秒
        instance.add(Calendar.MONTH, 1);
        instance.set(Calendar.DAY_OF_MONTH, 1);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        instance.add(Calendar.SECOND, -1);

        UserDto dto = new UserDto();
        dto.setIssuedAt(System.currentTimeMillis());
        dto.setExpiration(instance.getTimeInMillis());
        dto.setUserInfo(userInfo);
        return Jwts.builder()
                .setPayload(JSONObject.toJSONString(dto))
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecret())
                .compact();
    }

    /**
     * 更新用户头像
     *
     * @param dto  用户信息
     * @param path 头像路径
     */
    public void updateUserPhoto(UserDto dto, String path) {
        List<Integer> list = jdbcTemplate.queryForList("select 1 from user_profile_photo where ua_id=? ", new Object[]{dto.getUserInfo().getUaId()}, Integer.class);
        int res;
        if (list.size() >= 1) {
            res = jdbcTemplate.update("update user_profile_photo set path=?,update_time=? where ua_id=? ",
                    path, new Date(), dto.getUserInfo().getUaId());
        } else {
             res = jdbcTemplate.update("insert into user_profile_photo(ua_id,path,update_time) values (?,?,?)",
                    dto.getUserInfo().getUaId(), path, new Date());
        }
        if (res != 1) {
            log.error("预期更新头像生效条数：1， 实际生效条数：{}", res);
        }
    }

    /**
     * 输出用户头像
     *
     * @param userDto 用户信息
     * @param out     输出流
     */
    public void accessUserPhoto(UserDto userDto, OutputStream out) {
        UserInfo userInfo = userDto.getUserInfo();
        Long uaId = userInfo.getUaId();
        List<UserProfilePhoto> list = jdbcTemplate.query("select id, ua_id, path, qualifier, update_time "
                        + "from user_profile_photo where ua_id=?",
                new Object[]{uaId}, new BeanPropertyRowMapper<>(UserProfilePhoto.class));
        FileInputStream input = null;
        try {
            if (list.size() < 1) {
                input = new FileInputStream(new File(filePathConfig.getProfilePhoto(), filePathConfig.getDefaultProfilePhoto()));
                IOUtils.copy(input, out);
            } else {
                try {
                    input = new FileInputStream(new File(filePathConfig.getProfilePhoto(), uaId + "/" + list.get(0).getPath()));
                    IOUtils.copy(input, out);
                } catch (Exception e) {
                    input = new FileInputStream(new File(filePathConfig.getProfilePhoto(), filePathConfig.getDefaultProfilePhoto()));
                    IOUtils.copy(input, out);
                    log.error("读取用户配置头像异常：" + list.get(0).getPath() + ", 使用默认头像", e);
                }
            }
        } catch (IOException e) {
            log.error("读取头像文件异常", e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    log.error("", e);
                }
            }
        }

    }

    public UserInfo getUserInfo(UserDto dto) {
        try {
            return jdbcTemplate.queryForObject("select id, ua_id, nick_name, birthday, sex, signature from user_info where id=? "
                    , new Object[]{dto.getUserInfo().getId()}, new BeanPropertyRowMapper<>(UserInfo.class));
        } catch (Exception e) {
            log.error("查询账号信息异常：" + dto.getUserInfo().getId(), e);
            return null;
        }
    }

    /**
     * 更新用户信息
     *
     * @param userInfo 用户信息
     * @return 小于0，更新失败；等于0未更新；等于1更新成功；等于2，修改了名字
     */
    public int updateUserInfo(UserDto dto, UserInfo userInfo) {

        int res = 1;
        List<Object> args = new LinkedList<>();
        StringBuilder sql = new StringBuilder("update user_info set ");
        if (StringUtils.isNotBlank(userInfo.getNickName())) {
            sql.append(" nick_name=?,");
            args.add(userInfo.getNickName());
            res += 1;
        }
        if (StringUtils.isNotBlank(userInfo.getSignature())) {
            sql.append(" signature=?,");
            args.add(userInfo.getSignature());
        }
        if (userInfo.getBirthday() != null) {
            sql.append(" birthday=?,");
            args.add(userInfo.getBirthday());
        }
        if (userInfo.getSex() != null) {
            sql.append(" sex=?,");
            args.add(userInfo.getSex());
        }
        if (args.size() < 1) {
            //没有要更新的字段
            return 0;
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" where id=?");
        args.add(dto.getUserInfo().getId());

        try {
            int update = jdbcTemplate.update(sql.toString(), args.toArray());
            if (update < 1) {
                return -1;
            }
        } catch (Exception e) {
            log.error("更新用户信息异常", e);
            return -1;
        }
        return res;
    }

}

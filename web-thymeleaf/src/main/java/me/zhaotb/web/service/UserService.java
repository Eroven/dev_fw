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

    public void sendAuthCode(UserAccount account) {
        if (StringUtils.isNotBlank(account.getEmail())) {
            try {
                String authCode = emailService.sendAuthCode(account.getEmail(), expireMinutes);
                redisTemplate.opsForValue().set(AUTH_CODE_PREFIX + account.getEmail(), authCode, expireMinutes, TimeUnit.MINUTES);
            } catch (Exception e) {
                log.error("发送验证码到邮箱失败：" + account.getEmail(), e);
            }
        } else {

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
        //过期时间
        instance.add(Calendar.SECOND, jwtConfig.getExpire());

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
                input = new FileInputStream(new File(filePathConfig.getProfilePhoto(), list.get(0).getPath()));
                IOUtils.copy(input, out);
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

}

package me.zhaotb.web.service;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import me.zhaotb.web.dto.JWTConfig;
import me.zhaotb.web.dto.account.RegisterAccount;
import me.zhaotb.web.dto.account.UserAccount;
import me.zhaotb.web.dto.account.UserInfo;
import me.zhaotb.web.util.CRFactory;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
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

    private static final String AUTH_CODE_PREFIX = "email::auth::";

    @Autowired
    private JWTConfig jwtConfig;

    public void sendAuthCode(UserAccount account) {
        if (StringUtils.isNotBlank(account.getEmail())) {
            try {
                String authCode = emailService.sendAuthCode(account.getEmail());
                redisTemplate.opsForValue().set(AUTH_CODE_PREFIX + account.getEmail(), authCode, 3, TimeUnit.MINUTES);
            } catch (Exception e){
                log.error("发送验证码到邮箱失败：" + account.getEmail(), e);
            }
        } else {

        }
    }

    @Transactional
    public void register(RegisterAccount account) {
        if (StringUtils.isNotBlank(account.getEmail())) {
            String tmp = redisTemplate.opsForValue().get(AUTH_CODE_PREFIX + account.getEmail());
            if (StringUtils.isBlank(tmp) || !tmp.equals(account.getAuthCode())) {
                throw new RegisterException.InvalidAuthCodeException(account.getNickName());
            }
            jdbcTemplate.update("insert into user_account(`password`, email) values (?, ?)", account.getPassword(), account.getEmail());
        } else {
            jdbcTemplate.update("insert into user_account(`password`, phone_number) values (?, ?)", account.getPassword(), account.getPhoneNumber());
        }
        try {
            jdbcTemplate.update("insert into user_info(nick_name) values (?)", account.getNickName());
        } catch (Exception e) {
            log.error(CRFactory.DUP_NICK_NAME + account.getNickName(), e);
            throw new RegisterException.DuplicateNickNameException(account.getNickName());
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
        instance.add(Calendar.SECOND, jwtConfig.getExpire());//过期时间

        return Jwts.builder()
                .setSubject(userInfo.getNickName())
                .setIssuedAt(new Date())
                .setExpiration(instance.getTime())
                .addClaims(Collections.singletonMap("meunList", Arrays.asList("/HelloUser", "/OrderManage")))
                .addClaims(Collections.singletonMap("signature", userInfo.getSignature()))
                .addClaims(Collections.singletonMap("birthday", userInfo.getBirthday()))
                .addClaims(Collections.singletonMap("sex", userInfo.getSex()))
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecret())
                .compact();
    }


}

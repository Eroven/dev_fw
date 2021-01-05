package me.zhaotb.web.service;


import me.zhaotb.web.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author zhaotangbo
 * @since 2020/12/29
 */
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailProperties mailProperties;

    /**
     * 发送验证码到指定邮箱
     * @param emailAddress 邮箱地址
     * @param expireMinutes 失效时间
     * @return 返回验证码
     */
    public String sendAuthCode(String emailAddress, int expireMinutes) {
        String code = RandomUtil.randomNumber(4);
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(mailProperties.getUsername());
        msg.setTo(emailAddress);
        msg.setSubject("【系统邮件】验证码");
        msg.setText(buildContent(code, expireMinutes));
        javaMailSender.send(msg);
        return code;
    }

    private String buildContent(String code, int expireMinutes) {
        return "<p><span>验证码为：</span><h2>"
                + code + " </h2><span> ，有效时间："
                + expireMinutes + " 分钟</span></p>";
    }

}

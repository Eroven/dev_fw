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
     * @return 返回验证码
     */
    public String sendAuthCode(String emailAdress) {
        String code = RandomUtil.randomNumber(4);
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(mailProperties.getUsername());
        msg.setTo(emailAdress);
        msg.setSubject("【系统邮件】验证码");
        msg.setText(code);
        javaMailSender.send(msg);
        return code;
    }

}

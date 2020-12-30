package me.zhaotb.web;


import me.zhaotb.web.dto.account.UserAccount;
import me.zhaotb.web.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhaotangbo
 * @since 2020/12/29
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootSimpleTest {

    @Autowired
    private JavaMailSender javaMailSender;


    @Test
    public void testSendMail(){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("1170851768@qq.com");
        msg.setTo("1054709236@qq.com");
        msg.setSubject("【系统邮件】验证码");
        msg.setText("3345");
        javaMailSender.send(msg);
    }

    @Autowired
    private UserService userService;

    @Test
    public void testLogin(){
        UserAccount account = new UserAccount();
        account.setEmail("123");
        account.setPassword("123");
        userService.login(account);
    }
}

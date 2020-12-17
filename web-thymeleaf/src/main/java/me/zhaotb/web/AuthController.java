package me.zhaotb.web;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * @author zhaotangbo
 * @since 2020/12/17
 */
@Controller
@RequestMapping("unAuth")
public class AuthController {

    @Value("${jwt.secret}")
    private String secret;

    @RequestMapping("view")
    public String view(){
        return "login";
    }

    @RequestMapping("login")
    public String login(String user, String password){
        if ("123456".equals(password)) {
            String compact = Jwts.builder()
                    .setSubject(user)
                    .setIssuedAt(new Date())
                    .signWith(SignatureAlgorithm.HS256, secret)
                    .compact();
            return "/file/view";
        }
        return "401";
    }

}


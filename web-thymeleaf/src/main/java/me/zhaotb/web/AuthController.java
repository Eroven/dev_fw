package me.zhaotb.web;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import me.zhaotb.web.dto.CommonResponse;
import me.zhaotb.web.dto.User;
import me.zhaotb.web.util.CRFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
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

    @RequestMapping(value = "login", method = RequestMethod.GET)
    @ResponseBody
    public CommonResponse login(String user, String password){
        if ("123456".equals(password)) {
            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.MINUTE, 5);//5分钟后过期

            String compact = Jwts.builder()
                    .setSubject(user)
                    .setIssuedAt(new Date())
                    .setExpiration(instance.getTime())
                    .addClaims(Collections.singletonMap("meunList", Arrays.asList("/HelloUser", "/OrderManage")))
                    .signWith(SignatureAlgorithm.HS256, secret)
                    .compact();
            return CRFactory.okData(compact);
        }
        return CRFactory.error();
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse login(@RequestBody User user) {
        return login(user.getUser(), user.getPassword());
    }

}


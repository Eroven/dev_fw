package me.zhaotb.oauth.server.controller;

import me.zhaotb.oauth.server.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhaotangbo
 * @date 2018/11/23
 */
@RestController
@RequestMapping("rest")
public class RestUserController {

    @RequestMapping("user")
    public User user(HttpServletRequest request){
        Object user = request.getAttribute("user");
        return (User) user;
    }

    @RequestMapping("user/{id}")
    public User userById(@PathVariable("id")String id){
        User user = new User();
        user.setComment(id);
        return user;
    }

    @RequestMapping("error/401")
    public ResponseEntity<Object> erro401(HttpServletResponse response, String uri){
        response.setHeader("WWW-Authenticate", "OAuth 2.0");
        response.setCharacterEncoding("utf-8");
        return new ResponseEntity<>(new BaseResponse("未授权", uri), HttpStatus.UNAUTHORIZED);
    }
}

package me.zhaotb.oauth.server.service;

import me.zhaotb.oauth.server.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class LogInService {

    private HashMap<User, User> users = new HashMap<>();
    public LogInService() {
        User user = new User();
        user.setNameName("root");
        user.setPassword("123456");
        user.setComment("测试用户");
        users.put(user, user);
    }

    public User login(User user){
        return users.get(user);
    }

}

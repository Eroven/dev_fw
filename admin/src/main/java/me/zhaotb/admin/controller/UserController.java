package me.zhaotb.admin.controller;

import lombok.extern.slf4j.Slf4j;
import me.zhaotb.admin.api.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

/**
 * @author zhaotangbo
 * @date 2019/2/12
 */
@Slf4j
@Controller
@RequestMapping("user")
public class UserController {

    @RequestMapping("{id}")
    public String qryUser(@PathVariable("id") long id, Model model){
        model.addAttribute("user", new User(id, "张三", 29));
        return "/user/detail";
    }

    @RequestMapping("list")
    public String qryList(Model model){
        log.info("msg {}", "info");
        ArrayList<User> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new User((long) i, "张三" + i, i + 20));
        }
        model.addAttribute("users", list);
        return "user/list";
    }
}

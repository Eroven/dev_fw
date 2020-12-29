package me.zhaotb.web.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zhaotangbo
 * @since 2020/12/17
 */
@RequestMapping("error")
@Controller
public class ErrorController {

    @RequestMapping("common")
    public String commonError(){
        return "commonError";
    }

    @RequestMapping("401")
    public String show401(){
        return "401";
    }


}

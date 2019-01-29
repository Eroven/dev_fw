package me.zhaotb.oauth.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zhaotangbo
 * @date 2018/11/24
 */
@Controller
@RequestMapping("error")
public class ErrorController {

    @RequestMapping("402")
    public String error402(){
        return "error/402";
    }

}

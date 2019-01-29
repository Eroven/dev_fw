package me.zhaotb.mongodb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("files")
public class FileController {

    @RequestMapping("/{type}/{name}")
    public void get(@PathVariable("type") String type, @PathVariable("name") String name,
                    HttpServletResponse response) {

    }

}

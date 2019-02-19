package me.zhaotb.admin.controller;

import me.zhaotb.admin.spring.Env;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author zhaotangbo
 * @date 2019/2/11
 */
@RestController
@RequestMapping("main")
public class MainController {

    private Environment environment;

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @RequestMapping("rest")
    public String rest() {
        return "hello";
    }

    @RequestMapping("pic/{name}")
    public void pic(@PathVariable("name") String name, HttpServletResponse rep) throws IOException {
        String picPath = Env.getPicPath();
        System.out.println("picPath : " + picPath);
        System.out.println("picName : " + name);
        File file = new File(picPath, name);
        if (file.exists()) {
            FileInputStream inputStream = new FileInputStream(file);
            ServletOutputStream outputStream = rep.getOutputStream();
            int copy = IOUtils.copy(inputStream, outputStream);
            rep.setContentLength(copy);
            rep.setContentType("image/png");
            inputStream.close();
        }
    }

    @RequestMapping("env")
    public Map env() {
        String picPath = environment.getProperty("admin.env.pic-path");
        String charset = environment.getProperty("admin.env.charset");
        Hashtable<String, String> hashtable = new Hashtable<>();
        hashtable.put("picPath", picPath);
        hashtable.put("charset", charset);
        return hashtable;
    }

}

package me.zhaotb.ms.cc;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhaotangbo
 * @since 2020/11/27
 */
@SpringBootApplication
@RestController
@RequestMapping("config")
@RefreshScope
@EnableEurekaClient
public class ReadConfigController {

    @Value("${config.name:init}")
    private String name;

    @Autowired
    private Config config;

    @RequestMapping("readName")
    public String readName(){
        return name;
    }

    @RequestMapping("readNameRefresh")
    public String readNameRefresh(){
        return config.getName();
    }

    public static void main(String[] args) {
        SpringApplication.run(ReadConfigController.class, args);
    }
}

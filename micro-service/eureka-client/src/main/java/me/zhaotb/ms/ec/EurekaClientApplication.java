package me.zhaotb.ms.ec;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhaotangbo
 * @since 2020/11/27
 */
@SpringBootApplication
@EnableEurekaClient
@RestController
@RequestMapping("task")
public class EurekaClientApplication {

    @Value("${server.port}")
    private int port;

    @RequestMapping("test")
    public String test(){
        return "hello , " + port;
    }

    public static void main(String[] args) {
        SpringApplication.run(EurekaClientApplication.class, args);
    }

}

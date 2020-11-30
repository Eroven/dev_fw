package me.zhaotb.register.eureka;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author zhaotangbo
 * @since 2020/11/27
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaRegisterApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaRegisterApplication.class, args);
    }
}

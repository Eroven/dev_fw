package me.zhaotb.jmt;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author zhaotangbo
 * @since 2020/11/19
 */
@SpringBootApplication
public class Starter {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Starter.class, args);
    }
}

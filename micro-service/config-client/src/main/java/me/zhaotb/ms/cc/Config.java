package me.zhaotb.ms.cc;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhaotangbo
 * @since 2020/11/27
 */
@Component
@ConfigurationProperties(prefix = "config")
public class Config {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

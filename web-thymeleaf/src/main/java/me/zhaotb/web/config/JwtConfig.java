package me.zhaotb.web.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author zhaotangbo
 * @since 2020/12/29
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    /**
     * 密钥
     */
    private String secret;

    /**
     * AccessToken有效时长（秒）
     */
    private int expire =  60 * 60 * 24;

    /**
     * 匹配需要校验的链接.注意前后不要留空格。与servlet的pattern格式一致，支持*通配
     */
    private List<String> patterns;

}

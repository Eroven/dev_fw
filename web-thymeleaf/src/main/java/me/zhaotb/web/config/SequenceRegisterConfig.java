package me.zhaotb.web.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 序列注册配置
 * @author zhaotangbo
 * @date 2021/1/4
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "seq")
public class SequenceRegisterConfig {

    private Map<String, SequenceConfig> configs;
}

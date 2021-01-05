package me.zhaotb.web.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 序列配置
 *
 * @author zhaotangbo
 * @date 2021/1/4
 */
@Data
@Configuration
public class SequenceConfig {

    /**
     * 序列名，一般是表名
     */
    private String name;

    /**
     * 如果是表，该字段表示对应的列名
     */
    private String column;

    /**
     * 起始位置
     */
    private long start = 1;

    /**
     * 步长
     */
    private long step = 1;

}

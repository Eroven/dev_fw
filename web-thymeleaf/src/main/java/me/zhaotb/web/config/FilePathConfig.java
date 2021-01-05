package me.zhaotb.web.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhaotangbo
 * @date 2021/1/5
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "file.pre-path")
public class FilePathConfig {

    /**
     * 头像路径
     */
    private String profilePhoto;

    /**
     * 头像挂件路径
     */
    private String profileQualifier;

    /**
     * 实名信息照片路径
     */
    private String idCard;

    /**
     * 视频路径
     */
    private String video;

}

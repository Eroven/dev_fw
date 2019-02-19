package me.zhaotb.admin.spring;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author zhaotangbo
 * @date 2019/2/11
 */
@Component
@ConfigurationProperties(prefix = "admin.env")
@PropertySource(value = {"classpath:config/application.properties", "file:config/application.properties"}, ignoreResourceNotFound = true)
public class Env {

    private static String home;

    private static String picPath;

    private static String logPath;

    private static String charset = "UTF-8";

    public static String getHome() {
        return home;
    }

    public static void setHome(String home) {
        Env.home = home;
    }

    public static String getPicPath() {
        return picPath;
    }

    public static void setPicPath(String picPath) {
        Env.picPath = picPath;
    }

    public static void setLogPath(String logPath) {
        Env.logPath = logPath;
    }

    public static String getCharset() {
        return charset;
    }

    public static void setCharset(String charset) {
        Env.charset = charset;
    }

    public static String getLogPath(){
        if (StringUtils.isNotBlank(logPath)){
            return logPath;
        }
        return home == null ? "log" : new File(home, "log").getAbsolutePath();
    }

    public static String getConfigPath(){
        return home == null ? "config" : new File(home, "config").getAbsolutePath();
    }
}

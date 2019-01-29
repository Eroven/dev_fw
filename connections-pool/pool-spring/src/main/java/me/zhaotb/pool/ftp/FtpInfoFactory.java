package me.zhaotb.pool.ftp;

import me.zhaotb.framework.util.StringUtil;
import org.springframework.core.env.Environment;

public class FtpInfoFactory {

    public static final String KEY_SPLITER = ".";

    public static FtpInfo factoryByProperties(Environment env, String prefix){
        FtpInfo info = new FtpInfo();
        info.setIp(env.getProperty(totalKey(prefix, "ip")));
        info.setPort(Integer.parseInt(env.getProperty(totalKey(prefix, "port"))));
        info.setUser(env.getProperty(totalKey(prefix, "user")));
        info.setPassword(env.getProperty(totalKey(prefix, "password")));
        return info;
    }

    private static String totalKey(String prefix, String key){
        if (StringUtil.isEmpty(prefix)){
            return key;
        }else {
            return prefix + KEY_SPLITER + key;
        }
    }
}

package me.zhaotb.app.api;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhaotangbo
 * @date 2019/3/4
 */
@Setter
@Getter
public class Env {

    private static String charset = "UTF-8";

    private static String IP_PORT_SEP = ":";

    public static String getCharset() {
        return charset;
    }

    public static void setCharset(String charset) {
        Env.charset = charset;
    }

    public static String getIpPortSep() {
        return IP_PORT_SEP;
    }

    public static void setIpPortSep(String ipPortSep) {
        IP_PORT_SEP = ipPortSep;
    }
}

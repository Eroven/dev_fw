package me.zhaotb.app.api.station;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.zhaotb.app.api.Env;

/**
 * @author zhaotangbo
 * @date 2019/3/1
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Address {
    private String ip;
    private int port;
    public String get(){
        return ip + ":" + port;
    }
    @Override
    public String toString() {
        return get();
    }

    public static Address parseAddress(String ipPort){
        String[] split = ipPort.split(Env.getIpPortSep(), 2);
        return new Address(split[0], Integer.parseInt(split[1]));
    }
    public static Address formatAddress(String ip, int port){
        return new Address(ip, port);
    }
}
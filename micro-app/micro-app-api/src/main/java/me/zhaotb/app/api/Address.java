package me.zhaotb.app.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhaotangbo
 * @date 2019/3/1
 */
@Getter
@AllArgsConstructor
public class Address {
    private String ip;
    private int port;
    Address(){}
    public String get(){
        return ip + ":" + port;
    }
    @Override
    public String toString() {
        return get();
    }
}
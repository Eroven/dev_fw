package me.zhaotb.app.api.register;

import me.zhaotb.app.api.Address;

import java.util.Hashtable;

/**
 * @author zhaotangbo
 * @date 2019/3/4
 */
public class LeaderCache {

    private static Hashtable<Address, Address> cacheIp = new Hashtable<>();

    public static void put(Address tickAddr, Address ctrlAddr){
        if (tickAddr == null || ctrlAddr == null){
            return;
        }
        cacheIp.put(tickAddr, ctrlAddr);
    }

    public static Hashtable<Address, Address> getAll(){
        return cacheIp;
    }

    public static void clear(){
        cacheIp.clear();
    }

}

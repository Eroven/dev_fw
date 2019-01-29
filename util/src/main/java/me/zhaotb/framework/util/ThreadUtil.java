package me.zhaotb.framework.util;

public class ThreadUtil {

    public static String threadName(){
        return Thread.currentThread().getName() + " " ;
    }

}

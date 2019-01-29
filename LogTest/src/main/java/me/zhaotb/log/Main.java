package me.zhaotb.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhaotangbo
 * @date 2018/12/20
 */
public class Main {

    private final Logger logger = LoggerFactory.getLogger("main");

    public void logAllLevel(){
        logger.trace("你好1, {}","trace");
        logger.debug("你好1, {}","debug");
        int len = 10000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < len; i++) {
            logger.info("你好1, {}","hello");
        }
        long end = System.currentTimeMillis();
        logger.warn("你好1, {}","warn info 打印 " + len + " 次");
        logger.error("你好1, {}","error 耗时 :" + (end - start));
    }

    private String content(String str){
        int len = 1000000;
        StringBuilder sb = new StringBuilder(len);
        for (int i = 1; i < len; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

}

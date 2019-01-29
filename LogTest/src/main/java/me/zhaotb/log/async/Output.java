package me.zhaotb.log.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhaotangbo
 * @date 2018/12/21
 */
public class Output {

    private final Logger logger = LoggerFactory.getLogger("output");

    public void logAllInfo(){
        logger.trace("你好, {}","trace");
        logger.debug("你好, {}","debug");
        int len = 10000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < len; i++) {
            logger.info("你好, {}","info");
        }
        long end = System.currentTimeMillis();
        logger.warn("你好, {}","warn info 打印 " + len + " 次");
        logger.error("你好, {}","error 耗时 :" + (end - start));
    }

}

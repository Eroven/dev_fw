package me.zhaotb.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhaotangbo
 * @date 2018/12/25
 */
public final class MyLogger {

    private static ThreadLocal<Class<?>> classHolder = ThreadLocal.withInitial(() -> MyLogger.class);

    private static ThreadLocal<Logger> loggerThreadLocal = ThreadLocal.withInitial(() -> LoggerFactory.getLogger(classHolder.get()));

    public static Logger getLogger(Class<?> tClass){
        classHolder.set(tClass);
        return loggerThreadLocal.get();
    }

}

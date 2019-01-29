package me.zhaotb.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.sift.AbstractDiscriminator;

/**
 * @author zhaotangbo
 * @date 2018/12/21
 */
public class LoggerNameDiscriminator extends AbstractDiscriminator<ILoggingEvent> {

    private static ThreadLocal<String> logCityId = ThreadLocal.withInitial(() -> "0");

    public static void setCityId(String cityId){
        logCityId.set(cityId);
    }

    @Override
    public String getDiscriminatingValue(ILoggingEvent iLoggingEvent) {
        return logCityId.get() + "/" + iLoggingEvent.getLoggerName();
    }

    @Override
    public String getKey() {
        return "loggerName";
    }
}

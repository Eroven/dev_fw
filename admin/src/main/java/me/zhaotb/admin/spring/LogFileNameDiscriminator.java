package me.zhaotb.admin.spring;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.sift.AbstractDiscriminator;

/**
 * @author zhaotangbo
 * @date 2019/2/12
 */
public class LogFileNameDiscriminator extends AbstractDiscriminator<ILoggingEvent> {

    @Override
    public String getDiscriminatingValue(ILoggingEvent iLoggingEvent) {
        return Env.getLogPath();
    }

    @Override
    public String getKey() {
        return "logPath";
    }
}

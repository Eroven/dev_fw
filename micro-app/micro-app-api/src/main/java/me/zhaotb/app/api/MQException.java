package me.zhaotb.app.api;

/**
 * @author zhaotangbo
 * @date 2018/12/20
 */
public class MQException extends RuntimeException {

    public MQException() {
    }

    public MQException(String message) {
        super(message);
    }

    public MQException(String message, Throwable cause) {
        super(message, cause);
    }

    public MQException(Throwable cause) {
        super(cause);
    }

    public MQException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

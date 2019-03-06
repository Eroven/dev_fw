package me.zhaotb.app.api.station;

/**
 * @author zhaotangbo
 * @date 2019/3/6
 */
public class HeartBeatException extends StationException{
    public HeartBeatException(String message) {
        super(message);
    }

    public HeartBeatException(String message, Throwable cause) {
        super(message, cause);
    }

    public HeartBeatException(Throwable cause) {
        super(cause);
    }
}

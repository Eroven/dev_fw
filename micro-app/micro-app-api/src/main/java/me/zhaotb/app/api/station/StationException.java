package me.zhaotb.app.api.station;

/**
 * @author zhaotangbo
 * @date 2019/3/1
 */
public class StationException extends Exception {

    public StationException(String message) {
        super(message);
    }

    public StationException(String message, Throwable cause) {
        super(message, cause);
    }

    public StationException(Throwable cause) {
        super(cause);
    }
}

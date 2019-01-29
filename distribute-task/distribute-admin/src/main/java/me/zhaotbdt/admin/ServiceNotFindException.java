package me.zhaotbdt.admin;

public class ServiceNotFindException extends RuntimeException {
    public ServiceNotFindException() {
    }

    public ServiceNotFindException(String message) {
        super(message);
    }

    public ServiceNotFindException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceNotFindException(Throwable cause) {
        super(cause);
    }

    public ServiceNotFindException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

package me.zhaotb.web.service;


/**
 * @author zhaotangbo
 * @since 2020/12/30
 */
public class RegisterException extends RuntimeException {

    public RegisterException(String message) {
        super(message);
    }

    public static class DuplicateNickNameException extends RegisterException {
        public DuplicateNickNameException(String message) {
            super(message);
        }
    }

    public static class InvalidAuthCodeException extends RegisterException {
        public InvalidAuthCodeException(String message) {
            super(message);
        }
    }
}

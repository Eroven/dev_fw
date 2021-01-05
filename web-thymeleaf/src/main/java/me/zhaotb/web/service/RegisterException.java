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

    public static class NeedNotRollbackException extends RegisterException {
        public NeedNotRollbackException(String message) {
            super(message);
        }
    }

    public static class InvalidAuthCodeException extends NeedNotRollbackException {
        public InvalidAuthCodeException(String message) {
            super(message);
        }
    }

    public static class AlreadyRegisterException extends NeedNotRollbackException {
        public AlreadyRegisterException(String message) {
            super(message);
        }
    }

}

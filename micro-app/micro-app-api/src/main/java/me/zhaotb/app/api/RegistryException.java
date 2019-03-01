package me.zhaotb.app.api;

/**
 * 填写/读取 注册信息时出错
 * @author zhaotangbo
 * @date 2019/3/1
 */
public class RegistryException extends RuntimeException {

    public RegistryException(String message) {
        super(message);
    }

    public RegistryException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegistryException(Throwable cause) {
        super(cause);
    }
}

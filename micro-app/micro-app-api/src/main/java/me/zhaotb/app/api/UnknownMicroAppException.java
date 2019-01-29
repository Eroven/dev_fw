package me.zhaotb.app.api;

/**
 * 抛出此异常表示找不到对应程序
 *
 * @author zhaotangbo
 * @date 2018/12/17
 */
public class UnknownMicroAppException extends RuntimeException {

    public UnknownMicroAppException(String message) {
        super(message);
    }

}

package me.zhaotb.app.api.station;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 保持长连接交流信息
 * @author zhaotangbo
 * @date 2019/3/7
 */
public interface Communicationable {

    /**
     * 向目标地址发消息
     * @param msg 请求消息
     * @return 一直等待返回消息
     * @throws InterruptedException 发送被中断
     * @throws IOException 发送过程发生IO异常
     */
    Msg send(Msg msg) throws InterruptedException, IOException;

    /**
     * 向目标地址发消息
     * @param msg 请求消息
     * @return 等待一定的时间，返回消息
     * @throws InterruptedException 发送被中断
     * @throws IOException 发送过程发生IO异常
     * @throws TimeoutException 如果超过等待时间，则抛出此异常
     */
    Msg send(Msg msg, long timeout, TimeUnit unit) throws InterruptedException, IOException, TimeoutException;

}

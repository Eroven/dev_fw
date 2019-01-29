package me.zhaotb.app.scheduler;

import me.zhaotb.app.api.MQException;
import me.zhaotb.app.api.Message;
import me.zhaotb.app.api.MicroApplication;
import me.zhaotb.app.api.MsgListener;
import me.zhaotb.app.imp.CliMicroAppManager;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

@Component
public class ApplicationDispatcher {

    private static ConcurrentHashMap<String, ConcurrentLinkedQueue<Message>> storage = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, MsgListener> listeners = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, MicroApplication> exitsApps = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, MicroApplication> runningApps = new ConcurrentHashMap<>();

    static {
        storage.putIfAbsent("MICRO-APP", new ConcurrentLinkedQueue<>());
        storage.putIfAbsent("STOP-APP", new ConcurrentLinkedQueue<>());
    }

    private static Executor executor = Executors.newCachedThreadPool();

    public boolean register(String topic, MsgListener listener){
        if (storage.containsKey(topic)){
            listeners.put(topic, listener);
        }
        return false;
    }

    public void send(Message msg, SendCallBack callBack){
        ConcurrentLinkedQueue<Message> messages = storage.get(msg.getTopic());
        if (messages != null) {
            messages.add(msg);
            executor.execute(() -> {
                MsgListener msgListener = listeners.get(msg.getTopic());
                if (msgListener == null){
                    throw new MQException("未注册监听: " + msg.getTopic());
                }
                Object receive = msgListener.receive(msg);
                if (callBack != null){
                    callBack.result(receive);
                }
            });
        }

    }

}

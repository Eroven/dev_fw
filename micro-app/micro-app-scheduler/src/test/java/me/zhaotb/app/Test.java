package me.zhaotb.app;

import me.zhaotb.app.api.CliMessage;
import me.zhaotb.app.api.CliMicroAppInfo;
import me.zhaotb.app.api.Message;
import me.zhaotb.app.imp.CallResult;
import me.zhaotb.app.imp.CliMicroAppDemo;
import me.zhaotb.app.imp.CliMicroAppManager;
import me.zhaotb.app.scheduler.ApplicationDispatcher;
import me.zhaotb.app.scheduler.CliMsgListener;
import me.zhaotb.app.scheduler.SendCallBack;
import me.zhaotb.app.scheduler.StopMsgListener;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Test {

    public static void main(String[] args) {

        CliMicroAppManager.register(CliMicroAppDemo.class);

        ApplicationDispatcher dispatcher = new ApplicationDispatcher();
        CliMicroAppManager cliMicroAppManager = new CliMicroAppManager();
        dispatcher.register("MICRO-APP", new CliMsgListener(cliMicroAppManager));
        dispatcher.register("STOP-APP", new StopMsgListener(cliMicroAppManager));

        doStart(dispatcher);

    }

    private static void doStart(ApplicationDispatcher dispatcher) {
        String topic = "MICRO-APP";
        CliMessage msg = new CliMessage(topic, "key", "cliAppDemo", "-say hello");
        dispatcher.send(msg, result -> doStop(dispatcher, result));
    }

    private static void doStop(ApplicationDispatcher dispatcher, Object result) {
        System.out.println(result);
        if (result instanceof CallResult){
            String key = ((CallResult) result).getKey();

            try {
                TimeUnit.MILLISECONDS.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String stopTopic = "STOP-APP";
            CliMessage stopMsg = new CliMessage(stopTopic, key, "cliAppDemo");
            dispatcher.send(stopMsg, null);

            doStart(dispatcher);
        }
    }

    @org.junit.Test
    public void  testManager(){
        CliMicroAppManager.register(CliMicroAppDemo.class);
    }

    @org.junit.Test
    public void testSplit(){
        String arg = "a  B aa";
        System.out.println(Arrays.toString(arg.split(" ")));
    }

}

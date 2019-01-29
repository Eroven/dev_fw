package me.zhaotb.app.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhaotangbo
 * @date 2018/12/21
 */
public class MicroAppContext {

    private static ThreadLocal<MicroAppContext> contextHolder = ThreadLocal.withInitial(MicroAppContext::new);

    private Logger logger;

    private String sessionId = "0";

    private String appName = "appContext";

    public static Logger logger(){
        MicroAppContext context = contextHolder.get();
        if (context.logger == null){
            context.logger = context.initLogger();
        }
        return context.logger;
    }

    private Logger initLogger(){
        return LoggerFactory.getLogger(appName + "/" + sessionId);
    }

    private MicroAppContext(){
    }

    private MicroAppContext(String sessionId, String appName) {
        this.sessionId = sessionId;
        this.appName = appName;
    }

    public static MicroAppContext instance(String sessionId, String appName){
        MicroAppContext context = new MicroAppContext(sessionId, appName);
        contextHolder.set(context);
        return context;
    }

    public MicroAppContext(MicroAppContext context){
        this.sessionId = context.sessionId;
        this.appName = context.appName;
    }

}

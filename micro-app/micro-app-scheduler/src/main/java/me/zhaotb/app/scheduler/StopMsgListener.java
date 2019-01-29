package me.zhaotb.app.scheduler;

import com.alibaba.fastjson.JSON;
import me.zhaotb.app.api.CliMessage;
import me.zhaotb.app.api.CliMicroAppInfo;
import me.zhaotb.app.api.MsgListener;
import me.zhaotb.app.imp.CallResult;
import me.zhaotb.app.imp.CliMicroAppManager;

/**
 * @author zhaotangbo
 * @date 2018/12/17
 */
public class StopMsgListener implements MsgListener<CliMessage, CallResult> {

    private CliMicroAppManager cliMicroAppManager;

    public StopMsgListener(CliMicroAppManager cliMicroAppManager) {
        this.cliMicroAppManager = cliMicroAppManager;
    }

    @Override
    public CallResult receive(CliMessage msg) {
        String key = msg.getKey();
        String body = msg.getBody();
        CliMicroAppInfo appInfo = JSON.parseObject(body, CliMicroAppInfo.class);
        String appName = appInfo.getAppName();

        boolean isRunning = cliMicroAppManager.isRunningApp(key, appName);
        if (isRunning) {
            return cliMicroAppManager.stop(key);
        }
        return new CallResult(false, key, "程序未运行");
    }
}

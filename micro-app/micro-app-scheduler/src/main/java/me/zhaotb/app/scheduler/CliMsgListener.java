package me.zhaotb.app.scheduler;

import com.alibaba.fastjson.JSON;
import me.zhaotb.app.api.CliApplication;
import me.zhaotb.app.api.CliMessage;
import me.zhaotb.app.api.Message;
import me.zhaotb.app.api.CliMicroAppInfo;
import me.zhaotb.app.api.MsgListener;
import me.zhaotb.app.imp.CallResult;
import me.zhaotb.app.imp.CliMicroAppManager;

import java.util.ArrayList;
import java.util.List;

public class CliMsgListener implements MsgListener<CliMessage, CallResult> {

    private CliMicroAppManager cliMicroAppManager;

    public CliMsgListener(CliMicroAppManager cliMicroAppManager) {
        this.cliMicroAppManager = cliMicroAppManager;
    }

    @Override
    public CallResult receive(CliMessage msg) {

        String body = msg.getBody();
        CliMicroAppInfo appInfo = JSON.parseObject(body, CliMicroAppInfo.class);

        String key = msg.getKey();
        CallResult run = cliMicroAppManager.run(appInfo.getAppName(), appInfo.getParamArray());

        return run;
    }
}

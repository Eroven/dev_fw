package me.zhaotb.app.api;

import com.alibaba.fastjson.JSON;

/**
 * @author zhaotangbo
 * @date 2018/12/20
 */
public class CliMessage extends Message {

    private CliMicroAppInfo appInfo;


    public CliMessage(String topic) {
        super(topic);
    }

    public CliMessage(String topic, String key) {
        super(topic, key);
    }

    public CliMessage(String topic, String key, String appName) {
        this(topic, key, appName, null);
    }

    public CliMessage(String topic, String key, String appName, String appParams) {
        super(topic, key);
        appInfo = new CliMicroAppInfo(appName, appParams);
        setBody(JSON.toJSONString(appInfo));
    }

    public void setAppInfo(CliMicroAppInfo appInfo) {
        this.appInfo = appInfo;
    }

    public CliMicroAppInfo getAppInfo() {
        return appInfo;
    }
}

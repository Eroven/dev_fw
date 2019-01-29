package me.zhaotb.app.api;

import com.alibaba.fastjson.annotation.JSONField;
import me.zhaotb.framework.util.StringUtil;

import java.util.ArrayList;

/**
 * @author zhaotangbo
 * @date 2018/12/20
 */
public class CliMicroAppInfo {

    private String appName;

    private String params;

    public CliMicroAppInfo(String appName) {
        this.appName = appName;
    }

    public CliMicroAppInfo(String appName, String params) {
        this.appName = appName;
        this.params = params;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    @JSONField(serialize = false)
    public String[] getParamArray(){
        String[] split = this.params.split(" ");
        ArrayList<String> arrays = new ArrayList<>(split.length);
        for (String param : split) {
            if (StringUtil.isNotBlank(param)){
                arrays.add(param);
            }
        }
        return arrays.toArray(new String[1]);
    }
}

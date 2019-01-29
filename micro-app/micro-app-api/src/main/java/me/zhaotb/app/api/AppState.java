package me.zhaotb.app.api;

/**
 * @author zhaotangbo
 * @date 2018/12/18
 */
public class AppState {

    private String key;

    private String appName;

    private Class<?> appClass;

    private State state;

    private String remark;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Class<?> getAppClass() {
        return appClass;
    }

    public void setAppClass(Class<?> appClass) {
        this.appClass = appClass;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "AppState{" +
                "key='" + key + '\'' +
                ", appName='" + appName + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    public enum State {
        STARTING, RUNNING, STOPPING, TERMINATED, FAILED
    }
}

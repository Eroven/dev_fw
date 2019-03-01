package me.zhaotb.app.api;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhaotangbo
 * @date 2018/12/18
 */
@Getter
@Setter
public class AppState {

    private String key;

    private String appName;

    private Class<?> appClass;

    private State state;

    private String remark;

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

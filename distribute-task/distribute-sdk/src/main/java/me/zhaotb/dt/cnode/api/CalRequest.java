package me.zhaotb.dt.cnode.api;

import java.io.Serializable;

public class CalRequest implements Serializable {

    private Object content;

    private Object param;

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }
}

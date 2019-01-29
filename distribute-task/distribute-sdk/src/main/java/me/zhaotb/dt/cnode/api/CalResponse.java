package me.zhaotb.dt.cnode.api;

import java.io.Serializable;

public class CalResponse implements Serializable {

    private int statusCode;

    private String desc;

    private Object attachment;

    public CalResponse(int statusCode) {
        this.statusCode = statusCode;
    }

    public static CalResponse success(){
        return new CalResponse(StatusCode.STATUS_OK);
    }

    public static CalResponse error(){
        return new CalResponse(StatusCode.STATUS_ERROR);
    }

    public CalResponse attach(Object obj){
        this.attachment = obj;
        return this;
    }

    public CalResponse desc(String msg){
        this.desc = msg;
        return this;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getDesc() {
        return desc;
    }

    public Object getAttachment() {
        return attachment;
    }
}

package me.zhaotb.oauth.server.controller;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhaotangbo
 * @date 2018/11/26
 */
public class BaseResponse implements Serializable {

    private Date timestamp = new Date();

    private String comment;

    private Object data;

    public BaseResponse(String comment, Object data) {
        this.comment = comment;
        this.data = data;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}

package me.zhaotb.oauth.server.controller;

import java.io.Serializable;

/**
 * @author zhaotangbo
 * @date 2018/11/24
 */
public class StatusCode implements Serializable {

    private String code;

    private String status;

    private String comment;

    public StatusCode(String code) {
        this.code = code;
    }

    public StatusCode(String code, String status) {
        this.code = code;
        this.status = status;
    }

    public StatusCode(String code, String status, String comment) {
        this.code = code;
        this.status = status;
        this.comment = comment;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}

package me.zhaotb.app.api;

public interface MsgListener<T extends Message, R> {

    R receive(T msg);
}

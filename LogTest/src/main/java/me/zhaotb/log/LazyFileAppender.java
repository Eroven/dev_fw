package me.zhaotb.log;

import ch.qos.logback.core.rolling.RollingFileAppender;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhaotangbo
 * @date 2018/12/20
 */

public class LazyFileAppender<E> extends RollingFileAppender<E> {

    private ReentrantLock lock = new ReentrantLock();

    private boolean init = false;

    @Override
    public void start() {
        //什么都不做,第一次append的时候才初始化
        started = true;
    }

    @Override
    protected void append(E eventObject) {
        if (!init){
            init();
        }
        super.append(eventObject);
    }

    private void init(){
        try {
            lock.lock();
            if (init){
                return;
            }
            super.start();
            init = true;
        } finally {
            lock.unlock();
        }
    }

}

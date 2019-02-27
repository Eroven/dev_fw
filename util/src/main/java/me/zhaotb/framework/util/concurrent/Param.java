package me.zhaotb.framework.util.concurrent;

public class Param {
    private int max;

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    private int producerSize;

    public int getProducerSize() {
        return producerSize;
    }

    public void setProducerSize(int producerSize) {
        this.producerSize = producerSize;
    }

    private int handlerSize;

    public int getHandlerSize() {
        return handlerSize;
    }

    public void setHandlerSize(int handlerSize) {
        this.handlerSize = handlerSize;
    }

    private int queueSize;

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    private long dealTime;

    public long getDealTime() {
        return dealTime;
    }

    public void setDealTime(long dealTime) {
        this.dealTime = dealTime;
    }

    public Param() {
    }

    public Param(int max, int producerSize, int handlerSize, int queueSize, long dealTime) {
        this.max = max;
        this.producerSize = producerSize;
        this.handlerSize = handlerSize;
        this.queueSize = queueSize;
        this.dealTime = dealTime;
    }

    @Override
    public String toString() {
        return "Param{" +
                "max=" + max +
                ", producerSize=" + producerSize +
                ", handlerSize=" + handlerSize +
                ", queueSize=" + queueSize +
                ", dealTime=" + dealTime +
                '}';
    }
}
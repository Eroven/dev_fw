package me.zhaotb.dt.sdk.api;

public enum DispatchType {

    ONE_NODE("随机挑或指定一个节点,一个任务一个任务的执行,执行完一个,才派送下一个"),
    ONE_NODE_CONCURRENT("随机挑或指定一个节点执行,一次性将任务全部派送过去"),
    ONE_BY_ONE("轮询节点,只有当一个节点任务返回,才派送下一个"),
    ONE_BY_ONE_CONCURRENT("轮询派送任务,一次性,全部派送完"),
    FAST_FIRST("速度优先,先返回结果,就给下一个任务");

    private String description;

    DispatchType(String description) {
        this.description = description;
    }
}
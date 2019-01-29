package me.zhaotb.dt.sdk.api;

import me.zhaotb.dt.cnode.api.CalResponse;

public interface MergeRule {

    /**
     * 注意并发
     * @param result 某一次请求的结果
     */
    void addResult(CalResponse result);

    CalResponse finalResult();
}

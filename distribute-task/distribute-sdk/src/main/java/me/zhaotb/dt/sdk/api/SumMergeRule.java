package me.zhaotb.dt.sdk.api;

import me.zhaotb.dt.cnode.api.CalResponse;

public class SumMergeRule implements MergeRule {

    private long res;

    public synchronized void addResult(CalResponse result){
        if (result.getAttachment() instanceof Number){
            res += ((Number)result.getAttachment()).longValue();
        }
    }

    public CalResponse finalResult(){
        return CalResponse.success().attach(res);
    }
}

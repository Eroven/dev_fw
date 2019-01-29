package me.zhaotb.dt.sdk.api;

import me.zhaotb.dt.cnode.api.CalService;

public interface TaskManager {

    String taskName();

    Class<? extends CalService> forService();

    DispatchRule dispatchRule();

    MergeRule mergeRule();

}

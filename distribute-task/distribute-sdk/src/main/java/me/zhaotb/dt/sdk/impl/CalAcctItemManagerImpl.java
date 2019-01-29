package me.zhaotb.dt.sdk.impl;

import me.zhaotb.dt.cnode.api.CalService;
import me.zhaotb.dt.cnode.api.report.MonthlyAcctItemService;
import me.zhaotb.dt.sdk.api.DispatchRule;
import me.zhaotb.dt.sdk.api.DispatchType;
import me.zhaotb.dt.sdk.api.MergeRule;
import me.zhaotb.dt.sdk.api.SumMergeRule;
import me.zhaotb.dt.sdk.api.Task;
import me.zhaotb.dt.sdk.api.report.CalAcctItemManager;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Task
@Component
public class CalAcctItemManagerImpl implements CalAcctItemManager {

    @Override
    public String taskName() {
        return "reportAcct";
    }

    @Override
    public Class<? extends CalService> forService() {
        return MonthlyAcctItemService.class;
    }

    @Override
    public DispatchRule dispatchRule() {
        DispatchRule rule = new DispatchRule();
        rule.setType(DispatchType.ONE_BY_ONE);
        rule.setContents(Arrays.asList(1,2,3,4,5,6,7,8));
        return rule;
    }

    @Override
    public MergeRule mergeRule() {
        return new SumMergeRule();
    }
}

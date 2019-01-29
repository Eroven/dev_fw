package me.zhaotb.dt.cnode.report;

import com.alibaba.dubbo.config.annotation.Service;
import me.zhaotb.dt.cnode.api.CalRequest;
import me.zhaotb.dt.cnode.api.CalResponse;
import me.zhaotb.dt.cnode.api.report.MonthlyAcctItemService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Service(interfaceClass = MonthlyAcctItemService.class)
@Component("acctItemReport")
public class MonthlyAcctItemServiceImpl implements MonthlyAcctItemService {

    @Value("${exp}")
    private int exp;

    @Override
    public CalResponse doServe(CalRequest request) {
        Object attach = request.getContent();
        if (attach instanceof Integer) {
            Integer base = (Integer) attach;
            return CalResponse.success().attach(base * exp + 1);
        }
        return CalResponse.error().desc("数据类型错误!");
    }
}

package me.zhaotb.mongodb.controller;

import me.zhaotb.mongo.dao.AcctItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("at")
public class AcctItemController {

    private final AcctItemDao acctItemDao;

    @Autowired
    public AcctItemController(AcctItemDao acctItemDao) {
        this.acctItemDao = acctItemDao;
    }

    @RequestMapping("sum/{bid}")
    public Long sum(@PathVariable("bid") String businessId){
        return acctItemDao.sumCharge(businessId);
    }

}

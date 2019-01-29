package me.zhaotb.mongo.dto;

public class AcctItem {

    private Long acctItemId;

    private Long charge;

    private String businessId;

    public Long getAcctItemId() {
        return acctItemId;
    }

    public void setAcctItemId(Long acctItemId) {
        this.acctItemId = acctItemId;
    }

    public Long getCharge() {
        return charge;
    }

    public void setCharge(Long charge) {
        this.charge = charge;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
}

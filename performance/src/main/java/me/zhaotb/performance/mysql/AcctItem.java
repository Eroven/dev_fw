package me.zhaotb.performance.mysql;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author zhaotangbo
 * @date 2018/12/4
 */
@Entity
public class AcctItem {

    @Column(name = "acct_item_id")
    private Long acctItemId;

    @Column(name = "acct_id")
    private Long acctId;

    @Column(name = "prod_inst_id")
    private Long prodInstId;

    @Column(name = "staff_id")
    private String staffId;

    @Column(name = "post_id")
    private String postId;

    @Column(name = "custom_item")
    private String customItem;

    public Long getAcctItemId() {
        return acctItemId;
    }

    public void setAcctItemId(Long acctItemId) {
        this.acctItemId = acctItemId;
    }

    public Long getAcctId() {
        return acctId;
    }

    public void setAcctId(Long acctId) {
        this.acctId = acctId;
    }

    public Long getProdInstId() {
        return prodInstId;
    }

    public void setProdInstId(Long prodInstId) {
        this.prodInstId = prodInstId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getCustomItem() {
        return customItem;
    }

    public void setCustomItem(String customItem) {
        this.customItem = customItem;
    }
}

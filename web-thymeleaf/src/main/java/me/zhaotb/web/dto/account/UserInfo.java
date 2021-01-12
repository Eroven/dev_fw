package me.zhaotb.web.dto.account;


import lombok.Data;

import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * 用户基本信息
 * @author zhaotangbo
 * @since 2020/12/30
 */
@Data
@Table(name = "user_info")
public class UserInfo {

    private Long id;
    @OneToOne(targetEntity = UserAccount.class)
    private Long uaId;
    private String nickName;
    private Date birthday;
    /**
     * -1 保密； 0 女； 1 男； 2 其他
     */
    private Integer sex = -1;
    private String signature;
    private Date createTime;

}

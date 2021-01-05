package me.zhaotb.web.dto.account;


import lombok.Data;

import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 实名认证信息：省份证
 * @author zhaotangbo
 * @since 2020/12/29
 */
@Data
@Table(name = "user_id_card")
public class UserIDCard {

    private Long id;
    @OneToOne(targetEntity = UserAccount.class)
    private Long uaId;
    /**
     * 类型： 100 身份证；
     */
    private String type;
    private String number;
    private String realName;
    /**
     * 正面照片路径
     */
    private String frontPhoto;
    /**
     * 背面照片路径
     */
    private String backPhoto;

}

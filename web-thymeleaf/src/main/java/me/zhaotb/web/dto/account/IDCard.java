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
@Table(name = "id_card")
public class IDCard {

    private Long id;
    @OneToOne(targetEntity = UserAccount.class)
    private Long uaId;
    private String type;//类型： 100 身份证；
    private String number;
    private String realName;
    private String frontPhoto;//正面照片路径
    private String backPhoto;//背面照片路径

}

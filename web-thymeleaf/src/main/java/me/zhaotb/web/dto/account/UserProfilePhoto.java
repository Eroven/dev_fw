package me.zhaotb.web.dto.account;


import lombok.Data;

import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * 头像和挂件
 * @author zhaotangbo
 * @date 2021/1/5
 */
@Data
@Table(name = "user_profile_photo")
public class UserProfilePhoto {

    private Long id;
    @OneToOne(targetEntity = UserAccount.class)
    private Long uaId;
    /**
     * 头像路径
     */
    private String path;
    /**
     * 挂件路径
     */
    private String qualifier;
    private Date updateTime;

}

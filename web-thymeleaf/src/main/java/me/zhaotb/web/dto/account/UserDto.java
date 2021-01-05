package me.zhaotb.web.dto.account;


import lombok.Data;

/**
 * 用户登录后的数据对象
 * @author zhaotangbo
 * @date 2021/1/5
 */
@Data
public class UserDto {

    private UserInfo userInfo;
    private UserAccount userAccount;

    /**
     * 创建时间
     */
    private long createTime;
    /**
     * 失效时间
     */
    private long expiredTime;


}

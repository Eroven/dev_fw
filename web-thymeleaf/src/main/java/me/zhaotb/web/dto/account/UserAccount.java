package me.zhaotb.web.dto.account;

import lombok.Data;

import javax.persistence.Table;

/**
 * 用户账号信息，用于登录
 * @author zhaotangbo
 * @since 2020/12/23
 */
@Data
@Table(name = "user_account")
public class UserAccount {

    private Long id;
    private String password;
    private String email;
    private String phoneNumber;

}

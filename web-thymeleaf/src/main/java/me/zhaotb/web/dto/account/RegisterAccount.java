package me.zhaotb.web.dto.account;


import lombok.Data;

/**
 * @author zhaotangbo
 * @since 2020/12/30
 */
@Data
public class RegisterAccount {

    private String password;
    private String email;
    private String phoneNumber;
    private String nickName;
    private String authCode;

}

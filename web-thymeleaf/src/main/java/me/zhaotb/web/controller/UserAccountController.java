package me.zhaotb.web.controller;


import lombok.extern.slf4j.Slf4j;
import me.zhaotb.web.dto.CommonResponse;
import me.zhaotb.web.dto.account.UserDto;
import me.zhaotb.web.dto.account.UserInfo;
import me.zhaotb.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static me.zhaotb.web.util.CRFactory.error;
import static me.zhaotb.web.util.CRFactory.ok;
import static me.zhaotb.web.util.CRFactory.okData;

/**
 * 个人信息数据交互接口
 *
 * @author zhaotangbo
 * @date 2021/1/8
 */
@Slf4j
@RestController
@RequestMapping("user/account")
public class UserAccountController {

    @Autowired
    private UserService userService;

    @RequestMapping("getUserInfo")
    public CommonResponse getUserInfo(@RequestAttribute("userDto") UserDto dto) {
        UserInfo userInfo = userService.getUserInfo(dto);
        return okData(userInfo);
    }

    @RequestMapping("updateUserInfo")
    public CommonResponse updateUserInfo(@RequestAttribute("userDto") UserDto dto,
                                         @RequestBody UserInfo userInfo) {
        log.info("{}", userInfo);
        if (userService.updateUserInfo(dto, userInfo) > 0) {
            return ok();
        } else {
            return error();
        }
    }

}

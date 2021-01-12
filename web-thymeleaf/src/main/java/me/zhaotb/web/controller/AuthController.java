package me.zhaotb.web.controller;


import me.zhaotb.web.dto.CommonResponse;
import me.zhaotb.web.dto.account.RegisterAccount;
import me.zhaotb.web.dto.account.UserAccount;
import me.zhaotb.web.service.EmailService;
import me.zhaotb.web.service.RegisterException;
import me.zhaotb.web.service.UserService;
import me.zhaotb.web.util.CRFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zhaotangbo
 * @since 2020/12/17
 */
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Controller
@RequestMapping("unAuth")
public class AuthController {

    @Autowired
    private UserService userService;

    @RequestMapping("view")
    public String view(){
        return "login";
    }


    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse login(@RequestBody UserAccount account) {
        String jwt = userService.login(account);
        if (jwt == null) {
            return CRFactory.errorMsg("账号或密码错误");
        } else {
            return CRFactory.okData(jwt);
        }
    }

    @RequestMapping(value = "authCode", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse authCode(@RequestBody UserAccount account) {
        userService.sendAuthCode(account);
        return CRFactory.ok();
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse register(@RequestBody RegisterAccount account) {
        try {
            userService.register(account);
        } catch (RegisterException.DuplicateNickNameException e) {
            return  CRFactory.DUP_NICK_NAME;
        } catch (RegisterException.InvalidAuthCodeException e) {
            return  CRFactory.errorMsg("无效验证码");
        }
        return CRFactory.ok();
    }

}


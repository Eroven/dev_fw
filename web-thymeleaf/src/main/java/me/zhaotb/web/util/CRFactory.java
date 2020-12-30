package me.zhaotb.web.util;


import me.zhaotb.web.dto.CommonResponse;
import org.springframework.http.HttpStatus;

/**
 * 响应结构体的工厂类
 * @author zhaotangbo
 * @since 2020/12/23
 * @see me.zhaotb.web.dto.CommonResponse
 */
public class CRFactory {

    private static final String E_700 = "700";//后台各种错误通用700，具体某种错误则用 7XX， 16进制，所以总共支持256种解释

    public static final CommonResponse DUP_NICK_NAME = new CommonResponse("710", "昵称重复");

    public static CommonResponse ok() {
        return new CommonResponse(String.valueOf(HttpStatus.OK.value()));
    }

    public static CommonResponse ok(String msg, Object data) {
        return new CommonResponse(String.valueOf(HttpStatus.OK.value()), msg, data);
    }

    public static CommonResponse okMsg(String msg) {
        return new CommonResponse(String.valueOf(HttpStatus.OK.value()), msg);
    }

    public static CommonResponse okData(Object data) {
        return new CommonResponse(String.valueOf(HttpStatus.OK.value()), null, data);
    }

    public static CommonResponse error() {
        return new CommonResponse(E_700);
    }

    public static CommonResponse errorMsg(String msg) {
        return new CommonResponse(E_700, msg);
    }

    public static CommonResponse errorFor(String code, String msg, Object data) {
        return new CommonResponse(code, msg, data);
    }




}

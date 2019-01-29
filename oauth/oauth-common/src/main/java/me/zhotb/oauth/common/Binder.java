package me.zhotb.oauth.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * spring controller 请求参数自定义绑定变量
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Binder {

    /**
     * 绑定变量变量名
     */
    String value() default "";
}

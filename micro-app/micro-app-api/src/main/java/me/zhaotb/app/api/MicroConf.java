package me.zhaotb.app.api;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhaotangbo
 * @date 2018/12/17
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MicroConf {

    /**
     * 指定程序适用范围
     */
    Scope scope() default Scope.SINGLETON;

    /**
     * 执行程序名, 默认使用类名首字母小写
     */
    @AliasFor("value")
    String name() default "";

    /**
     * @see #name();
     */
    @AliasFor("name")
    String value() default "";

    public enum Scope {

        /**
         * 单例
         */
        SINGLETON,

        /**
         * 原型
         */
        PROTOTYPE

    }


}

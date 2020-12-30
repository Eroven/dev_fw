package me.zhaotb.web.dto;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识关联的外键表及字段
 * @deprecated 使用jpa的注解代替了
 * @see javax.persistence.OneToOne
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Deprecated
public @interface Associative  {

    /**
     * 表名
     */
    String value();

    /**
     * 对应表的字段
     */
    String field() default "id";

}

package me.zhaotb.spring.springboot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 检查参数不能为空，否则抛出异常
 * 使用范围：类 -> 该类包含所有方法传参都不能为null
 * 方法 ->
 *
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNull {
}

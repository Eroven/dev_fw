package me.zhaotb.spring.springboot;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class MyAspect {

    @Pointcut("@annotation(me.zhaotb.spring.springboot.NotNull)")
    public void notNull(){}

    @Before("notNull()")
    public void checkNotNull(JoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        for (Object param : args){
            if(param == null){
                throw new IllegalArgumentException(point.getSignature().toString() + "所有参数均不能为null");
            }
        }
//        point.proceed();
    }

    @AfterReturning
    public void aspReturn(){
    }

    @AfterThrowing
    public void aspThr(){

    }

}


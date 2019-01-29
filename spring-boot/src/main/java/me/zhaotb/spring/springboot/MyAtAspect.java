package me.zhaotb.spring.springboot;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class MyAtAspect {

    @Pointcut("@annotation(me.zhaotb.spring.springboot.Log)")
    public void log(){}

    @After("log()")
    public void doLog(){
        System.out.println("log");

    }

    @Around(value = "log()")
    public void doAround(ProceedingJoinPoint point) throws Throwable {
        Object obj = point.getThis();
        Object[] args = point.getArgs();
        String kind = point.getKind();
        Object target = point.getTarget();
        point.proceed();
    }
}

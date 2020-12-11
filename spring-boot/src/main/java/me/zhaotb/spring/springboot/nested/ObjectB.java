package me.zhaotb.spring.springboot.nested;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhaotangbo
 * @since 2020/12/10
 */
@Component
public class ObjectB {

    private ObjectA a;

    @Autowired
    public void setA(ObjectA a) {
        this.a = a;
    }
}

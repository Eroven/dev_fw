package me.zhaotb.test;

import com.fasterxml.classmate.GenericType;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import org.junit.Test;

import java.util.List;

/**
 * @author zhaotangbo
 * @date 2019/1/21
 */
public class ClassMateTest {

    @Test
    public void cm(){
        TypeResolver tr = new TypeResolver();
        ResolvedType resolve = tr.resolve(new GenericType<List<Integer>>() {});
    }

}

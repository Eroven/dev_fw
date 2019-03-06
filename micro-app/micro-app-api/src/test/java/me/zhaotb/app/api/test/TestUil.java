package me.zhaotb.app.api.test;

import me.zhaotb.app.api.Util;
import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * @author zhaotangbo
 * @date 2019/3/6
 */
public class TestUil {

    @Test
    public void testInt(){
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(-99822123);
        int intB = Util.getIntB(buffer.array(), 0);
        System.out.println(intB);

    }

    @Test
    public void testSp(){
        String str = "abc=#aaa=#aaa";
        String[] split = str.split("=#");
        System.out.println();
    }

}

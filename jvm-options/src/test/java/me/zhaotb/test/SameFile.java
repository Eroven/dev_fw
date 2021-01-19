package me.zhaotb.test;

import org.junit.Test;

import java.io.File;

/**
 * @author zhaotangbo
 * @date 2019/2/1
 */
public class SameFile {

    @Test
    public void same() {
        File file = new File("F:\\tmp\\shard");
        File file2 = new File("F:\\tmp\\shard");

        System.out.println(file.equals(file2));
    }

    @Test
    public void testCeil() {
        System.out.println(Math.ceil(1.23D));
        System.out.println(Math.ceil(1.73D));
        System.out.println(Math.floor(-1.73D));
        System.out.println(Math.floor(-1.23D));
        System.out.println(Math.floor(-0.23D));
        System.out.println(Math.floor(-0.73D));
    }

    @Test
    public void testArcSin() {
        double a = 3;
        double b = 3;
        System.out.println(Math.asin(a / Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2))));
        System.out.println(Math.PI / 4);
    }

    @Test
    public void testArcTan() {
        double dx = -3;
        double dy = 3;
        System.out.println(Math.atan(dy / dx));
        System.out.println(Math.atan(1));
    }
}

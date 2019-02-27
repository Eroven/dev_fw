package me.zhaotb.test;

import me.zhaotb.framework.util.math.MathUtil;
import org.junit.Test;

/**
 * @author zhaotangbo
 * @date 2019/2/22
 */
public class TestMath {

    @Test
    public void testGCD() {
        System.out.println(MathUtil.gcd(18, 20));
        System.out.println(MathUtil.gcd(10, 20));
        System.out.println(MathUtil.gcd(11, 20));
        System.out.println(MathUtil.gcd(12, 20));
    }

    @Test
    public void testLCM(){
        System.out.println(MathUtil.lcm(18, 20));
    }
}

package me.zhaotb.framework.util.math;

/**
 * @author zhaotangbo
 * @date 2019/2/22
 */
public class MathUtil {

    private MathUtil() {
    }

    /**
     * 辗转相除求法
     * @param a 整数
     * @param b 整数
     * @return 返回最大公约数
     */
    public static int gcd(int a, int b){
        while (a % b != 0){
            a = a + b;
            b = a - b;
            a = a - b;
            b = b % a;
        }
        return b;
    }

    /**
     * @param a 整数
     * @param b 整数
     * @return 返回最小公倍数
     */
    public static int lcm(int a, int b){
        return a * b / gcd(a, b);
    }
}

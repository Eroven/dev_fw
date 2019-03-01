package me.zhaotb.service.admin;

/**
 * @author zhaotangbo
 * @date 2019/2/27
 */
public class TestStep {

    public static int step(int n){
        if (n == 1){
            return 1;
        }else if (n == 2){
            return 2;
        }return step(n - 1) + step(n - 2);
    }


    /**
     * 1 1 2 3 5 8 13 ...
     * 斐波那契数列 通项公式
     * @param n
     * @return 返回第n个数
     */
    public static int fb(int n){
        double v = Math.sqrt(5) / 5 * (Math.pow((1 + Math.sqrt(5)) / 2, (n + 1)) - Math.pow((1 - Math.sqrt(5)) / 2, (n + 1)));
        return (int) v;
    }

    public static void main(String[] args) {

        System.out.println(fb(1));
        System.out.println(fb(2));
        System.out.println(fb(3));
        System.out.println(fb(4));
        System.out.println(fb(5));
        System.out.println(fb(6));
        System.out.println(fb(7));

        System.out.println(step(3));
        System.out.println(step(4));
        System.out.println(step(5));
    }
}

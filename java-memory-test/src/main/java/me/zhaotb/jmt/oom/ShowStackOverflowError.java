package me.zhaotb.jmt.oom;


/**
 * @author zhaotangbo
 * @since 2020/12/10
 */
public class ShowStackOverflowError {

    private long sum(long a, long b) {
        if (a > 0)
            return 1 + sum(a - 1, b);
        else if (b > 0)
            return 1 + sum(a, b - 1);
        else
            return 0;
    }

    public static void main(String[] args) {
        ShowStackOverflowError ssofe = new ShowStackOverflowError();
        long sum = ssofe.sum(100000000L, 200000000L);
        System.out.println(sum);
    }
}

package me.zhaotb.jmt;


import java.util.Random;

/**
 * @author zhaotangbo
 * @since 2020/11/19
 */
public class RandomObject {

    private static char[] fillers = new char[126 - 32];

    static {
        char t = 33;
        for (int i = 0; i < fillers.length; i++,t++) {
            fillers[i] = t;
        }
    }

    private Random random = new Random();

    public MyObject anyStringObj(int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int j = random.nextInt(fillers.length);
            sb.append(fillers[j]);
        }
        return new MyObject(sb.toString());
    }

}

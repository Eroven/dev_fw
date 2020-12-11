package me.zhaotb.jvm;


import java.awt.AWTException;

/**
 * @author zhaotangbo
 * @since 2020/12/10
 */
public class Robot {

    public static void main(String[] args) throws AWTException, InterruptedException {
        java.awt.Robot r = new java.awt.Robot();
        int[][] coor = new int[][]{{100, 200}, {200, 300}, {300, 400}, {400, 500}, {500, 600}};
        int i = 0;
        while (true){
            Thread.sleep(1000 * 10);
            int[] coo = coor[i++ % coor.length];
            r.mouseMove(coo[0],coo[1]);
        }
    }
}

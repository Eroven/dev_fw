package me.zhaotb.test;

import java.util.Scanner;

/**
 * @author zhaotangbo
 * @date 2019/1/22
 */
public class Niuk {

    public static boolean valid(String str) {
        if (str == null || str.length() < 8) {
            return false;
        }
        char lls = 'a', lle = 'z';
        char uls = 'A', ule = 'Z';
        char ns = '0', ne = '9';
        char[] chars = str.toCharArray();
        if (chars[0] >= ns && chars[0] <= ne) {
            return false;
        }
        int type = 0;
        for (char c : chars) {
            if (ns <= c && ne >= c) {
                type += 1;
            } else if (lls <= c && lle >= c) {
                type += 1;
            } else if (uls <= c && ule >= c) {
                type += 1;
            } else {
                return false;
            }

            if (type >= 3) {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        sc.nextLine();
        while (n-- > 0) {
            if (valid(sc.nextLine())) {
                System.out.println("YES");
            } else {
                System.out.println("NO");
            }
        }
    }
}

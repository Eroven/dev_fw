package me.zhaotb.framework.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class FilterUtilTest {

    //    @Test
    public void testFilter() {
        List<FilterUtil.FilterResult<Vo>> res = FilterUtil.filterGreedy(Arrays.asList(new Vo(6, 2),
                new Vo(3, 2), new Vo(5, 6),
                new Vo(4, 5), new Vo(6, 4)), 10);
        System.out.println(res);
    }

    private class Vo implements PickAble {
        private int value;
        private int weight;

        private Vo(int value, int weight) {
            this.value = value;
            this.weight = weight;
        }

        @Override
        public int value() {
            return value;
        }

        @Override
        public int weight() {
            return weight;
        }
    }

    @Test
    public void testTurn1() {
        int[] arr = {0};
        int num = maxTurn(arr, 0, false);
        System.out.println(num);
        Assert.assertEquals(1, num);
    }

    @Test
    public void testTurn2() {
        int[] arr = {1};
        int num = maxTurn(arr, 0, false);
        System.out.println(num);
        Assert.assertEquals(0, num);
    }

    @Test
    public void testTurn3() {
        int[] arr = {0, 0};
        int num = maxTurn(arr, 0, false);
        System.out.println(num);
        Assert.assertEquals(1, num);
        arr = new int[]{0, 0, 0};
        num = maxTurn(arr, 0, false);
        System.out.println(num);
        Assert.assertEquals(2, num);
    }

    @Test
    public void testTurn4() {
        int[] arr = {1, 1};
        int num = maxTurn(arr, 0, false);
        System.out.println(num);
        Assert.assertEquals(0, num);
        arr = new int[]{1, 1, 1};
        num = maxTurn(arr, 0, false);
        System.out.println(num);
        Assert.assertEquals(0, num);
    }

    @Test
    public void testTurn5() {
        int[] arr = {1, 0 , 0 , 0, 0};
        int num = maxTurn(arr, 0, false);
        System.out.println(num);
        Assert.assertEquals(0, num);
    }

    @Test
    public void testTurn(){
        int[] arr = {1, 0 , 0 , 0, 0, 0, 0, 0, 0,  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 , 0 , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        long start = System.currentTimeMillis();
        int num = maxTurn2(arr);
        long end = System.currentTimeMillis();
        System.out.println(num + "\t" + (end - start));
        int i = maxTurn(arr, 0, false);
        long last = System.currentTimeMillis();
        System.out.println(i + "\t" + (last - end));
    }



    public static int maxTurn2(int[] arr){
        int max = 0;
        int continuousZero = 1;//开始算一个0
        for (int i : arr) {
            if (i == 0){
                continuousZero ++;
            }else {
                if (continuousZero > 3) {
                    max += (continuousZero - 1) / 2;
                }
                continuousZero = 0;
            }
        }
        continuousZero ++;//结尾算一个零
        if (continuousZero > 3) {
            max += (continuousZero - 1) / 2;
        }
        return max;
    }


    public static int maxTurn(int[] arr, int idx, boolean lastTurn) {

        if (idx + 1 >= arr.length) {
            if (lastTurn || arr[idx] == 1) {
                return 0;
            } else {
                return 1;
            }
        }
        if (lastTurn) {
            if (arr[idx] == 1) {
                return maxTurn(arr, idx + 1, true);
            } else {
                return maxTurn(arr, idx + 1, false);
            }
        } else {
            if (arr[idx] == 1 || arr[idx + 1] == 1) {
                return maxTurn(arr, idx + 1, true);
            } else {
                int a = 1 + maxTurn(arr, idx + 1, true);
                int b = maxTurn(arr, idx + 1, false);
                return Math.max(a, b);
            }
        }
    }
}
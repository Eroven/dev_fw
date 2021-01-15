package me.zhaotb.leecode;


import me.zhaotb.leecode.day20210115.MinCost;
import me.zhaotb.leecode.day20210115.Trap;
import org.junit.Test;

/**
 * @author zhaotangbo
 * @date 2021/1/15
 */
public class ExecuteDay20210115Test {

    @Test
    public void testMinCost() {
        MinCost minCost = new MinCost();
        System.out.println(minCost.minCost("abaac", new int[]{1, 2, 3, 4, 5}));
        System.out.println(minCost.minCost("abc", new int[]{1, 2, 3}));
        System.out.println(minCost.minCost("aabaa", new int[]{1, 2, 3, 4, 1}));


    }

    @Test
    public void testTrap() {
        Trap trap = new Trap();
        System.out.println(trap.trap(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}));

    }

}

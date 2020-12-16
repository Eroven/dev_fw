package me.zhaotb.leecode;


import me.zhaotb.leecode.day20201216.MedianFinder;
import me.zhaotb.leecode.day20201216.PrintWords;
import org.junit.Test;

import java.util.PriorityQueue;
import java.util.TreeSet;

/**
 * @author zhaotangbo
 * @since 2020/12/16
 */
public class ExecuteDay20201216Test {

    @Test
    public void testRedBlackTree(){
        TreeSet<Integer> tree = new TreeSet<>();
        tree.add(1);
        tree.add(2);
        tree.add(3);
        tree.add(4);
        tree.add(5);
        tree.add(6);
        tree.add(7);
        tree.add(8);
        tree.add(9);
        tree.add(10);
        tree.add(11);
        tree.add(12);
        tree.add(13);
        tree.add(14);
        tree.add(15);
        tree.add(16);
        tree.add(17);
        System.out.println(tree);
    }

    @Test
    public void testPriorityQueue(){
//        PriorityQueue<Integer> queue = new PriorityQueue<>(3, Comparator.reverseOrder());
        PriorityQueue<Integer> queue = new PriorityQueue<>(3);
        queue.offer(2);
        queue.offer(4);
        queue.offer(1);
        queue.offer(3);
        System.out.println(queue);
        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());

    }

    @Test
    public void testMedianFinder(){
        MedianFinder finder = new MedianFinder();
        finder.addNum(1);
        finder.addNum(2);
        System.out.println(finder.findMedian());
        finder.addNum(3);
        System.out.println(finder.findMedian());

    }

    @Test
    public void testArrayOverride(){
        PrintWords printWords = new PrintWords();
        System.out.println(printWords.printVertically("Hello Im girl"));
        System.out.println(printWords.printVertically("Hello Im very beautiful"));
        System.out.println(printWords.printVertically("Hello Im a beautiful girl"));

    }

}

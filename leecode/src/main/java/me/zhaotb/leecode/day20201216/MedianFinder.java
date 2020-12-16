package me.zhaotb.leecode.day20201216;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 随机产生数字并传递给一个方法。你能否完成这个方法，在每次产生新值时，寻找当前所有值的中间值（中位数）并保存。
 *
 * 中位数是有序列表中间的数。如果列表长度是偶数，中位数则是中间两个数的平均值。
 *
 * 例如，
 *
 * [2,3,4] 的中位数是 3
 *
 * [2,3] 的中位数是 (2 + 3) / 2 = 2.5
 *
 * 设计一个支持以下两种操作的数据结构：
 *
 *     void addNum(int num) - 从数据流中添加一个整数到数据结构中。
 *     double findMedian() - 返回目前所有元素的中位数。
 *
 * 示例：
 *
 * addNum(1)
 * addNum(2)
 * findMedian() -> 1.5
 * addNum(3)
 * findMedian() -> 2
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/continuous-median-lcci
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * @author zhaotangbo
 * @since 2020/12/16
 */
public class MedianFinder {

    private MedianFindable medianFindable;

    /** initialize your data structure here. */
    public MedianFinder() {
        medianFindable = new MaxMinHeapImpl();
    }

    public void addNum(int num) {
        medianFindable.addNum(num);
    }

    public double findMedian() {
        return medianFindable.findMedian();
    }

    private interface MedianFindable {
        void addNum(int num);
        double findMedian();
    }

    private class MaxMinHeapImpl implements MedianFindable {
        private PriorityQueue<Integer> max = new PriorityQueue<>(Comparator.reverseOrder());
        private PriorityQueue<Integer> min = new PriorityQueue<>();

        @Override
        public void addNum(int num) {
            if (max.size() > min.size()) {
                max.offer(num);
                min.offer(max.poll());
            } else {
                min.offer(num);
                max.offer(min.poll());
            }
        }

        @Override
        public double findMedian() {
            if (max.size() > min.size()) {
                return max.peek();
            } else if (min.size() > max.size()) {
                return min.peek();
            } else {
                return (min.peek() + max.peek()) / 2D;
            }
        }
    }

    private class ArrayListImpl implements MedianFindable {
        ArrayList<Integer> list = new ArrayList<>();
        @Override
        public void addNum(int num) {
            list.add(num);
            Collections.sort(list);
        }

        @Override
        public double findMedian() {
            if (list.size() % 2 == 0) {
                return (list.get((list.size() - 1) /2) + list.get(list.size() / 2)) / 2D;
            } else {
                return list.get(list.size() / 2);
            }
        }
    }

    private class DoubleChainImpl implements MedianFindable {
        private class Node {
            int val;
            private Node pre;
            private Node next;
            Node(int val) {
                this.val = val;
            }
        }
        private Node mid;
        private int size;

        @Override
        public void addNum(int num) {
            size += 1;
            Node node = new Node(num);
            if (mid == null) {
                mid = node;
            } else {
                Node tmp = mid;
                Node last;
                if (tmp.val >= num) {//往左边移动
                    do {
                        last = tmp;
                        tmp = tmp.pre;
                    } while (tmp != null && tmp.val >= num);
                    if (tmp != null) {
                        tmp.next = node;
                        node.pre = tmp;
                    }
                    last.pre = node;
                    node.next = last;
                    if (size % 2 == 0) mid = mid.pre;//左边加一个数变成偶数时，向左移动一个
                } else { //往右边移动
                    do {
                        last = tmp;
                        tmp = tmp.next;
                    } while (tmp != null && tmp.val < num);
                    if (tmp != null) {
                        tmp.pre = node;
                        node.next = tmp;
                    }
                    last.next = node;
                    node.pre = last;

                    if (size % 2 == 1) mid = mid.next;//右边加一个数变成奇数数，向右移动一个
                }
            }
        }

        @Override
        public double findMedian() {
            if (size % 2 == 1) {
                return mid.val;
            } else {
                return (mid.val + mid.next.val) / 2D;
            }
        }
    }

}

/**
 * Your MedianFinder object will be instantiated and called as such:
 * MedianFinder obj = new MedianFinder();
 * obj.addNum(num);
 * double param_2 = obj.findMedian();
 */
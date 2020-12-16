package me.zhaotb.leecode.day20201216;


import java.util.TreeMap;

/**
 * @author zhaotangbo
 * @since 2020/12/16
 */
public class RedBlackTree<T> {

    public Node<T> root;

    void test(){
        TreeMap<Integer, Object> treeMap = new TreeMap<>();
        treeMap.put(1, 1);
    }



    private static final boolean RED = true;
    private static final boolean BLACK = false;
    private class Node<E> {

        private E val;
        private Node<E> left;
        private Node<E> right;
        private boolean color = BLACK;



    }

}

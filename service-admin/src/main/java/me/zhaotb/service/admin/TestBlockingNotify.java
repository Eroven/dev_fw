package me.zhaotb.service.admin;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhaotangbo
 * @date 2019/2/27
 */
public class TestBlockingNotify {


    private static class MyBlockingQueue<E> {
        int capacity;

        AtomicInteger count = new AtomicInteger();

        Node<E> head;
        Node<E> last;

        ReentrantLock putLock = new ReentrantLock();
        ReentrantLock takeLock = new ReentrantLock();

        Condition full = putLock.newCondition();
        Condition empty = takeLock.newCondition();

        MyBlockingQueue(){
            this(Integer.MAX_VALUE);
        }

        MyBlockingQueue(int initCap){
            this.capacity = initCap;
            this.head = this.last = new Node<>(null);
        }

        void signalNotFull(){
            try{
                putLock.lock();
                full.signal();
            }finally {
                putLock.unlock();
            }
        }

        void signalNotEmpty(){
            try{
                takeLock.lock();
                empty.signal();
            }finally {
                takeLock.unlock();
            }
        }

        void put(E e) throws InterruptedException {
            Node<E> node = new Node<>(e);
            int c;
            try {
                putLock.lock();
                while (count.get() >= capacity){
                    full.await();
                }
                enqueue(node);
                c = count.getAndIncrement();
                if (c + 1 < capacity){
                    full.signal();
                }
            }finally {
                putLock.unlock();
            }
            if (c == 0){
                signalNotEmpty();
            }
        }

        E take() throws InterruptedException {
            int c;
            E item;
            try {
                takeLock.lock();
                while (count.get() < 1){
                    empty.await();
                }
                item = dequeue();
                c = count.getAndDecrement();
                if (c > 0){
                    empty.signal();
                }
            }finally {
                takeLock.unlock();
            }
            if (c == capacity){
                signalNotFull();
            }

            return item;
        }

        void enqueue(Node<E> node){
            last = last.next = node;
        }

        E dequeue(){
            Node<E> first = head.next;
            head.next = null;
            head = first;
            return first.item;
        }

    }

    private static class Node<E> {
        E item;
        Node<E> next;
        Node(E item){this.item = item;}
    }

    static MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(5);

    public static void main(String[] args) {

        ReentrantLock lock = new ReentrantLock();
        Thread thread = new Thread(() -> {
            try {
                lock.lockInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("LOCK");
            lock.unlock();
        });
        Thread thread2 = new Thread(() -> {
            lock.lock();
            System.out.println("LOCK");
            lock.unlock();
        });
        thread.start();
        thread2.start();
        thread.interrupt();


        new Thread(TestBlockingNotify::put).start();
        new Thread(TestBlockingNotify::put).start();
        new Thread(TestBlockingNotify::put).start();

        new Thread(TestBlockingNotify::take).start();
        new Thread(TestBlockingNotify::take).start();
        new Thread(TestBlockingNotify::take).start();

    }

    public static void put(){
        try {
            queue.put(1);
            queue.put(2);
            queue.put(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void take(){
        try {
            Integer take = queue.take();
            Integer take2 = queue.take();
            Integer take3 = queue.take();
            System.out.println(take + ":" + take2 + ":" + take3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

package me.zhaotb.framework.util.concurrent;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zhaotangbo
 * @date 2019/2/21
 */
public class TestJavaBlockingQueue {

    private Param param;

    private AtomicLong all = new AtomicLong();
    private volatile boolean produceEnd = false;
    private LinkedBlockingQueue<Integer> queue;

    public TestJavaBlockingQueue(Param param) {
        this.param = param;
    }

    public void start() {
        long s = System.currentTimeMillis();
        queue = new LinkedBlockingQueue<>(getQueueSize());

        int avg = getMax() / getProducerSize();
        int left = getMax() - avg * getProducerSize();
        Thread[] produceThreads = new Thread[getProducerSize()];
        for (int i = 0; i < getProducerSize(); i++) {
            if (i == 0) {
                produceThreads[i] = new Thread(new Producer(avg + left), "生产者-" + i);
            } else {
                produceThreads[i] = new Thread(new Producer(avg), "生产者-" + i);
            }
            produceThreads[i].start();
        }

        Thread[] threads = new Thread[getHandlerSize()];
        for (int i = 0; i < getHandlerSize(); i++) {
            threads[i] = new Thread(this::consume, "消费者-" + i);
            threads[i].start();
        }

        try {
            for (Thread thread : produceThreads) {
                thread.join();
            }

            produceEnd = true;
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        long e = System.currentTimeMillis();
        System.out.println(all.get() + " : " + param + " 耗时： " + (e - s));
    }

    private void consume() {
        long sum = 0;
        while (!produceEnd || queue.size() > 0) {
            try {
                Integer val = queue.poll(5, TimeUnit.MILLISECONDS);
                if (val != null) {
                    sum += val;
                }
                Thread.sleep(getDealTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        all.addAndGet(sum);
    }

    public int getMax() {
        return param.getMax();
    }

    public int getProducerSize() {
        return param.getProducerSize();
    }


    public int getHandlerSize() {
        return param.getHandlerSize();
    }

    public int getQueueSize() {
        return param.getQueueSize();
    }

    public long getDealTime() {
        return param.getDealTime();
    }

    private class Producer implements Runnable {
        private int pMax;

        public Producer(int pMax) {
            this.pMax = pMax;
        }

        @Override
        public void run() {
            for (int i = 0; i < pMax; i++) {
                try {
                    queue.put(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {

        int max = 1000000;
        int producerSize = 1;
        int handlerSize = 16;
        int queueSize = 16;
        long dealTime = 0;
        Param param = new Param(max, producerSize, handlerSize, queueSize, dealTime);
        new TestJavaBlockingQueue(param).start();
        new TestJavaBlockingQueue(param).start();
        System.out.println("------------开始！");

        for (int i = 0; i < 6; i++) {
            doubleQueueSize(param);
        }

        param.setQueueSize(queueSize);
        param.setHandlerSize(handlerSize * 2);
        for (int i = 0; i < 6; i++) {
            doubleQueueSize(param);
        }

        param.setQueueSize(queueSize);
        param.setHandlerSize(handlerSize);
        param.setMax(10000);
        param.setDealTime(1);
        for (int i = 0; i < 6; i++) {
            doubleQueueSize(param);
        }

        param.setQueueSize(queueSize);
        param.setHandlerSize(handlerSize * 2);
        for (int i = 0; i < 6; i++) {
            doubleQueueSize(param);
        }

        param.setQueueSize(queueSize);
        param.setHandlerSize(handlerSize * 4);
        for (int i = 0; i < 6; i++) {
            doubleQueueSize(param);
        }

        System.out.println("------------结束！");
    }

    private static void doubleQueueSize(Param param) {
        param.setQueueSize(param.getQueueSize() * 2);
        new TestJavaBlockingQueue(param).start();
        new TestJavaBlockingQueue(param).start();
        new TestJavaBlockingQueue(param).start();
        new TestJavaBlockingQueue(param).start();
        new TestJavaBlockingQueue(param).start();
    }
}

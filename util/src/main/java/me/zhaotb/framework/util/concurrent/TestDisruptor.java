package me.zhaotb.framework.util.concurrent;

import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zhaotangbo
 * @date 2019/2/21
 */
public class TestDisruptor {

    private Disruptor<Event<Integer>> disruptor = new Disruptor<>(Event::new, 1024  , new ThreadFactory() {
        AtomicLong count = new AtomicLong(1);
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "队列线程-" + count.getAndIncrement());
        }
    });

    private ExecutorService service = Executors.newFixedThreadPool(6);

    private MyWorkHandler[] workHandlers;

    private int getHandlerSize() {
        return 32;
    }

    public void start(){

        workHandlers = getWorkHandlers();
        disruptor.handleEventsWithWorkerPool(workHandlers);
        disruptor.start();

        produceTask();

        disruptor.shutdown();
        service.shutdown();

        long all = 0;
        for (MyWorkHandler handler : workHandlers) {
            all += handler.getSum();
        }
        System.out.println("all ： " + all);
    }

    public void produceTask(){
        long start = System.currentTimeMillis();
        long tmp;
        long sum = 0;
        int max = 1000000;
        System.out.println("max : " + max);
        for (int i = 0; i < max; i++) {
            tmp = System.currentTimeMillis();
            disruptor.publishEvent(this::translateTo, 1);
            sum += System.currentTimeMillis() - tmp;
        }
        long end = System.currentTimeMillis();
        System.out.println("生产完毕, 耗时：" + (end - start) + ", 阻塞： " + (sum));
    }

    public void translateTo(Event<Integer> event, long sequence, Integer val){
        event.setVal(val);
    }


    public  MyWorkHandler[] getWorkHandlers(){
        MyWorkHandler[] handlers = new MyWorkHandler[getHandlerSize()];
        for (int i = 0; i < handlers.length; i++) {
            handlers[i] = new MyWorkHandler();
        }
        return handlers;
    }

    public class MyWorkHandler implements WorkHandler<Event<Integer>> {

        private long sum;

        public long getSum() {
            return sum;
        }

        @Override
        public void onEvent(Event<Integer> event)   {
            Integer val = event.getVal();
            sum += val;
            if (sum % 50000 == 0){
                System.out.println(Thread.currentThread().getName() + " : " + sum);
            }
        }
    }

    public class Event<V> implements Serializable {
        private V val;
        public V getVal() {
            return val;
        }

        public void setVal(V val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {

        long s = System.currentTimeMillis();
        new TestDisruptor().start();
        long e = System.currentTimeMillis();
        System.out.println("程序耗时： " + (e - s));

    }

}

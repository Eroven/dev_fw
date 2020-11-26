package me.zhaotb.performance.concurrent;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhaotangbo
 * @since 2020/11/24
 */
public class ScheduledTest {

    public static void main(String[] args) {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(4, new ThreadFactory() {
            private AtomicInteger count = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "T-" + count.incrementAndGet());
            }
        });

        SimpleDateFormat mainFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String main = Thread.currentThread().getName();
        System.out.println(String.format("%s %s", main, mainFormat.format(new Date())));
        service.scheduleWithFixedDelay(()->{
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                String name = Thread.currentThread().getName();
                System.out.println(String.format("%s %s %s","fixedDelay", name, format.format(new Date())));
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }, 2000, 4000, TimeUnit.MILLISECONDS);
        service.scheduleAtFixedRate(()->{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String name = Thread.currentThread().getName();
            System.out.println(String.format("%s %s %s","fixedRate", name, format.format(new Date())));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 3000, 5000, TimeUnit.MILLISECONDS);

        System.out.println("MAIN");


    }

}

package me.zhaotb.util;

import org.junit.Test;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author zhaotangbo
 * @date 2019/1/14
 */
public class DelayTest {

    @Test
    public void testDelayQueue() throws Exception {
        DelayQueue<Data> queue = new DelayQueue<>();
        queue.put(new Data("你好"));
        Thread.sleep(3000);
        queue.put(new Data("延迟"));

        while (queue.size() > 0){
            Data poll = queue.poll();
            if (poll == null){
                Thread.sleep(1000);
                continue;
            }else {
                System.out.println(poll.getData());
            }
        }


    }

    public static class Data implements Delayed {

        private String data;
        private long start ;

        public Data(String data) {
            this.data = data;
            start = System.currentTimeMillis();
        }

        public String getData() {
            return data;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            long end = System.currentTimeMillis();
            long l = TimeUnit.SECONDS.toMillis(10);

            long sourceDuration = l - end + start;
            return unit.convert(sourceDuration, TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            return Long.compare(this.getDelay(TimeUnit.MILLISECONDS), o.getDelay(TimeUnit.MILLISECONDS));
        }
    }
}

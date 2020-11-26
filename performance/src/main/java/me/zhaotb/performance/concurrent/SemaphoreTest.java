package me.zhaotb.performance.concurrent;


import java.util.concurrent.Semaphore;

/**
 * @author zhaotangbo
 * @since 2020/11/20
 */
public class SemaphoreTest {

    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphore = new Semaphore(2);

        System.out.println(semaphore);

        semaphore.release(2);

        System.out.println(semaphore);

        semaphore.acquire();
        semaphore.acquire();
        semaphore.acquire();
        semaphore.acquire();
        System.out.println(semaphore);


    }
}

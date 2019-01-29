package me.zhaotb.app.imp;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhaotangbo
 * @date 2018/12/18
 */
public class ReentrantKeyLock<K> {

    private ReentrantLock realLock;

    private HashMap<K, ReentrantLock> locks = new HashMap<>();

    public ReentrantKeyLock() {
        realLock = new ReentrantLock();
    }

    public boolean tryLock(K key, long timeout, TimeUnit timeUnit) throws InterruptedException {
        locks.putIfAbsent(key, new ReentrantLock());
        ReentrantLock lock = locks.get(key);
        return lock.tryLock(timeout, timeUnit);
    }

    public void unlock(K key){
        ReentrantLock lock = locks.get(key);
        if (lock != null){
            lock.unlock();
            locks.remove(key);
        }
    }

}

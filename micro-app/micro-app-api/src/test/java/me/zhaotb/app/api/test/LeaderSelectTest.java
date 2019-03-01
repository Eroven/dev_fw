package me.zhaotb.app.api.test;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhaotangbo
 * @date 2019/3/1
 */
public class LeaderSelectTest {

    @Test
    public void testSelector() {


        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181",
                new ExponentialBackoffRetry(1000, 3, 3000));
        client.start();

        LeaderSelector leader = new LeaderSelector(client, "/val", new LeaderSelectorListener() {

            private ReentrantLock monitor = new ReentrantLock();
            private Condition cond = monitor.newCondition();

            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {

                try {
                    monitor.lock();
                    System.out.println("LEADER");
                    cond.await();
                } finally {
                    monitor.unlock();
                }

            }

            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {

            }
        });
        leader.start();



        System.out.println("END");
    }

}

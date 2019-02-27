package me.zhaotb.framework.util.concurrent;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.ExecutionException;

/**
 * @author zhaotangbo
 * @date 2019/2/20
 */
public class TestService {

    public static void main(String[] args) throws Exception {


//        ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(2));

        ListeningExecutorService listeningExecutorService = MoreExecutors.newDirectExecutorService();

        ListenableFuture<Integer> submit = listeningExecutorService.submit(() -> {
            System.out.println(3);
            return 1;
        });
        submit.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    Integer integer = submit.get();
                    System.out.println(integer);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }, MoreExecutors.directExecutor());

        System.out.println(2);
        listeningExecutorService.shutdown();
//
//        ExecutorService service = Executors.newFixedThreadPool(2);
//
//
//        AbstractService abstractService = new AbstractService() {
//            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
//            private ReentrantLock lock = new ReentrantLock();
//            @Override
//            protected void doStart() {
//                service.execute(new Runnable() {
//                    int count = 0;
//
//                    @Override
//                    public void run() {
////                        executorService.scheduleWithFixedDelay(() -> {
////                            if (count++ < 3)
////                                System.out.println("RUNNING...");
////                            else
////                                notifyFailed(new RuntimeException("SHI BAI"));
////                        }, 1, 1, TimeUnit.SECONDS);
//                        notifyStarted();
//                        lock.lock();
//                        try{
//                            if (state() != State.TERMINATED)
//                                notifyFailed(new RuntimeException("SHI BAI"));
//                        }finally {
//                            lock.unlock();
//                        }
//
//                    }
//                });
//
//            }
//
//            @Override
//            protected void doStop() {
//                service.execute(() -> {
//                    if (!executorService.isTerminated()) {
//                        try {
//                            executorService.shutdown();
//                            executorService.awaitTermination(3, TimeUnit.SECONDS);
//                            if (!executorService.isTerminated()) {
//                                executorService.shutdownNow();
//                                executorService.awaitTermination(3, TimeUnit.SECONDS);
//                                if (!executorService.isTerminated()) {
//                                    throw new RuntimeException("停止失败...");
//                                }
//                            }
//                        } catch (Exception e) {
//                            notifyFailed(e);
//                            return;
//                        }
//                    }
//                    lock.lock();
//                    try {
//                        if (State.STOPPING == state())
//                            notifyStopped();
//                    }finally {
//                        lock.unlock();
//                    }
//                });
//            }
//        };
//
//        abstractService.addListener(new Service.Listener() {
//            @Override
//            public void failed(Service.State from, Throwable failure) {
//                super.failed(from, failure);
//            }
//
//        }, MoreExecutors.directExecutor());
//
//        abstractService.startAsync();
//
//        abstractService.stopAsync();
//
//        service.shutdownNow();
//
//        System.out.println(abstractService.state());
    }

}

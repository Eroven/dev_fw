package me.zhaotb.app.api;

import com.google.common.util.concurrent.AbstractService;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 程序启动 : (NEW) -> 调doStart(STARTING)  ->  notifyStarted -> RUNNING
 *
 * 程序停止: (RUNNING) -> 调doStop(STOPPING)  -> notifyStopped -> TERMINATED
 *
 * 程序异常: (ANY) -> 程序异常 -> notifyFailed -> FAILED
 */
public abstract class AbstractMicroAppService extends AbstractService implements MicroApplication {

    private final static ExecutorService executor = Executors.newCachedThreadPool(new ThreadFactory() {
        AtomicLong num = new AtomicLong(1);
        public Thread newThread(Runnable r) {
            return new Thread(r,"MicroApp-TaskThread-"+num.getAndIncrement());
        }
    });

    private MicroAppContext microAppContext;

    private Listener listener = new Listener() {
        @Override
        public void running() {
            System.out.println("程序正在运行");
        }

        @Override
        public void terminated(State from) {
            System.out.println("从状态【" + from + "】停止程序");
        }

        @Override
        public void failed(State from, Throwable failure) {
            System.out.println("在【" + from + "】发生异常：" + failure.getMessage());
        }
    };

    protected void doStart() {
        addListener(listener, executor());
        executor().execute(this::run);
    }

    protected void doStop() {
    }

    protected void sleep(long time, TimeUnit unit){
        try {
            unit.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected Executor executor(){
        return executor;
    }

    private void run() {
        notifyStarted();

        try {
            notifyStopped();
        } catch (Exception e) {
            notifyFailed(e);
        }
    }

    @Override
    public MicroAppContext getContext() {
        return microAppContext;
    }

    @Override
    public void setContext(MicroAppContext context) {
        this.microAppContext = context;
    }

}

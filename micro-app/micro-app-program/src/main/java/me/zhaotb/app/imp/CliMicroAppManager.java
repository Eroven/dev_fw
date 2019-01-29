package me.zhaotb.app.imp;

import com.google.common.util.concurrent.Service;
import me.zhaotb.app.api.AppState;
import me.zhaotb.app.api.CliApplication;
import me.zhaotb.app.api.MicroAppContext;
import me.zhaotb.app.api.MicroConf;
import me.zhaotb.framework.util.StringUtil;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class CliMicroAppManager {

    private final static ExecutorService executor = Executors.newCachedThreadPool(new ThreadFactory() {
        AtomicLong num = new AtomicLong(1);
        public Thread newThread(Runnable r) {
            return new Thread(r,"MicroApp-ListenerTread-" + num.getAndIncrement());
        }
    });

    private ConcurrentHashMap<String, ConcurrentHashMap<String, AppState>> history = new ConcurrentHashMap<>();

    /**
     * 所有可执行的程序 appName : app
     */
    private static ConcurrentHashMap<String, Class<? extends CliApplication>> apps = new ConcurrentHashMap<>();

    /**
     * 正在运行的程序 uniqueKey : app
     */
    private ConcurrentHashMap<String, CliApplication> runningApps = new ConcurrentHashMap<>();

    /**
     * @see #runningApps 的key集合
     */
    private ConcurrentHashSet<String> runningAppKeys = new ConcurrentHashSet<>();

    private ReentrantKeyLock<String> lock = new ReentrantKeyLock<>();
    private int timeout = 30;
    private TimeUnit timeoutUnit = TimeUnit.SECONDS;

    /**
     * 通过执行程序名运行程序
     *
     * @param appName 程序名
     * @param args    参数
     * @return 返回调用结果
     */
    public CallResult run(String appName, String[] args) {
        Class<? extends CliApplication> appClass = apps.get(appName);
        if (appClass == null) {
            return fail("未知程序名: " + appName);
        }
        String key = appName + "#" + StringUtil.uuid();
        while (runningAppKeys.contains(key)) {
            key = appName + "#" + StringUtil.uuid();
        }
        CliApplication app = null;
        try {
            app = appClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return fail(appName + " 程序实例化异常: " + e.getMessage());
        }
        return setupApp(args, app, key, appName);
    }

    private CallResult setupApp(String[] args, CliApplication app, String key, String appName) {
        AppState state = new AppState();
        state.setKey(key);
        state.setAppName(appName);

        ConcurrentHashMap<String, AppState> map = history.get(appName);
        if (map == null){
            history.putIfAbsent(appName, new ConcurrentHashMap<>());
            map = history.get(appName);
        }
        map.putIfAbsent(key, state);
        app.setContext(MicroAppContext.instance(key, appName));
        app.addListener(new StateChangeListener(state), executor);
        app.setArgs(args);
        app.startAsync();
        runningAppKeys.add(key);
        runningApps.put(key, app);
        return success("程序已发起启动", key);
    }

    /**
     * 指定key运行程序
     *
     * @param key     唯一标识,全局不能重复
     * @param appName 应用名
     * @param args    参数
     * @return 返回调用结果
     */
    public CallResult run(String key, String appName, String[] args) {
        Class<? extends CliApplication> appClass = apps.get(appName);
        if (appClass == null) {
            return fail("未知程序名: " + appName);
        }
        try {
            if (!lock.tryLock(key, timeout, timeoutUnit)){
                return fail("获取锁超时: " + key);
            }
            if (runningAppKeys.contains(key)) {
                System.out.println();
                return fail("重复key: " + key);
            }
            CliApplication app = appClass.newInstance();
            return setupApp(args, app, key, appName);
        } catch (InterruptedException e) {
            System.err.println();
            e.printStackTrace();
            return fail("获取锁中断, key: " + key);
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return fail(appName + " 程序实例化异常: " + e.getMessage());
        } finally {
            lock.unlock(key);
        }

    }

    /**
     * 判断当前key对应的app是否正在运行
     * @param key 唯一标识
     * @param appName 应用名
     * @return true: 正在运行, false: key 与appName不对应 或者 程序已停止 或者程序未运行
     */
    public boolean isRunningApp(String key, String appName){
        ConcurrentHashMap<String, AppState> map = history.get(appName);
        if (map == null){
            return false;
        }
        AppState state = map.get(key);
        return AppState.State.RUNNING == state.getState();
    }

    public CallResult stop(String key){
        if (!runningAppKeys.contains(key)){
            return fail("找不到key对应的运行中程序: " + key);
        }
        CliApplication app = runningApps.get(key);
        if (app == null){
            runningAppKeys.remove(key);
            return fail("找不到key对应的运行中程序: " + key);
        }
        app.stopAsync();
        return success("程序已发起停止", key);
    }

    private CallResult success(String message, String key){
        return new CallResult(true, message, key);
    }

    private CallResult fail(String message){
        return new CallResult(false, message, null);
    }

    private class StateChangeListener extends Service.Listener {

        private AppState state;

        public StateChangeListener(AppState state) {
            this.state = state;
        }

        @Override
        public void starting() {
            state.setState(AppState.State.STARTING);
            updateAppState(state);
        }

        @Override
        public void running() {
            state.setState(AppState.State.RUNNING);
            updateAppState(state);
        }

        @Override
        public void stopping(Service.State from) {
            state.setState(AppState.State.STOPPING);
            updateAppState(state);
        }

        @Override
        public void terminated(Service.State from) {
            state.setState(AppState.State.TERMINATED);
            state.setRemark(from.toString());
            updateAppState(state);
            runningAppKeys.remove(state.getKey());
            runningApps.remove(state.getKey());
        }

        @Override
        public void failed(Service.State from, Throwable failure) {
            state.setState(AppState.State.FAILED);
            state.setRemark(from.toString() + " ,失败原因: " + failure.toString());
            updateAppState(state);
            runningAppKeys.remove(state.getKey());
            runningApps.remove(state.getKey());
        }

    }

    private void updateAppState(AppState state){
        System.out.println("应用: " + state.getAppName() + " -> " + state.getState());
    }

    public static void register(Class<? extends CliApplication> appClass){
        MicroConf conf = appClass.getDeclaredAnnotation(MicroConf.class);
        String appName;
        if (conf == null){
            appName = StringUtil.classNameToAppName(appClass.getSimpleName());
        }else {
            appName = conf.name();
            if (StringUtil.isBlank(appName)){
                appName = conf.value();
                if (StringUtil.isBlank(appName)){
                    appName = StringUtil.classNameToAppName(appClass.getSimpleName());
                }
            }
        }
        apps.put(appName, appClass);

    }


}

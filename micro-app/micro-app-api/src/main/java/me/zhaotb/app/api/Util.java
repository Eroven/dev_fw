package me.zhaotb.app.api;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhaotangbo
 * @date 2019/3/4
 */
public class Util {

    /**
     * zk目录分割符
     */
    private static final String SEP = "/";

    /**
     * 按zk的规范拼接路劲
     *
     * @param path 路劲数组
     * @return 返回拼接后的路径
     */
    public static String path(String... path) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.length; i++) {
            if (StringUtils.isBlank(path[i])) continue;
            if (i == 0 && !path[i].startsWith(SEP)) {
                sb.append(SEP);
            }
            if (i == path.length - 1) {
                if (path[i].endsWith(SEP)) {
                    sb.append(path[i], 0, path[i].length() - 1);
                } else {
                    sb.append(path[i]);
                }
            } else if (path[i].endsWith(SEP)) {
                sb.append(path[i]);
            } else {
                sb.append(path[i]).append(SEP);
            }

        }
        return sb.toString();
    }

    /**
     * 获取规范zk路径的最后一个路径.
     * 例如： path=/first/second/last   lastPath(path) 返回 last.
     * 特殊的，如果path不包含分隔符，则直接返回
     * @param path zk路径
     * @return 最后一个路径
     */
    public static String lastPath(String path){
        int i = path.lastIndexOf(SEP);
        if (i == path.length() - 1){
            return "";
        }
        if (i == -1){
            return path;
        }else {
            return path.substring(i + 1);
        }

    }

    /**
     * 去掉最后一个路径.
     * 例如： path=/first/second/last   trimLast(path) 返回 /first/second.
     * 特殊的，如果path不包含分隔符，则直接返回
     * @param path 全路径
     * @return 返回结果
     */
    public static String trimLast(String path){
        int i = path.lastIndexOf(SEP);
        if (i == -1){
            return path;
        }else {
            return path.substring(0, i);
        }
    }

    private static ExecutorService cacheService = Executors.newFixedThreadPool(5, new ThreadFactory() {
        private AtomicInteger count = new AtomicInteger();
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "节点监控线程-" + count.incrementAndGet());
        }
    });

    public static ExecutorService getNodeWatchedService(){
        return cacheService;
    }

    private static ExecutorService stationService = Executors.newFixedThreadPool(20, new ThreadFactory() {
        private AtomicInteger count = new AtomicInteger();
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "节点监控线程-" + count.incrementAndGet());
        }
    });

    public static ExecutorService getStationService(){
        return stationService;
    }

    public static void whenShutDown(Thread task){
        Runtime.getRuntime().addShutdownHook(task);
    }



}
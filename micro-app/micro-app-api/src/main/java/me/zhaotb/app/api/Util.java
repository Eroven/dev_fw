package me.zhaotb.app.api;

import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

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
        if (sb.toString().endsWith(SEP)){
            sb.deleteCharAt(sb.length() - 1);
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

    private static ExecutorService followStationService = Executors.newFixedThreadPool(20, new ThreadFactory() {
        private AtomicInteger count = new AtomicInteger();
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "follow基站线程-" + count.incrementAndGet());
        }
    });

    private static ExecutorService leaderStationService = Executors.newCachedThreadPool(new ThreadFactory() {
        private AtomicLong count = new AtomicLong();
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "leader基站线程-" + count.incrementAndGet());
        }
    });

    public static ExecutorService getFollowStationService(){
        return followStationService;
    }

    public static ExecutorService getLeaderStationService(){
        return leaderStationService;
    }

    /**
     * 高节序byte转int
     * @param bytes 数据源
     * @param pos 开始位置，包含
     * @return 返回结果，如果数据源长度不够，返回 {@link Integer#MIN_VALUE}
     */
    public static int getIntB(byte[] bytes, int pos){
        if (bytes.length - pos < 4)
            return Integer.MIN_VALUE;
        return makeInt(bytes[pos], bytes[pos + 1], bytes[pos + 2], bytes[pos + 3]);
    }

    /**
     * 高接续byte转long
     * @param bytes 数据源
     * @param pos 开始位置，包含
     * @return 返回结果，如果数据源长度不够，返回 {@link Long#MIN_VALUE}
     */
    public static long getLongB(byte[] bytes, int pos){
        if (bytes.length - pos < 8){
            return Long.MIN_VALUE;
        }
        return makeLong(bytes[pos], bytes[pos + 1], bytes[pos + 2], bytes[pos + 3],
                bytes[pos + 4], bytes[pos + 5], bytes[pos + 6], bytes[pos + 7]);
    }

    /**
     * 字节转int
     * @param b0 第一个字节，第一位为结果的符号位
     * @param b1 第二个字节
     * @param b2 第三个字节
     * @param b3 第四个字节
     * @return 返回int
     */
    private static int makeInt(byte b0, byte b1, byte b2, byte b3){
        return  ((b0       ) << 24) |
                ((b1 & 0xFF) << 16) |
                ((b2 & 0xFF) << 8 ) |
                ((b3 & 0xFF)      );
    }

    /**
     * 字节转long
     * @param b0 第一个字节，第一位为结果的符号位
     * @param b1 第二个字节
     * @param b2 第三个字节
     * @param b3 第四个字节
     * @param b4 第五个字节
     * @param b5 第六字节
     * @param b6 第七个字节
     * @param b7 第八个字节
     * @return 返回long
     */
    private static long makeLong(byte b0, byte b1, byte b2, byte b3,
                                 byte b4, byte b5, byte b6, byte b7) {
        return  (((long)b0       ) << 56) |
                (((long)b1 & 0xFF) << 48) |
                (((long)b2 & 0xFF) << 40 ) |
                (((long)b3 & 0xFF) << 32  ) |
                (((long)b4 & 0xFF) << 24) |
                (((long)b5 & 0xFF) << 16) |
                (((long)b6 & 0xFF) << 8 ) |
                (((long)b7 & 0xFF)      );
    }

    /**
     *
     * @return 返回本机ip
     */
    public static String getLocalhostIp(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "127.0.0.1";
        }
    }

}

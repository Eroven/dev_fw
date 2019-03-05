package me.zhaotb.app.api;

/**
 * 程序注册接口
 * @author zhaotangbo
 * @date 2019/3/5
 */
public interface Registerable {

    /**
     *
     * @return 程序名
     */
    String appName();

    /**
     *
     * @return 类型
     */
    RuntimeType type();

    /**
     *
     * @return 占用内存，单位 KB
     */
    long memery();

    /**
     * 在该系统下执行该命令就能启动程序
     * @return 启动命令
     */
    String startCMD();

    enum RuntimeType {
        SINGLETON("单例"),
        ROUND_ROBIN("轮询"),
        COPY("复制"),
        RANDOM("随机"),
        FREE_FIRST("空闲优先");

        private String desc;
        RuntimeType(String desc) {
            this.desc = desc;
        }
    }

}

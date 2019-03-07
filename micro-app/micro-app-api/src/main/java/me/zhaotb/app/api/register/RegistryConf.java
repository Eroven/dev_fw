package me.zhaotb.app.api.register;

import lombok.Getter;
import lombok.Setter;

/**
 * 注册表配置信息，用于初始化注册器
 * @see Register
 * @author zhaotangbo
 * @date 2019/3/1
 */
@Getter
@Setter
public class RegistryConf {

    /**
     * zk连接串
     */
    private String connectStr;

    /**
     * 根目录
     */
    private String root = "/micro-app/scheduler";

    /**
     * 接收检测心跳的端口;如果是leader则为接收命令的端口
     */
    private int tickPort = 3015;

    /**
     * 控制端口；数据交流端口
     */
    private int ctrlPort = 3020;

    /**
     * 注册时绑定的地址，默认为本机ip
     */
    private String address;

    /**
     * 程序会使用最小和最大值之间的端口，某个被占则用下一个
     */
    private int minPort = 6000;

    private int maxPort = 12000;

    /**
     * 通用重试次数
     */
    private int retryTimes = 3;

    /**
     * 重试间隔，单位 ms
     */
    private long retryInterval = 1000;

    /**
     * 每次心跳检测间隔， 单位： 毫秒
     */
    private long tickTime = 3000;

    /**
     * 心跳无响应超过该次数，判定为失联
     */
    private int tickLimit = 3;

    private long tickTimeout = 3000;

}

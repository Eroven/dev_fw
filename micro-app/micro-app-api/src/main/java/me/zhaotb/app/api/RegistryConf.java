package me.zhaotb.app.api;

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
     * 检测心跳的端口
     */
    private int tickPort = 3015;

    /**
     * 控制端口；数据交流端口
     */
    private int ctrlPort = 3020;

    /**
     * 程序会使用最小和最大值之间的端口，某个被占则用下一个
     */
    private int minPort = 6000;

    private int maxPort = 12000;

    /**
     * 每次心跳检测间隔， 单位： 毫秒
     */
    private int tickTime = 2000;

    /**
     * 心跳无响应超过该次数，判定为失联
     */
    private int tickLimit = 3;

}

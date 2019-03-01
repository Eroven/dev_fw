package me.zhaotb.app.scheduler;

import me.zhaotb.app.api.AppInfo;
import me.zhaotb.app.api.AppState;

/**
 * 程序调度器，负责启动和关闭程序
 * @author zhaotangbo
 * @date 2019/3/1
 */
public interface AppScheduler {

    /**
     * 直接启动程序
     * @param appInfo 程序信息
     * @return 返回程序状态
     */
    AppState setup(AppInfo appInfo);

    /**
     * 直接关闭程序
     * @param appInfo 程序信息
     * @return 返回程序状态
     */
    AppState shutdown(AppInfo appInfo);

}

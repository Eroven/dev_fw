package me.zhaotb.app.api;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

/**
 * 命令行接口应用程序
 */
public interface CliApplication extends MicroApplication {

    /**
     * 获取指令选项，用于设定参数.注：该方法会被都次调用
     * @return 选项集合
     */
    Options getOptions();

    /**
     * 执行任务
     * @param cmd 通过解析 {@link #getOptions()} 得到的结果
     * @param args 原始参数
     */
    void doTask(CommandLine cmd, String[] args);

    void setArgs(String[] args);



}

package me.zhaotb.app.program.hello;

import me.zhaotb.app.api.Registerable;

/**
 * @author zhaotangbo
 * @date 2019/3/5
 */
public class RegisterHelloWorld implements Registerable {
    @Override
    public String appName() {
        return "hello";
    }

    @Override
    public RuntimeType type() {
        return RuntimeType.ROUND_ROBIN;
    }

    @Override
    public long memery() {
        return 1;
    }

    @Override
    public String startCMD() {
        return "java ";
    }
}

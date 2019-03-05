package me.zhaotb.app.scheduler;

import me.zhaotb.app.api.register.Register;
import me.zhaotb.app.api.register.RegistryConf;

/**
 * @author zhaotangbo
 * @date 2019/3/4
 */
public class SchedulerMain {

    private static RegistryConf getConf(){
        RegistryConf conf = new RegistryConf();
        conf.setConnectStr("localhost:2181");
        conf.setTickPort(3000);
        conf.setCtrlPort(3010);
        return conf;
    }

    public static void main(String[] args) {
        RegistryConf conf = getConf();
        Register register = new Register(conf);
        register.init();



    }
}

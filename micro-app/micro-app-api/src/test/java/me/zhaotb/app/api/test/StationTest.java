package me.zhaotb.app.api.test;

import lombok.extern.slf4j.Slf4j;
import me.zhaotb.app.api.register.Register;
import me.zhaotb.app.api.register.RegistryConf;
import me.zhaotb.app.api.station.AppStation;
import org.junit.Test;

/**
 * @author zhaotangbo
 * @date 2019/3/6
 */
@Slf4j
public class StationTest {

    private RegistryConf getConf(){
        RegistryConf conf = new RegistryConf();
        conf.setConnectStr("localhost:2181");
        return conf;
    }

    @Test
    public void testFollowStation() throws Exception {

        log.info("日志");
        RegistryConf conf = getConf();
        conf.setCtrlPort(3030);
        conf.setTickPort(3020);
        Register register = new Register(conf);
        register.init();



        System.out.println("Follow station started!");


    }

    @Test
    public void testLeaderStation() throws Exception {
        RegistryConf conf = getConf();
        conf.setCtrlPort(3060);
        conf.setTickPort(3050);
        Register register = new Register(conf);
//        register.init();
        AppStation appStation = AppStation.newLeaderStation()
                .conf(conf).register(register)
                .build();
        appStation.start();

        System.out.println("Leader station started!");

        appStation.stop();
    }

}

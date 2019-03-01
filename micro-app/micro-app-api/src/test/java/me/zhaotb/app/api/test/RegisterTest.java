package me.zhaotb.app.api.test;

import me.zhaotb.app.api.Register;
import me.zhaotb.app.api.RegistryConf;
import org.junit.Test;

/**
 * @author zhaotangbo
 * @date 2019/3/1
 */
public class RegisterTest {

    RegistryConf getConf(){
        RegistryConf conf = new RegistryConf();
        conf.setConnectStr("localhost:2181");
        conf.setTickPort(3000);
        conf.setCtrlPort(3010);
        return conf;
    }

    @Test
    public void testRegister(){
        RegistryConf conf = getConf();
        Register register = new Register(conf);
        register.init();

        try {
            register.admin();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}

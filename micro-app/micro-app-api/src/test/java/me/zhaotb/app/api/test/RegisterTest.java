package me.zhaotb.app.api.test;

import me.zhaotb.app.api.station.Address;
import me.zhaotb.app.api.register.Register;
import me.zhaotb.app.api.register.RegistryConf;
import org.junit.Test;

/**
 * @author zhaotangbo
 * @date 2019/3/1
 */
public class RegisterTest {

    private RegistryConf getConf(){
        RegistryConf conf = new RegistryConf();
        conf.setConnectStr("localhost:2181");
        conf.setTickPort(3020);
        conf.setCtrlPort(3030);
        return conf;
    }

    @Test
    public void testRegister(){
        RegistryConf conf = getConf();
        Register register = new Register(conf);
        register.init();

        try {
            Address admin = register.admin();
            System.out.println(admin);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Test
    public void testRegister2(){
        RegistryConf conf = getConf();
        conf.setTickPort(3040);
        conf.setCtrlPort(3050);
        Register register = new Register(conf);
        register.init();

        try {
            Address admin = register.admin();
            System.out.println(admin);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}

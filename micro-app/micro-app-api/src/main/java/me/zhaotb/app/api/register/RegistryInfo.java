package me.zhaotb.app.api.register;

import lombok.Getter;
import me.zhaotb.app.api.Address;

import java.util.Hashtable;

/**
 * 注册表信息
 * @author zhaotangbo
 * @date 2019/3/1
 */
@Getter
public class RegistryInfo {

    private String appName;

    private Hashtable<Address, Address> addressTable;

    RegistryInfo(String appName) {
        this.appName = appName;
        addressTable = new Hashtable<>();
    }

    void regist(String tickIp, int tickPort, String ctrlIp, int ctrlPort){
        Address tick = new Address(tickIp, tickPort);
        Address ctrl =new Address(ctrlIp, ctrlPort);
        addressTable.put(tick, ctrl);
    }


}

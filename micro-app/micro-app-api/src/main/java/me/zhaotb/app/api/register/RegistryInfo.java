package me.zhaotb.app.api.register;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.zhaotb.app.api.station.Address;

/**
 * 注册表信息
 * @author zhaotangbo
 * @date 2019/3/1
 */
@Getter
@Setter
@NoArgsConstructor
public class RegistryInfo {

    private String appName;

    private Address tickAddr;

    private Address ctrlAddr;

    public RegistryInfo(String appName) {
        this.appName = appName;
    }

}

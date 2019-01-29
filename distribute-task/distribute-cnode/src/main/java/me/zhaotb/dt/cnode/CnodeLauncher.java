package me.zhaotb.dt.cnode;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import me.zhaotb.dt.sdk.utils.SpringHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = "me.zhaotb.dt")
@EnableDubboConfiguration
public class CnodeLauncher {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(CnodeLauncher.class, args);
        SpringHelper.setContext(applicationContext);

        ServiceRegister register = applicationContext.getBean(ServiceRegister.class);
        register.doRegister();

        synchronized (CnodeLauncher.class){
            System.out.println("CNODE 启动成功!");
            CnodeLauncher.class.wait();
        }
    }
}

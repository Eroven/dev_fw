package me.zhaotbdt.admin;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import me.zhaotb.dt.sdk.api.Task;
import me.zhaotb.dt.sdk.utils.SpringHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;

@SpringBootApplication
@EnableDubboConfiguration
public class AdminLauncher {


    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(AdminLauncher.class, args);
        SpringHelper.setContext(context);
        AdminTaskList bean = context.getBean(AdminTaskList.class);
        Map<String, Object> tasks = context.getBeansWithAnnotation(Task.class);
        bean.translateTaskManager(tasks);

        synchronized (AdminLauncher.class){
            System.out.println("ADMIN 节点启动成功! ");
            AdminLauncher.class.wait();
        }
    }
}

package me.zhaotb.admin.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

@SpringBootApplication(scanBasePackages = {"me.zhaotb"}, exclude = {DataSourceAutoConfiguration.class})
public class Application {

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        if (Env.getHome() == null) {
            File home = new File("");
            Env.setHome(home.getAbsolutePath());
        }
        System.out.println("HOME = " + Env.getHome());
        System.out.println("CONFIG_PATH = " + Env.getConfigPath());
        System.out.println("LOG_PATH = " + Env.getLogPath());
        File file = new File(Env.getLogPath());
        if (!file.isDirectory()) {
            if (!file.mkdirs()) {
                System.err.println("log目录创建失败！ ： " + file.getAbsolutePath());
                System.exit(-1);
            }
        }
        ConfigurableEnvironment environment = context.getEnvironment();
        String pid = environment.getProperty("PID");
        System.out.println("PID : " + pid);
        PrintWriter writer = new PrintWriter(new File(file, "PID"), Env.getCharset());
        writer.print(pid);
        writer.close();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            File logPath = new File(Env.getLogPath());
            if (!logPath.isDirectory()) return;
            File pidFile = new File(logPath, "PID");
            if (pidFile.isFile()) {
                if (!pidFile.delete()) {
                    System.err.println("删除PID文件失败");
                }
            }
        }));
    }

}

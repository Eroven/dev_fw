package me.zhaotb.dt.register;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.locks.ReentrantLock;

@SpringBootApplication
@RestController
@RequestMapping("register")
public class RegistrationCenter {

    private ReentrantLock lock = new ReentrantLock();
    private ConcurrentHashMap<String, Set<String>> infoMap = new ConcurrentHashMap<>();

    @RequestMapping("server")
    public String registerInfo(@RequestParam(name="sn") String serverName,
                               @RequestParam("port") int port,
                               HttpServletRequest request) {
        String remoteHost = request.getRemoteHost();
        Set<String> set = infoMap.get(serverName);
        if (set == null) {
            lock.lock();
            try {
                set = new ConcurrentSkipListSet<>();
                infoMap.put(serverName, set);
            }finally {
                lock.unlock();
            }
        }
        set.add(remoteHost+":"+port);
        return "注册成功";
    }

    @RequestMapping("find")
    public Set<String> lookup(@RequestParam("sn") String serverName) {
        return infoMap.get(serverName);
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext application = SpringApplication.run(RegistrationCenter.class, args);
    }
}

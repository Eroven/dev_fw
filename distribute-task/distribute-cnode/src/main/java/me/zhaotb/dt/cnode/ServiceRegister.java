package me.zhaotb.dt.cnode;

import me.zhaotb.dt.cnode.api.CalService;
import me.zhaotb.dt.cnode.api.report.MonthlyAcctItemService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;

@Component
public class ServiceRegister {

    @Value("${register.domain}")
    private String registerDomain;

    @Value("${register.port}")
    private int registerPort;

    @Value("${spring.dubbo.protocol.port}")
    private int port;

    private Set<Class<? extends CalService>> services = new HashSet<>();

    public ServiceRegister() {
        services.add(MonthlyAcctItemService.class);
    }

    public void doRegister(){
        String str = "http://" + registerDomain + ":" + registerPort + "/register/server?sn=$&port=" + port;
        for (Class<? extends CalService> service : services) {
            try {
                URL url = new URL(str.replaceFirst("\\$", service.getName()));
                URLConnection conn = url.openConnection();
                conn.connect();
                InputStream inputStream = conn.getInputStream();
                byte[] buffer = new byte[1024];
                int read = inputStream.read(buffer);
                System.out.println(new String(buffer, 0, read));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

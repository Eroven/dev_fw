package me.zhaotb.oauth.client;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class DomainUtils implements ApplicationContextAware {

    private static final String KEY_DOMAIN = "server.domain";

    private static ApplicationContext context;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static String domain(){
        Environment env = context.getEnvironment();
        String domain = env.getProperty(KEY_DOMAIN);
        return domain;
    }

    public static int port(){
        Environment env = context.getEnvironment();
        String port = env.getProperty("server.port");
        return Integer.parseInt(port);
    }

    public static String domainContext(){
        Environment env = context.getEnvironment();
        return env.getProperty(KEY_DOMAIN) + ":" + env.getProperty("server.port");
    }
}

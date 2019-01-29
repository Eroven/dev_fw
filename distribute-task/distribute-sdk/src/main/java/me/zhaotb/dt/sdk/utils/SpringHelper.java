package me.zhaotb.dt.sdk.utils;

import org.springframework.context.ConfigurableApplicationContext;

public class SpringHelper {

    public static ConfigurableApplicationContext context;

    public static ConfigurableApplicationContext getContext() {
        return context;
    }

    public static void setContext(ConfigurableApplicationContext context) {
        SpringHelper.context = context;
    }

    public static <T> T getBean(Class<T> type) {
        return context.getBean(type);
    }

    public static Object getBean(String beanName){
        return context.getBean(beanName);
    }
}

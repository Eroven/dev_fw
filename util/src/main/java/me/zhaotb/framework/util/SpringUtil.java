package me.zhaotb.framework.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static ApplicationContext getContext(){
        return context;
    }

    /**
     * 指定类获取bean
     * @param clazz 类名
     * @param <T> 泛型
     * @return bean对象引用
     */
    public static <T> T getBean(Class<T> clazz){
        return context.getBean(clazz);
    }

    /**
     * 通过 beanName 获取对象
     * @param beanName 定义的 beanName
     * @return bean对象引用
     */
    public static Object getBean(String beanName){
        return  context.getBean(beanName);
    }

    /**
     *   指定类获取bean
     * @param clazz 类名
     * @param args 构造参数
     * @param <T> 泛型
     * @return bean对象引用
     */
    public static <T> T getBean(Class<T> clazz, Object...args){
        return context.getBean(clazz, args);
    }

    /**
     * 通过 beanName 获取对象
     * @param beanName 定义的 beanName
     * @param args 构造参数
     * @return bean对象引用
     */
    public static Object getBean(String beanName, Object... args){
        return context.getBean(beanName,args);
    }


}

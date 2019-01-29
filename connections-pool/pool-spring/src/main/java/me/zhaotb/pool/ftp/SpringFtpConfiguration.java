package me.zhaotb.pool.ftp;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.SpringProperties;
import org.springframework.core.env.Environment;

@Configuration
public class SpringFtpConfiguration {

    @Bean(name = "myftpInfo")
    public FtpInfo myFtpInfo(Environment env){
        return FtpInfoFactory.factoryByProperties(env, "spring.ftp.myftp");
    }

    @Bean("myftpFacotry")
    public PooledFtpFactory pooledFtpFactory(@Qualifier("myftpInfo") FtpInfo ftpInfo){
        return new PooledFtpFactory(ftpInfo);
    }

    @Bean("myftpConfig")
    public GenericObjectPoolConfig config(){
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxIdle(5);
        config.setMinIdle(0);
        config.setMaxTotal(20);
        config.setMaxWaitMillis(10000);//最大等待时间(毫秒),超过该时间,则拿不到对象且抛出异常
//        config.setMinEvictableIdleTimeMillis(60000);//最小强制移除闲置对象时间(毫秒)
        config.setSoftMinEvictableIdleTimeMillis(6000);//最小移除闲置对象时间(毫秒),会判断最小闲置数
        config.setNumTestsPerEvictionRun(1); //检测闲置对象的线程数,如果为负数则取绝对值
        config.setTimeBetweenEvictionRunsMillis(10000);//检测闲置对象循环时间间隔(毫秒)
        config.setTestOnCreate(false);//创建是校验
        config.setTestOnBorrow(true);//使用时校验
        config.setTestOnReturn(false);//归还是校验
        config.setBlockWhenExhausted(true);//当无空闲对象是,是否阻塞
        return config;
    }

    @Bean(name = "myftpTemplate", initMethod = "init", destroyMethod = "shutdown")
    public FtpTemplate myftpTemplate(@Qualifier("myftpFacotry") PooledFtpFactory factory,
                                     @Qualifier("myftpConfig") GenericObjectPoolConfig config){
        return new FtpTemplate(factory, config);
    }

    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Bean(initMethod = "init", destroyMethod = "close")
    public Data data(){
        return new Data();
    }

}

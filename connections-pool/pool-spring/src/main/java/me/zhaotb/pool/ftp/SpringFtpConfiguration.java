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

        config.setMaxTotal(5);//连接池最多创建这个值的对象个数
        config.setMaxIdle(4);//连接池最多保持这个值的闲置对象个数。当归还对象时，闲置对象超过超过该值，则返还的对象直接销毁
        config.setMinIdle(3);//连接池最小保持这个值的闲置对象个数。闲置对象不超过该值时，evictor线程反过来会异步创建足够的对象。由于是异步的，会出现与使用者冲突的现象，这里建议设置为0或者将闲置检测线程关掉

        config.setBlockWhenExhausted(true);//当无空闲对象是,是否阻塞。不阻塞，拿不到对象时直接抛异常
        config.setMaxWaitMillis(10000);//如果阻塞，最大等待时间(毫秒),超过该时间,则拿不到对象且抛出异常

//        config.setTimeBetweenEvictionRunsMillis(10000);//开启闲置对象检测线程evictor，并设置检测闲置对象循环时间间隔(毫秒)
        config.setNumTestsPerEvictionRun(2); //每次检测闲置对象的数量
        config.setSoftMinEvictableIdleTimeMillis(30000);//将当前闲置对象闲置时间大于该值的对象取出来（取出来后要保证大于最小闲置数量）回收掉。最小移除闲置对象时间(毫秒),会判断最小闲置数
        config.setMinEvictableIdleTimeMillis(60000);//最小强制移除闲置对象时间(毫秒)，不管最小闲置数。场景：比如正常闲置30分钟，这里设置1天，那么都闲置了1天，就直接强制回收了。强制回收了后，又会创建对象，保证最小闲置对象数量。。。感觉就跟很鸡肋
        config.setTestWhileIdle(false);//闲置对象检测线程每次检测的对象是否需要进行校验，主要执行3个方法： activateObject validateObject passivateObject

        config.setTestOnCreate(false);//创建时校验
        config.setTestOnBorrow(true);//使用时校验
        config.setTestOnReturn(false);//归还是校验

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

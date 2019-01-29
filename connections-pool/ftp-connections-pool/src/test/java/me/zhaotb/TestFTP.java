package me.zhaotb;

import me.zhaotb.pool.ftp.FtpInfo;
import me.zhaotb.pool.ftp.FtpTemplate;
import me.zhaotb.pool.ftp.PooledFtpFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;

public class TestFTP {

    @Test
    public void testTemplate(){
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxIdle(5);
        config.setMinIdle(0);
        config.setMaxTotal(20);
        config.setMaxWaitMillis(10000);//最大等待时间(毫秒),超过该时间,则拿不到对象且抛出异常
        config.setSoftMinEvictableIdleTimeMillis(6000);//最小移除闲置对象时间(毫秒),会判断最小闲置数
        config.setNumTestsPerEvictionRun(1); //检测闲置对象的线程数,如果为负数则取绝对值
        config.setTimeBetweenEvictionRunsMillis(10000);//检测闲置对象循环时间间隔(毫秒)
        config.setTestOnCreate(false);//创建是校验
        config.setTestOnBorrow(true);//使用时校验
        config.setTestOnReturn(false);//归还是校验
        config.setBlockWhenExhausted(true);//当无空闲对象是,是否阻塞
        FtpInfo info = new FtpInfo();
        info.setIp("192.168.179.128");
        info.setPort(21);
        info.setUser("hadoop");
        info.setPassword("123456");
        PooledFtpFactory factory = new PooledFtpFactory(info);
        FtpTemplate template = new FtpTemplate(factory, config);



        try {
            boolean suc = template.retrieveFile("b.txt", "F:\\FTP\\test\\b.txt");

            System.out.println(suc);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

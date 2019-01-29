package me.zhaotb.pool.ftp;


import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.pool2.KeyedObjectPool;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class FtpMain {

    public static void main(String[] args) throws Exception {

        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxIdle(5);
        config.setMinIdle(0);
        config.setMaxTotal(20);
        config.setMaxWaitMillis(10000);//最大等待时间(毫秒),超过该时间,则拿不到对象且抛出异常
//        config.setMinEvictableIdleTimeMillis(60000);//最小强制移除闲置对象时间(毫秒)
        config.setSoftMinEvictableIdleTimeMillis(6000);//最小移除闲置对象时间(毫秒),会判断最小闲置数
        config.setNumTestsPerEvictionRun(1); //检测闲置对象的线程数,如果为负数则取绝对值
        config.setTimeBetweenEvictionRunsMillis(10000);//检测闲置对象循环时间间隔(毫秒)
        config.setTestOnCreate(false);//创建时校验
        config.setTestOnBorrow(true);//使用时校验
        config.setTestOnReturn(false);//归还时校验
        config.setBlockWhenExhausted(true);//当无空闲对象是,是否阻塞

        FtpInfo info = new FtpInfo();
        info.setIp("192.168.179.128");
        info.setPort(21);
        info.setUser("hadoop");
        info.setPassword("123456");

        KeyedPooledObjectFactory<FtpInfo, FTPClient> factory = new KeyedPoolFtpFactory();
        GenericKeyedObjectPoolConfig keyedConfig = new GenericKeyedObjectPoolConfig();
        GenericKeyedObjectPool<FtpInfo, FTPClient> keyedPool = new GenericKeyedObjectPool<>(factory, keyedConfig);
        FTPClient client = keyedPool.borrowObject(info);
        getA(client);

        GenericObjectPool<FTPClient> pool = new GenericObjectPool<>(new PooledFtpFactory(info),config);
        pool.preparePool();

        FTPClient ftpA = pool.borrowObject();
        ftpA.enterLocalActiveMode();
        FTPFile[] files = ftpA.listFiles();
        System.out.println(Arrays.toString(files));

        getA(ftpA);

        ftpA = pool.borrowObject();
        ftpA.enterLocalPassiveMode();
        FTPFile[] ftpFiles = ftpA.listFiles();
        System.out.println(Arrays.toString(ftpFiles));

        getA(ftpA);

    }

    private static void getA(FTPClient ftpA) throws IOException {
        InputStream in = ftpA.retrieveFileStream("a.txt");
        InputStreamReader inRe = new InputStreamReader(in);
        BufferedReader reader = new BufferedReader(inRe);
        String s;
        while (( s = reader.readLine()) != null){
            System.out.println(s);
        }
    }
}

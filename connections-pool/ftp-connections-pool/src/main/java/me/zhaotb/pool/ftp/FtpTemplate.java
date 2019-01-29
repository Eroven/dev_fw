package me.zhaotb.pool.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.locks.ReentrantLock;

public class FtpTemplate implements FtpOperation {

    private GenericObjectPool<FTPClient> pool;

    private PooledFtpFactory factory;

    private GenericObjectPoolConfig config;

    private ReentrantLock lock = new ReentrantLock();

    private int mode = LOCAL_PASSITIVE_MODE;

    public FtpTemplate(PooledFtpFactory factory) {
        this(factory, new GenericObjectPoolConfig());
    }

    public FtpTemplate(PooledFtpFactory factory, GenericObjectPoolConfig config) {
        this.factory = factory;
        this.config = config;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void init(){
        pool = new GenericObjectPool<>(factory, config);
    }

    @Override
    public InputStream retrieveFile(String remoteFile) throws Exception {
        checkPool();
        FTPClient ftp = pool.borrowObject();
        try {
            switchMode(ftp);
            return ftp.retrieveFileStream(remoteFile);
        }finally {
            pool.returnObject(ftp);
        }
    }

    private void checkPool(){
        if (pool == null){
            lock.lock();
            try {
                if (pool == null) {
                    init();
                }
            }finally {
                lock.unlock();
            }
        }
        if (pool.isClosed()){
            throw new IllegalStateException("FTP连接池已经被关闭,不允许使用!");
        }
    }

    @Override
    public boolean retrieveFile(String remoteFile, String localFile) throws Exception {
        checkPool();
        FTPClient ftp = pool.borrowObject();
        try{
            switchMode(ftp);
            File file = new File(localFile);
            File parentFile = file.getParentFile();
            if (!parentFile.exists() && !parentFile.mkdirs()){
                throw new FileNotFoundException("目录不存在 : " + parentFile.getAbsolutePath());
            }
            return ftp.retrieveFile(remoteFile, new FileOutputStream(file));
        }finally {
            pool.returnObject(ftp);
        }
    }

    @Override
    public boolean storeFile(String localFile, String remoteFile) throws Exception {
        checkPool();
        FTPClient ftp = pool.borrowObject();
        switchMode(ftp);
        try (FileInputStream inputStream = new FileInputStream(localFile)) {
            return ftp.storeFile(remoteFile, inputStream);
        } finally {
            pool.returnObject(ftp);
        }
    }

    @Override
    public boolean storeFile(InputStream localInputStream, String remoteFile) throws Exception {
        checkPool();
        FTPClient ftp = pool.borrowObject();
        try{
            return ftp.storeFile(remoteFile, localInputStream);
        }finally {
            pool.returnObject(ftp);
        }
    }

    @Override
    public GenericObjectPool<FTPClient> getFTPPool() {
        return pool;
    }

    private void switchMode(FTPClient ftp) {
        switch (mode) {
            case LOCAL_ACTIVE_MODE:
                ftp.enterLocalActiveMode();
                break;
            case LOCAL_PASSITIVE_MODE:
                ftp.enterLocalPassiveMode();
                break;
        }
    }

    public void shutdown(){
        System.out.println("shutdown");
        pool.close();
    }
}

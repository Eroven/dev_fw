package me.zhaotb.pool.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.io.InputStream;

public interface FtpOperation {

    /**
     * 本地主动模式
     */
    int LOCAL_ACTIVE_MODE = 1;
    /**
     * 本地被动模式
     */
    int LOCAL_PASSITIVE_MODE = 2;

    InputStream retrieveFile(String remoteFile) throws Exception;

    boolean retrieveFile(String remoteFile, String localFile) throws Exception;

    boolean storeFile(String localFile, String remoteFile) throws Exception;

    boolean storeFile(InputStream localInputStream, String remoteFile) throws Exception;

    GenericObjectPool<FTPClient> getFTPPool();
}

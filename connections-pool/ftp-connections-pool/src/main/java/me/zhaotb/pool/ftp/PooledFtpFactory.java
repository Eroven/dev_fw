package me.zhaotb.pool.ftp;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PooledFtpFactory implements PooledObjectFactory<FTPClient> {
    private final Logger logger = LoggerFactory.getLogger(PooledFtpFactory.class);
    private FtpInfo ftpInfo;

    public PooledFtpFactory(FtpInfo ftpInfo) {
        this.ftpInfo = ftpInfo;
    }

    @Override
    public PooledObject<FTPClient> makeObject() throws Exception {
        FTPClient ftp = new FTPClient();
        logger.info("创建对象：{}", ftp);
        ftp.connect(ftpInfo.getIp(), ftpInfo.getPort());
        if (!ftp.login(ftpInfo.getUser(), ftpInfo.getPassword())) {
            throw new IllegalArgumentException("Authentication failed : " + ftpInfo.toString());
        }
        ftp.setControlEncoding(ftpInfo.getCtlEncode());
        ftp.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
        if (ftpInfo.isActiveMode()) {
            ftp.enterLocalActiveMode();
        } else {
            ftp.enterLocalPassiveMode();
        }
        return new DefaultPooledObject<>(ftp);
    }

    @Override
    public void destroyObject(PooledObject<FTPClient> pooledObject) {
        try {
            logger.info("回收连接对象：{}", pooledObject.getObject());
            FTPClient ftp = pooledObject.getObject();
            ftp.logout();
            ftp.disconnect();
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    @Override
    public void activateObject(PooledObject<FTPClient> pooledObject) throws Exception {
        logger.info("激活连接对象: {}", pooledObject.getObject() );
    }

    @Override
    public void passivateObject(PooledObject<FTPClient> pooledObject) throws Exception {
        logger.info("归还连接对象: {}", pooledObject.getObject() );
    }

    @Override
    public boolean validateObject(PooledObject<FTPClient> pooledObject) {
        FTPClient ftp = pooledObject.getObject();
        logger.info("校验连接对象：{}", pooledObject.getObject());
        try {
            return FTPReply.isPositiveCompletion(ftp.stat());
        } catch (IOException e) {
            return false;
        }
    }
}

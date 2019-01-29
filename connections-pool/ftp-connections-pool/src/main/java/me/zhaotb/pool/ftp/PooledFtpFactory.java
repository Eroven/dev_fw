package me.zhaotb.pool.ftp;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.io.IOException;

public class PooledFtpFactory implements PooledObjectFactory<FTPClient> {

    private FtpInfo ftpInfo;

    public PooledFtpFactory(FtpInfo ftpInfo) {
        this.ftpInfo = ftpInfo;
    }

    @Override
    public PooledObject<FTPClient> makeObject() throws Exception {
        FTPClient ftp = new FTPClient();
        ftp.connect(ftpInfo.getIp(), ftpInfo.getPort());
        if (!ftp.login(ftpInfo.getUser(), ftpInfo.getPassword())){
            throw new IllegalArgumentException("Authentication failed : " + ftpInfo.toString());
        }
        ftp.setControlEncoding(ftpInfo.getCtlEncode());
        ftp.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
        return new DefaultPooledObject<>(ftp);
    }

    @Override
    public void destroyObject(PooledObject<FTPClient> pooledObject)  {
    }

    @Override
    public void activateObject(PooledObject<FTPClient> pooledObject) throws Exception {
        FTPClient ftp = pooledObject.getObject();
        if (ftpInfo.isActiveMode()){
            ftp.enterLocalActiveMode();
        }else {
            ftp.enterLocalPassiveMode();
        }
    }

    @Override
    public void passivateObject(PooledObject<FTPClient> pooledObject) throws Exception {
        FTPClient ftp = pooledObject.getObject();
        ftp.logout();
        ftp.disconnect();
    }

    @Override
    public boolean validateObject(PooledObject<FTPClient> pooledObject) {
        FTPClient ftp = pooledObject.getObject();
        try {
            return FTPReply.isPositiveCompletion(ftp.stat());
        } catch (IOException e) {
            return false;
        }
    }
}

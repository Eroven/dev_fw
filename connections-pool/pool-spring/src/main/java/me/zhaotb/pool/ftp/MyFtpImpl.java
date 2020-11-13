package me.zhaotb.pool.ftp;

import com.sun.javafx.binding.StringFormatter;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Repository
public class MyFtpImpl implements MyFtp {

    private final Logger logger = LoggerFactory.getLogger(MyFtpImpl.class);

    private FtpTemplate ftpTemplate;

    public MyFtpImpl(FtpTemplate ftpTemplate) {
        this.ftpTemplate = ftpTemplate;
    }

    @Override
    public InputStream retrieveFile(String remoteFile) throws Exception {
        return ftpTemplate.retrieveFile(remoteFile);
    }

    @Override
    public boolean retrieveFile(String remoteFile, String localFile) throws Exception {
        return ftpTemplate.retrieveFile(remoteFile, localFile);
    }

    @Override
    public boolean storeFile(String localFile, String remoteFile) throws Exception {
        return ftpTemplate.storeFile(localFile, remoteFile);
    }

    @Override
    public boolean storeFile(InputStream localInputStream, String remoteFile) throws Exception {
        return ftpTemplate.storeFile(localInputStream, remoteFile);
    }

    @Override
    public GenericObjectPool<FTPClient> getFTPPool() {
        return ftpTemplate.getFTPPool();
    }


    @Override
    public List<String> listnames() throws Exception {
        GenericObjectPool<FTPClient> ftpPool = ftpTemplate.getFTPPool();
        logger.info("before: {} {} {} {}",
                ftpPool.getNumIdle(),
                ftpPool.getNumActive(),
                ftpPool.listAllObjects().size(),
                ftpPool.getNumWaiters()
        );
        FTPClient ftpClient = ftpPool.borrowObject();
        logger.info("borrowed: {} {} {} {}",
                ftpPool.getNumIdle(),
                ftpPool.getNumActive(),
                ftpPool.listAllObjects().size(),
                ftpPool.getNumWaiters());
        try {
            String[] strings = ftpClient.listNames();
            String[] strings1 = ftpClient.listNames("a.txt");
            return Arrays.asList(strings);
        }finally {
            ftpPool.returnObject(ftpClient);
            logger.info("returned: {} {} {} {}",
                    ftpPool.getNumIdle(),
                    ftpPool.getNumActive(),
                    ftpPool.listAllObjects().size(),
                    ftpPool.getNumWaiters());
        }
    }

    @Override
    public String look() {
        GenericObjectPool<FTPClient> ftpPool = ftpTemplate.getFTPPool();
        logger.info("look: {} {} {} {}",
                ftpPool.getNumIdle(),
                ftpPool.getNumActive(),
                ftpPool.listAllObjects().size(),
                ftpPool.getNumWaiters());
        return String.format("look: %s %s %s %s",
                ftpPool.getNumIdle(),
                ftpPool.getNumActive(),
                ftpPool.listAllObjects().size(),
                ftpPool.getNumWaiters());
    }
}

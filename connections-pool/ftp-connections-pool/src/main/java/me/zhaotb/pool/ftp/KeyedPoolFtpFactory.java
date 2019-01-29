package me.zhaotb.pool.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.io.IOException;

public class KeyedPoolFtpFactory extends BaseKeyedPooledObjectFactory<FtpInfo, FTPClient> {

    /**
     * Create an instance that can be served by the pool.
     *
     * @param key the key used when constructing the object
     * @return an instance that can be served by the pool
     * @throws Exception if there is a problem creating a new instance,
     *                   this will be propagated to the code requesting an object.
     */
    @Override
    public FTPClient create(FtpInfo key) throws Exception {
        FTPClient client = new FTPClient();
        client.connect(key.getIp(), key.getPort());
        if (!client.login(key.getUser(), key.getPassword())){
            throw new IllegalArgumentException("Authentication failed : " + key.toString());
        }
        client.setControlEncoding(key.getCtlEncode());
        return client;
    }

    /**
     * Wrap the provided instance with an implementation of
     * {@link PooledObject}.
     *
     * @param value the instance to wrap
     * @return The provided instance, wrapped by a {@link PooledObject}
     */
    @Override
    public PooledObject<FTPClient> wrap(FTPClient value) {
        return new DefaultPooledObject<>(value);
    }

    /**
     * Uninitialize an instance to be returned to the idle object pool.
     * <p>
     * The default implementation is a no-op.
     *
     * @param key the key used when selecting the object
     * @param p   a {@code PooledObject} wrapping the the instance to be passivated
     */
    @Override
    public void passivateObject(FtpInfo key, PooledObject<FTPClient> p) throws Exception {
        super.passivateObject(key, p);
        FTPClient ftp = p.getObject();
        ftp.logout();
        ftp.disconnect();
    }

    /**
     * Ensures that the instance is safe to be returned by the pool.
     * <p>
     * The default implementation always returns {@code true}.
     *
     * @param key the key used when selecting the object
     * @param p   a {@code PooledObject} wrapping the the instance to be validated
     * @return always <code>true</code> in the default implementation
     */
    @Override
    public boolean validateObject(FtpInfo key, PooledObject<FTPClient> p) {
        FTPClient ftp = p.getObject();
        try {
            return FTPReply.isPositiveCompletion(ftp.stat());
        } catch (IOException e) {
            return false;
        }
    }
}

package me.zhaotb.pool.ftp;

import java.util.List;

public interface MyFtp extends FtpOperation{

    List<String> listnames() throws Exception;

}

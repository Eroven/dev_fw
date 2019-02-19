package me.zhaotb.framework.util;

import java.io.File;

/**
 * @author zhaotangbo
 * @date 2019/1/29
 */
public class OutOfDiskSpaceException extends RuntimeException {

    public OutOfDiskSpaceException(String message) {
        super("磁盘空间不足：" + message);
    }

    public OutOfDiskSpaceException(File file, long size){
        super("磁盘空间不足：" + size + " , " + file.getAbsolutePath());
    }
}

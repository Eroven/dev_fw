package me.zhaotb.file.reader.loader;

import java.io.IOException;

public interface Loader {

    /**
     * 加载一个字节
     * @return 加载的字节
     */
    byte load();

    /**
     * 加载不超过buffer长度的字节
     * @param buffer 将加载到的字节数据放到buffer中
     * @return 实际加载byte个数
     */
    int load(byte[] buffer) throws IOException;

    String DEFAULT_CHARSET = "UTF-8";

    String DEFAULT_LINE_FEED = "\n";

}

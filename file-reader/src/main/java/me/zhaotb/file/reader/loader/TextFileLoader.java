package me.zhaotb.file.reader.loader;


import java.io.File;
import java.nio.channels.FileChannel;

/**
 * 加载文件内容，使用NIO接口
 * @author zhaotangbo
 * @since 2020/11/13
 */
public class TextFileLoader {

    private String charset = Loader.DEFAULT_CHARSET;

    private String lineFeed = Loader.DEFAULT_LINE_FEED;

    private File target;

    private FileChannel channel;

    public String loadLine() {
        channel
    }

}

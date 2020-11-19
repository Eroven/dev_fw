package me.zhaotb.file.reader.loader;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

/**
 * 文件加载器.以 {@link Frame} 为单位作为返回值.
 */
public class TextLoader implements Loader,Closeable {

    /**
     * 指向当前字节读取开始位置
     */
    private int cursor;

    private int curLineNumber = 0;

    private String charsetName;

    private int buffersize;

    private ByteLoader byteLoader;

    public TextLoader(ByteLoader byteLoader, String charsetName) {
        if (byteLoader == null){
            throw new NullPointerException("Loader 不能为空");
        }
        if (!Charset.isSupported(charsetName)){
            throw new UnsupportedCharsetException(charsetName);
        }
        this.byteLoader = byteLoader;
        this.charsetName = charsetName;
    }

    public void close() throws IOException {
        byteLoader.close();
    }

    /**
     *
     * @return 是否还有下一帧数据, true 则定位移动下一帧开始, false 定位保持不动
     */
    public boolean nextFrame() {
        return byteLoader.nextFrame();
    }

    /**
     *
     * @return 是否有上一帧数据, true 定位移动到上一帧开始, false 定位保持不动 0
     */
    public boolean preFrame() {
        return byteLoader.preFrame();
    }

    public void setPosition(long position) {
        byteLoader.setPosition(position);
    }

    public Frame readFrame(){
        Frame frame = new Frame(10);
        byte[] bytes = byteLoader.readFrame();
        try {
            String str = new String(bytes, charsetName);
            StringReader reader = new StringReader(str);
            BufferedReader br = new BufferedReader(reader);
            String line;
            while((line = br.readLine()) != null){
                frame.setData(curLineNumber ++, line);
            }
            line = frame.getData(curLineNumber -1);
            if (line != null){

            }
        } catch (IOException ignored) {
        }



        return frame;
    }

    /**
     * 加载一个字节
     *
     * @return 加载的字节
     */
    @Override
    public byte load() {
        return byteLoader.load();
    }

    /**
     * 加载不超过buffer长度的字节
     *
     * @param buffer 将加载到的字节数据放到buffer中
     * @return 实际加载byte个数
     */
    @Override
    public int load(byte[] buffer) throws IOException {
        return byteLoader.load(buffer);
    }
}

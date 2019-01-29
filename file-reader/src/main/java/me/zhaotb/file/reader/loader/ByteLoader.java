package me.zhaotb.file.reader.loader;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

/**
 * 字节加载器, 按字节加载数据
 */
public class ByteLoader implements Loader, Closeable {

    public static final int DEFAULT_BUFFER_SIZE = 51;

    private int bufferSize = DEFAULT_BUFFER_SIZE;
    private long position = -1;

    private long limit;
    private FileChannel channel;

    public ByteLoader(String filePath, String fileName) throws IOException {
        File file = new File(filePath, fileName);
        if (!file.exists()){
            throw new FileNotFoundException(file.getAbsolutePath());
        }

        limit = file.length();
        channel = FileChannel.open(file.toPath(), StandardOpenOption.READ);

    }


    public void close() throws IOException {
        channel.close();
    }

    public byte[] read(long position, int size){
        if (position < 0){
            position = 0;
        }
        try {
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, position, size);
            byte[] result = new byte[buffer.limit()];
            buffer.get(result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @return 是否还有下一帧数据, true 则定位移动下一帧开始, false 定位保持不动
     */
    public boolean nextFrame(){
        if (position == -1){
            position = 0;
            return limit > 0;
        }
        boolean result = position < limit;
        position += bufferSize;
        if (position > limit){
            position = limit;
        }
        return  result;
    }

    /**
     *
     * @return 是否有上一帧数据, true 定位移动到上一帧开始, false 定位保持不动 0
     */
    public boolean preFrame(){
        boolean result = position > 0;
        position -= bufferSize;
        if (position < 0){
            position = 0;
        }
        return result;
    }

    /**
     * 调用之前可使用 {@link ByteLoader#nextFrame()} 判断是否还有下一帧并向下移动一帧偏移量 <br>
     * 使用 {@link ByteLoader#preFrame()} 判断是否有上一帧并向上移动一帧偏移量
     * @return 返回一帧数据
     */
    public byte[] readFrame(){
        return read(position, bufferSize);
    }

    public void setPosition(long position){
        this.position = position;
    }

    public void setBufferSize(int size){
        this.bufferSize = size;
    }

    /**
     * 加载一个字节
     *
     * @return 加载的字节
     */
    @Override
    public byte load() {
        return 0;
    }

    /**
     * 加载不超过buffer长度的字节
     *
     * @param buffer 将加载到的字节数据放到buffer中
     * @return 实际加载byte个数
     */
    @Override
    public int load(byte[] buffer) {
        return 0;
    }
}

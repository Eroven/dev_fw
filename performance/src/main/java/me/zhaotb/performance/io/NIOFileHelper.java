package me.zhaotb.performance.io;


import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.Selector;
import java.nio.file.Paths;

/**
 * @author zhaotangbo
 * @since 2020/11/25
 */
public class NIOFileHelper {

    private Selector selector;

    public void init() throws IOException {
        this.selector = Selector.open();


    }

    public void readFileTest() throws IOException {
        FileChannel fileChannel = FileChannel.open(Paths.get("F:\\eshore\\projects\\no8","tmp", "krb5.conf"));

        ByteBuffer buffer = ByteBuffer.allocate(16);
        int len;
        ByteOutputStream output = new ByteOutputStream();
        while ((len = fileChannel.read(buffer)) > 0) {
            output.write(buffer.array(), 0, len);
            buffer.clear();
        }
        fileChannel.close();
        byte[] bytes = output.getBytes();

        System.out.println(new String(bytes));
    }

    public static void main(String[] args) throws IOException {
        NIOFileHelper nioFileHelper = new NIOFileHelper();
        nioFileHelper.init();
        nioFileHelper.readFileTest();
    }



}

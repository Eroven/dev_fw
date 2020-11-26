package me.zhaotb.performance.io;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author zhaotangbo
 * @since 2020/11/25
 */
public class NIOSocketClient {

    private Selector selector;
    private SocketConfig config = new SocketConfig();
    private SocketChannel client;

    public void init() throws IOException {
        this.selector = Selector.open();
        client = SocketChannel.open();
        client.configureBlocking(false);
        client.connect(new InetSocketAddress(config.getHost(), config.getPort()));
        client.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }

    public void run() {
        while (true){
            try {
                int select = selector.select();
                if (select < 1)continue;
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey next = iterator.next();
                SocketChannel channel = (SocketChannel) next.channel();
                if (next.isConnectable()) {
                    try {
                        boolean b = channel.finishConnect();
                        if (b){
                            channel.write(ByteBuffer.wrap("Ensure connected !".getBytes()));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (next.isReadable()){
                    try {
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int read = channel.read(buffer);
                        System.out.println(new String(buffer.array(), 0, read));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (next.isWritable()){
                    try {
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int read = channel.read(buffer);
                        System.out.println(new String(buffer.array(), 0, read));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        NIOSocketClient client = new NIOSocketClient();
        client.init();
        new Thread(client::run).start();
    }

}

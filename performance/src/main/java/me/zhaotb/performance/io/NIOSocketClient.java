package me.zhaotb.performance.io;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;
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
        client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }

    public void run() throws IOException {
        while (true){
            int select = selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey next = iterator.next();
                SelectableChannel channel = next.channel();
                System.out.println(channel);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        NIOSocketClient client = new NIOSocketClient();
        client.init();
        client.run();
    }
}

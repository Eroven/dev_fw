package me.zhaotb.performance.io;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author zhaotangbo
 * @since 2020/11/25
 */
public class NIOSocketServer {

    private Selector selector;
    private ServerSocketChannel server;
    private SocketConfig config = new SocketConfig();

    public void init() throws IOException {
        this.selector = Selector.open();
        server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(config.getHost(), config.getPort()));
        server.configureBlocking(false);//必须设置为非阻塞才能注册selector
        server.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void run(){
        while (true) {
            try {
                selector.select();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey next = iterator.next();
//                iterator.remove();
                SelectableChannel channel = next.channel();
                System.out.println(channel);
            }
        }

    }

    public static void main(String[] args) throws IOException {
        NIOSocketServer server = new NIOSocketServer();
        server.init();
        server.run();
    }

}

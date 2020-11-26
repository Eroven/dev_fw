package me.zhaotb.performance.io;


import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author zhaotangbo
 * @since 2020/11/25
 */
public class NIOSocketServer implements Closeable {

    private Selector selector;
    private ServerSocketChannel server;
    private boolean closed = false;
    private SocketConfig config = new SocketConfig();

    public void init() throws IOException {
        this.selector = Selector.open();
        server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(config.getHost(), config.getPort()));
        server.configureBlocking(false);//必须设置为非阻塞才能注册selector
        server.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void run(){
        while (!closed) {
            try {
                int select = selector.select();
                if (select < 1)continue;
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();
                //                iterator.remove();
                if (next.isAcceptable()) {
                    ServerSocketChannel tmp = (ServerSocketChannel) next.channel();
                    try {
                        SocketChannel accept = tmp.accept();
                        accept.configureBlocking(false);
                        accept.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (next.isReadable()) {
                    System.out.println("read:");
                    SocketChannel tmp = (SocketChannel) next.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    try {
                        int read = tmp.read(buffer);
                        System.out.println("服务端：" + new String(buffer.array(), 0, read));
                        tmp.write(ByteBuffer.wrap("Hi,I'm Server.".getBytes()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (next.isWritable()) {
                    System.out.println("write:");
                    SocketChannel tmp = (SocketChannel) next.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    try {
                        int read = tmp.read(buffer);
                        System.out.println("服务端：" + new String(buffer.array(), 0, read));
                        tmp.write(ByteBuffer.wrap("Er,I'm Server.".getBytes()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                iterator.remove();
                System.out.println(next.channel());
            }
        }

    }


    @Override
    public void close() throws IOException {
        closed = true;
        server.close();
    }

    public static void main(String[] args) throws IOException {
        NIOSocketServer server = new NIOSocketServer();
        server.init();
        new Thread(server::run).start();
    }
}

package me.zhaotb.performance.io;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author zhaotangbo
 * @since 2020/11/24
 */
public class NIOTest {


    public static void main(String[] args) throws IOException {
        NIOSocketServer server = new NIOSocketServer();
        server.init();
        new Thread(server::run).start();

        NIOSocketClient client = new NIOSocketClient();
        client.init();
        new Thread(client::run).start();

//        test01();

    }

    private static void test01() throws IOException {
        String host = "localhost";
        int port = 1702;
        Selector selector = Selector.open();
        Selector selector2 = Selector.open();

        ServerSocketChannel server = ServerSocketChannel.open().bind(new InetSocketAddress(host, port));
        server.register(selector, SelectionKey.OP_READ);

        new Thread(() -> {
            SocketChannel accept = null;
            try {
                ByteBuffer buffer = ByteBuffer.allocate(8);
                accept = server.accept();
                int read = accept.read(buffer);
                System.out.println(new String(buffer.array(), 0, read));

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (accept != null) {
                    try {
                        accept.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        SocketChannel client = SocketChannel.open(new InetSocketAddress(host, port));

        client.write(ByteBuffer.wrap("你好,服务端。".getBytes()));
        client.close();

        System.out.println("main");
    }

}

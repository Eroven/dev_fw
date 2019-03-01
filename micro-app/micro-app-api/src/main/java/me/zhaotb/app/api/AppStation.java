package me.zhaotb.app.api;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 应用连接基站
 * @author zhaotangbo
 * @date 2019/3/1
 */
@Slf4j
public class AppStation {

    private Register register;

    private final RegistryConf conf;

    public AppStation(RegistryConf conf) {
        this.conf = conf;
    }

    public void setRegister(Register register) {
        this.register = register;
    }

    /**
     * 初始化基站
     */
    public void init(){
        if (register == null){
            register = new Register(conf);
            register.init();
        }

    }

    private class AdminStation {

        private ServerSocket server;

        private ExecutorService executorService = Executors.newCachedThreadPool();

        public void start() throws StationException, IOException {

            int port = conf.getMinPort();
            int maxPort = conf.getMaxPort();
            for (int i = port; i < maxPort; i++) {
                try {
                    server = new ServerSocket(port);
                    break;
                } catch (IOException e) {
                    log.warn(port + " 被占用.");
                }
            }
            if (server == null){
                log.error(port + " ~ " + maxPort + " 均被占用");
                throw new StationException("启动admin station 失败！");
            }
            while (!server.isClosed()){
                Socket accept = server.accept();
                executorService.submit(new AdminCommu(accept));
            }
        }

        public void stop(){

        }

        private class AdminCommu implements Runnable{
            private Socket client;

            public AdminCommu(Socket client) {
                this.client = client;
            }

            @Override
            public void run() {
                try {
                    InputStream input = client.getInputStream();
                    OutputStream output = client.getOutputStream();

                } catch (IOException e) {
                    log.error("交互异常", e);
                }finally {
                    try {
                        client.close();
                    } catch (IOException e) {
                        log.error("请求端关闭异常：" + client.getInetAddress());
                    }
                }
            }
        }

    }

    private class ProgramStation {
        private Socket socket;

        public void start() {

        }

        public void stop(){

        }
    }

    /**
     * 向admin查询某个节点是否处于活跃状态
     * @param address 节点地址
     * @return true：活跃，false：其他情况
     */
    public boolean isActive(Address address) throws Exception {
        Address admin = register.admin();

        return false;
    }

}

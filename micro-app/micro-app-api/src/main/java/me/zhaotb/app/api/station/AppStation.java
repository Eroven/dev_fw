package me.zhaotb.app.api.station;

import lombok.extern.slf4j.Slf4j;
import me.zhaotb.app.api.register.Address;
import me.zhaotb.app.api.register.Register;
import me.zhaotb.app.api.register.RegistryConf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 应用连接基站
 * @author zhaotangbo
 * @date 2019/3/1
 */
@Slf4j
public class AppStation {

    protected Register register;

    protected RegistryConf conf;

    protected AtomicBoolean stoped = new AtomicBoolean(false);

    public void setConf(RegistryConf conf) {
        this.conf = conf;
    }

    public void setRegister(Register register) {
        this.register = register;
    }

    public static StationBuilder newLeaderStation(){
        StationBuilder stationBuilder = new StationBuilder(new LeaderStationTCP());
        return stationBuilder;
    }

    public static StationBuilder newFollowStation(){
        StationBuilder stationBuilder = new StationBuilder(new FollowStation());
        return stationBuilder;
    }

    public static class StationBuilder {

        private RegistryConf conf;

        private Register register;

        private AppStation appStation;

        public StationBuilder(AppStation appStation) {
            this.appStation = appStation;
        }

        public StationBuilder conf(RegistryConf conf){
            this.conf = conf;
            return this;
        }

        public StationBuilder register(Register register){
            this.register = register;
            return this;
        }

        public AppStation build() throws StationException {
            if (register == null){
                throw new StationException("Register不能为空");
            }
            appStation.setRegister(register);
            if (conf != null){
                appStation.setConf(conf);
            }else {
                appStation.setConf(register.getConf());
            }

            return appStation;
        }
    }


    /**
     * 启动基站，开始建立连接
     * @throws Exception 异常
     */
    public synchronized void start() throws Exception{
    }
    public synchronized void stop(){
        stoped.set(true);
    }


    /**
     *
     */
    private static class LeaderStationUDP extends AppStation {

        private DatagramSocket server;

        @Override
        public void start() throws Exception {
            server = new DatagramSocket(conf.getCtrlPort());
            DatagramChannel channel = server.getChannel();
        }
    }

    private static class LeaderStationTCP extends AppStation {

        private ServerSocket server;

        private ExecutorService executorService = Executors.newCachedThreadPool();

        @Override
        public synchronized void start() throws StationException, IOException {
            if (stoped.get()){
                return;
            }

            ensureServer(conf.getRetryTimes());
            while (!server.isClosed()){
                Socket accept = server.accept();
                executorService.submit(new LeaderCommu(accept));
            }

        }

        private void ensureServer(int retryTimes) {
            if (retryTimes < 1){
                log.error("启动基站失败!");
            }
            try {
                server = new ServerSocket(conf.getCtrlPort());
            } catch (Exception e){
                log.warn("启动基站失败...重试中");
                try {
                    TimeUnit.MILLISECONDS.sleep(conf.getRetryInterval());
                } catch (InterruptedException ignore) {
                    log.warn("重试被打断");
                    return;
                }
                ensureServer(retryTimes - 1);
            }
        }

        @Override
        public synchronized void stop(){
            super.stop();
        }

        private class LeaderCommu implements Runnable {
            private Socket client;

            public LeaderCommu(Socket client) {
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

    private static class FollowStation extends AppStation{
        private ServerSocket heart;
        private Socket socket;

        @Override
        public synchronized void start() {
            try (ServerSocket heart = new ServerSocket(conf.getTickPort())) {
                heart.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void startHeartBeatReceiver(){

        }

        @Override
        public synchronized void stop(){
            super.stop();
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

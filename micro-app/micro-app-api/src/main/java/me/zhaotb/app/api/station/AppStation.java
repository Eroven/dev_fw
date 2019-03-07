package me.zhaotb.app.api.station;

import lombok.extern.slf4j.Slf4j;
import me.zhaotb.app.api.Util;
import me.zhaotb.app.api.register.LeaderCache;
import me.zhaotb.app.api.register.NodeInfo;
import me.zhaotb.app.api.register.Register;
import me.zhaotb.app.api.register.RegistryConf;
import me.zhaotb.app.api.register.RegistryInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 应用连接基站
 *
 * @author zhaotangbo
 * @date 2019/3/1
 */
@Slf4j
public class AppStation {

    private CuratorFramework curator;

    protected Register register;

    protected RegistryConf conf;

    protected AtomicBoolean stopped = new AtomicBoolean(false);

    public void setConf(RegistryConf conf) {
        this.conf = conf;
    }

    public void setRegister(Register register) {
        this.register = register;
    }

    public static StationBuilder newLeaderStation() {
        return new StationBuilder(new LeaderStationTCP());
    }

    public static StationBuilder newFollowStation() {
        return new StationBuilder(new FollowStationTCP());
    }

    public static class StationBuilder {

        private RegistryConf conf;

        private Register register;

        private AppStation appStation;

        public StationBuilder(AppStation appStation) {
            this.appStation = appStation;
        }

        public StationBuilder conf(RegistryConf conf) {
            this.conf = conf;
            return this;
        }

        public StationBuilder register(Register register) {
            this.register = register;
            return this;
        }

        public AppStation build() throws StationException {
//            if (register == null) {
//                throw new StationException("Register不能为空");
//            }
            appStation.setRegister(register);
            if (conf != null) {
                appStation.setConf(conf);
            } else {
                appStation.setConf(register.getConf());
            }

            return appStation;
        }
    }

    public CuratorFramework getCurator() {
        return curator;
    }

    /**
     * 启动基站，开始建立连接.该方法应该立即返回
     *
     * @throws Exception 异常
     */
    public synchronized void start() throws Exception {
        curator = CuratorFrameworkFactory.newClient(conf.getConnectStr(), new ExponentialBackoffRetry(3000, 3));
        curator.start();
    }

    /**
     * 停止基站，回收资源.该方法应该立即返回
     */
    public synchronized void stop() {
        stopped.set(true);
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

        private ServerSocket leaderServer;

        @Override
        public synchronized void start() throws Exception {
            if (stopped.get()) {
                return;
            }
            ensureServer(conf.getRetryTimes());

            Util.getLeaderStationService().execute(this::doRequest);
            Util.getLeaderStationService().execute(this::doTick);
        }

        private void doTick() {
            long tickTime = conf.getTickTime();
            int tickLimit = conf.getTickLimit();
            long tickTimeout = conf.getTickTimeout();



            List<NodeInfo> nodeInfos = LeaderCache.listNodeInfo();
            for (NodeInfo node : nodeInfos) {
                Address tickAddr = node.getTickAddr();

                try {
                    Msg msg = dispatcherTick(tickAddr, tickLimit, tickTimeout);
                    System.out.println(msg);

                } catch (HeartBeatException e){

                }

            }
        }

        private class DispatcherTickTask implements Runnable{

            @Override
            public void run() {

            }
        }

        private Msg dispatcherTick(Address tickAddr, int leftRetryTimes, long timeout) throws HeartBeatException {
            if (leftRetryTimes < 1){
                throw new HeartBeatException("响应超时，在重试" + conf.getTickLimit() + "次后放弃");
            }
            try (Socket socket = new Socket(tickAddr.getIp(), tickAddr.getPort())) {
                Future<Msg> future = Util.getLeaderStationService().submit(new TickTask(socket));
                return future.get(timeout, TimeUnit.MILLISECONDS);
            } catch (IOException | InterruptedException | ExecutionException e) {
                log.error("心跳检测异常", e);
                return dispatcherTick(tickAddr, leftRetryTimes - 1, timeout);
            } catch (TimeoutException e) {
                return dispatcherTick(tickAddr, leftRetryTimes - 1, timeout);
            }
        }

        private class TickTask implements Callable<Msg> {

            private Socket client;

            public TickTask(Socket client) {
                this.client = client;
            }

            @Override
            public Msg call() throws Exception {
                OutputStream out = client.getOutputStream();
                writeMsg(new Msg.TickMsg(), out);
                InputStream input = client.getInputStream();
                byte[] bytes = new byte[20];
                input.read(bytes);
                ByteBuffer buffer = ByteBuffer.wrap(bytes);
                int anInt = buffer.getInt();
                long aLong = buffer.getLong();
                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(aLong)));
                return null;
            }
        }

        private void doRequest() {
            try {
                while (!leaderServer.isClosed()) {
                    Socket accept = leaderServer.accept();
                    Util.getLeaderStationService().execute(new LeaderCommu(accept));
                }
            } catch (IOException e) {
                log.error("Leader 监控请求异常", e);
            }
        }

        private void ensureServer(int retryTimes) throws StationException {
            if (retryTimes < 1) {
                throw new StationException("启动基站失败!");
            }
            try {
                leaderServer = new ServerSocket(conf.getTickPort());
            } catch (Exception e) {
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
        public synchronized void stop() {
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
                } finally {
                    try {
                        client.close();
                    } catch (IOException e) {
                        log.error("请求端关闭异常：" + client.getInetAddress());
                    }
                }
            }
        }

    }

    private static class FollowStationTCP extends AppStation {
        private ServerSocket heart;
        private Socket leaderHeartbeatRequest;
        private ReentrantLock requestLock = new ReentrantLock();
        private Condition requestCond = requestLock.newCondition();

        @Override
        public synchronized void start() {
            if (stopped.get()) {
                return;
            }
            Util.getFollowStationService().execute(this::startHeartBeatReceiver);
            Util.getFollowStationService().execute(this::responseHeartbeat);

            registerStation();

        }

        private void registerStation() {
            RegistryInfo registryInfo = new RegistryInfo();

            String ip = Util.getLocalhostIp();
            registryInfo.setTickAddr(Address.formatAddress(ip, conf.getTickPort()));
            registryInfo.setCtrlAddr(Address.formatAddress(ip, conf.getCtrlPort()));

            try {
                register.register(registryInfo);
            } catch (Exception e) {
                log.error("注册follow station 异常", e);
                stop();
            }
        }

        private void startHeartBeatReceiver() {
            try {
                Socket tmp;
                heart = new ServerSocket(conf.getTickPort());
                while (!heart.isClosed()) {
                    if (stopped.get()) {
                        stop();
                        return;
                    }
                    requestLock.lock();
                    try {
                        tmp = heart.accept();
                        if (leaderHeartbeatRequest != null){
                            leaderHeartbeatRequest.close();
                        }
                        leaderHeartbeatRequest = tmp;
                        requestCond.signal();
                    } finally {
                        requestLock.unlock();
                    }
                }
            } catch (IOException e) {
                log.error("", e);
            }
        }

        private void responseHeartbeat() {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            byte[] headBuffer = new byte[4];
            while (true) {
                if (stopped.get()) {
                    return;
                }
                requestLock.lock();
                try {
                    if (stopped.get()) {
                        return;
                    }
                    if (leaderHeartbeatRequest == null || !leaderHeartbeatRequest.isConnected()) {
                        requestCond.await();
                    }
                } catch (InterruptedException e) {
                    log.warn("等待被打断", e);
                } finally {
                    requestLock.unlock();
                }
                try (InputStream inputStream = leaderHeartbeatRequest.getInputStream();
                     OutputStream outputStream = leaderHeartbeatRequest.getOutputStream()) {

                    while (leaderHeartbeatRequest.isConnected()) {
                        int read = inputStream.read(headBuffer);
                        if (read != headBuffer.length) {
                            throw new HeartBeatException("期望head size ： " + headBuffer.length + " ，实际 ： " + read);
                        }
                        int size = Util.getIntB(headBuffer, 0);
                        byte[] contentBuffer = new byte[size];
                        read = inputStream.read(contentBuffer);
                        if (read != size) {
                            throw new HeartBeatException("期望content size ： " + size + " ，实际 ： " + read);
                        }
                        long requestTimeStamp = Util.getLongB(contentBuffer, 0);
                        log.info("心跳请求时间{}", format.format(new Date(requestTimeStamp)));

                        writeMsg(new Msg.TickMsg(), outputStream);
                    }
                } catch (HeartBeatException e) {
                    log.error("心跳异常", e);
                } catch (Exception e) {
                    log.error("响应心跳发生未知异常", e);
                }
            }
        }


        @Override
        public synchronized void stop() {
            super.stop();
            if (leaderHeartbeatRequest != null) {
                try {
                    leaderHeartbeatRequest.close();
                    leaderHeartbeatRequest = null;
                } catch (IOException e) {
                    log.error("关闭心跳请求端异常", e);
                }
            }
            if (heart != null) {
                try {
                    heart.close();
                    heart = null;
                } catch (IOException e) {
                    log.error("关闭心跳接收端异常", e);
                }
            }
        }
    }

    /**
     * 向admin查询某个节点是否处于活跃状态
     *
     * @param address 节点地址
     * @return true：活跃，false：其他情况
     */
    public boolean isActive(Address address) throws Exception {
        Address admin = register.admin();

        return false;
    }

    protected void writeMsg(Msg msg, OutputStream out) throws IOException {
        int size = msg.size();
        byte[] content = msg.content();
        ByteBuffer buffer = ByteBuffer.allocate(content.length + 4);
        buffer.putInt(size);
        buffer.put(content);
        out.write(buffer.array());
        out.flush();
    }

}

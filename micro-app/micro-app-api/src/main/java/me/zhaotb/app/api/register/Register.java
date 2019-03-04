package me.zhaotb.app.api.register;

import lombok.extern.slf4j.Slf4j;
import me.zhaotb.app.api.Address;
import me.zhaotb.app.api.AppInfo;
import me.zhaotb.app.api.Env;
import me.zhaotb.app.api.Util;
import me.zhaotb.framework.util.CollectionUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

import java.net.InetAddress;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static me.zhaotb.app.api.Util.path;

/**
 * 注册器，与配置中心对接
 *
 * @author zhaotangbo
 * @date 2019/3/1
 */
@Slf4j
public class Register {


    private final RegistryConf conf;

    private CuratorFramework curator;

    private final String IP_PORT_SEP = Env.getIpPortSep();

    private final String programsPath = "programs";

    private final String adminPath = "admin";

    private final String followPath = "follow";

    private final String selectPath = "selectAdmin";

    private TickListener tickListener;

    public Register(RegistryConf conf) {
        this.conf = conf;
    }

    public void init() {
        curator = CuratorFrameworkFactory.newClient(conf.getConnectStr(), new ExponentialBackoffRetry(3000, 3));
        curator.start();
        selectAdmin();
        listenProgram();
    }

    CuratorFramework getCurator() {
        return curator;
    }

    Register getRegister(){
        return this;
    }

    /**
     * 注册心跳监听器
     *
     * @param listener 监听器实例
     */
    public void listenTick(TickListener listener) {
        this.tickListener = listener;
    }

    /**
     * @return 返回管理员的地址
     */
    public Address admin() throws Exception {
        String admin = path(conf.getRoot(), adminPath);
        Stat stat = curator.checkExists().forPath(admin);
        if (stat == null) {
            return null;
        }
        byte[] bytes = getCurator().getData().forPath(admin);

        String str = new String(bytes);
        String[] ipPort = str.split(IP_PORT_SEP, 2);
        return new Address(ipPort[0], Integer.parseInt(ipPort[1]));
    }

    /**
     * 发起admin选举，只有一个节点会被选为admin
     */
    private void selectAdmin() {
        LeaderSelector leaderSelector = new LeaderSelector(curator, path(conf.getRoot(), selectPath), new LeaderSelectorListener() {

            private ReentrantLock lock = new ReentrantLock();

            private Condition cond = lock.newCondition();

            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {
                log.info("当选ADMIN");
                //当选admin
                String admin = path(conf.getRoot(), adminPath);
                ensurePath(client, admin);
                InetAddress address = InetAddress.getLocalHost();
                String hostAddress = address.getHostAddress() + IP_PORT_SEP + conf.getCtrlPort();
                client.setData().forPath(admin, hostAddress.getBytes());

                //打开心跳检测
                String program = path(conf.getRoot(), programsPath);
                ensurePath(client, program);
                PathChildrenCache pathChildrenCache = new PathChildrenCache(client, program, false);
                pathChildrenCache.getListenable().addListener(new ProgramNameListener(getRegister()), Util.getCacheService());
                pathChildrenCache.start();
                doTick();

                //TODO 监听程序节点
                //TODO 打开数据交流端


                //让线程不占cpu，并保证方法不结束，除非连接断开
                while (client.getZookeeperClient().isConnected()) {
                    try {
                        lock.lock();
                        cond.await();
                    } finally {
                        lock.unlock();
                    }
                }
                log.info("卸任ADMIN");
                pathChildrenCache.close();
            }

            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                //如果连接变得不可用，则释放admin控制权
                if (!newState.isConnected()) {
                    try {
                        lock.lock();
                        cond.signal();
                    } finally {
                        lock.unlock();
                    }
                }
            }
        });
        leaderSelector.start();
        Runtime.getRuntime().addShutdownHook(new Thread(leaderSelector::close));
    }

    /**
     * 保证路径存在
     * @param client zk客户端
     * @param path 路径
     * @throws Exception 异常
     */
    private void ensurePath(CuratorFramework client, String path) throws Exception {
        Stat stat = client.checkExists().forPath(path);
        if (stat == null) {
            client.create().creatingParentsIfNeeded().forPath(path);
        }
    }


    public void process(WatchedEvent event) throws Exception {
        Watcher.Event.EventType type = event.getType();
        String path = event.getPath();
        System.out.println(path + " : " + type);
    }

    private void doTick() {

    }

    /**
     * 监听程序注册信息
     */
    private void listenProgram() {

    }

    /**
     * 主动注册信息
     *
     * @param info 注册内容
     * @throws Exception 异常
     */
    public void register(RegistryInfo info) throws Exception {
        CuratorFramework curator = getCurator();
        String appName = info.getAppName();
        Hashtable<Address, Address> addressTable = info.getAddressTable();
        String appPath = path(conf.getRoot(), appName);
        Enumeration<Address> keys = addressTable.keys();
        while (keys.hasMoreElements()) {
            Address tick = keys.nextElement();
            Address ctrl = addressTable.get(tick);
            String tickPath = path(appPath, tick.get());
            String ctrlPath = path(tickPath, ctrl.get());
            ensurePath(curator, ctrlPath);
        }
    }

    /**
     * 获取指定app的注册信息
     *
     * @param appInfo app
     * @return 返回注册信息
     */
    public RegistryInfo getReistryInfo(AppInfo appInfo) {
        try {
            String appPath = path(conf.getRoot(), programsPath, appInfo.getAppName());
            List<String> tickAddrList = curator.getChildren().forPath(appPath);
            if (CollectionUtil.isEmpty(tickAddrList)) {
                return null;
            }
            RegistryInfo info = new RegistryInfo(appInfo.getAppName());
            for (String tick : tickAddrList) {
                try {
                    List<String> ctrl = curator.getChildren().forPath(path(appPath, tick));
                    if (!CollectionUtil.isSingle(ctrl)) {
                        continue;
                    }
                    String[] tickInfo = tick.split(IP_PORT_SEP, 2);
                    String[] ctrlInfo = ctrl.get(0).split(IP_PORT_SEP, 2);
                    info.regist(tickInfo[0], Integer.parseInt(tickInfo[1]), ctrlInfo[0], Integer.parseInt(ctrlInfo[1]));
                } catch (Exception e) {
                    log.warn("无效注册节点：" + path(appPath, tick));
                }
            }

        } catch (Exception e) {
            throw new RegistryException("获取注册信息异常", e);
        }

        return null;
    }

    /**
     * 心跳监听
     */
    public interface TickListener {
        /**
         * @param requestTime  请求时间戳
         * @param responseTime 响应时间戳
         * @param address      地址
         */
        void onTick(long requestTime, long responseTime, Address address);
    }

}

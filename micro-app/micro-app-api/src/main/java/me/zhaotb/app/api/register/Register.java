package me.zhaotb.app.api.register;

import lombok.extern.slf4j.Slf4j;
import me.zhaotb.app.api.AppInfo;
import me.zhaotb.app.api.Env;
import me.zhaotb.app.api.Util;
import me.zhaotb.app.api.station.AppStation;
import me.zhaotb.app.api.station.StationException;
import me.zhaotb.framework.util.CollectionUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

import java.net.InetAddress;
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

    private AppStation followStation;

    private boolean leader = false;

    private ReentrantLock stationLock = new ReentrantLock();

    public Register(RegistryConf conf) {
        this.conf = conf;
    }

    public RegistryConf getConf() {
        return conf;
    }

    public void init() {
        curator = CuratorFrameworkFactory.newClient(conf.getConnectStr(), new ExponentialBackoffRetry(3000, 3));
        curator.start();
        selectAdmin();
        listenProgram();
        setupFollowStation();
    }

    CuratorFramework getCurator() {
        return curator;
    }

    Register getRegister() {
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

            private Register register = getRegister();

            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {
                log.info("当选ADMIN");
                //当选admin
                leader = true;
                String adminPath = path(conf.getRoot(), register.adminPath);
                ensurePath(client, adminPath);
                InetAddress address = InetAddress.getLocalHost();
                String hostAddress = address.getHostAddress() + IP_PORT_SEP + conf.getLeaderPort();
                client.setData().forPath(adminPath, hostAddress.getBytes());

                //打开心跳检测
                String followPath = path(conf.getRoot(), register.followPath);
                ensurePath(client, followPath);
                PathChildrenCache pathChildrenCache = new PathChildrenCache(client, followPath, false);
                pathChildrenCache.getListenable().addListener(new FollowListener(), Util.getNodeWatchedService());
                pathChildrenCache.start();
                AppStation leaderStation = setupLeaderStation();
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
                leaderStation.stop();
                leader = false;

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
     *
     * @param client zk客户端
     * @param path   路径
     * @throws Exception 异常
     */
    private void ensurePath(CuratorFramework client, String path) throws Exception {
        Stat stat = client.checkExists().forPath(path);
        if (stat == null) {
            client.create().creatingParentsIfNeeded().forPath(path);
        }
    }


    /**
     * 监听程序注册信息
     */
    private void listenProgram() {

    }

    private void setupFollowStation() {
        stationLock.lock();
        try {
            if (followStation != null){
                followStation.stop();
            }
            followStation = AppStation.newFollowStation().conf(conf).register(getRegister()).build();
            followStation.start();
        } catch (StationException e) {
            log.error("构建基站异常", e);
        } catch (Exception e) {
            log.error("启动基站异常", e);
        } finally {
            stationLock.unlock();
        }
    }

    private AppStation setupLeaderStation() {
        AppStation leaderStation = null;
        try {
            leaderStation = AppStation.newLeaderStation().conf(conf).register(getRegister()).build();
            leaderStation.start();
        } catch (StationException e) {
            log.error("构建基站异常", e);
        } catch (Exception e) {
            log.error("启动基站异常", e);
        }
        return leaderStation;
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
        String path = path(conf.getRoot(), this.followPath,
                info.getTickAddr().get(), info.getCtrlAddr().get(), appName);
        Stat stat = curator.checkExists().forPath(path);
        String res = path;
        if (stat == null) {
            res = curator.create().creatingParentsIfNeeded().forPath(path);
        }
        log.info("register : {} , return : {}", path, res);
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
//                    info.regist(tickInfo[0], Integer.parseInt(tickInfo[1]), ctrlInfo[0], Integer.parseInt(ctrlInfo[1]));
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

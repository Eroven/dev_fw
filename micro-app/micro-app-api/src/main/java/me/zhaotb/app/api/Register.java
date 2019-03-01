package me.zhaotb.app.api;

import lombok.extern.slf4j.Slf4j;
import me.zhaotb.framework.util.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 注册器，与配置中心对接
 *
 * @author zhaotangbo
 * @date 2019/3/1
 */
@Slf4j
public class Register {

    /**
     * zk目录分割符
     */
    private static final String SEP = "/";

    private final RegistryConf conf;

    private CuratorFramework curator;

    private final String programsPath = "programs";

    private final String adminPath = "admin";

    private final String selectPath = "selectAdmin";

    private TickListener tickListener;

    public Register(RegistryConf conf) {
        this.conf = conf;
    }

    public void init() {
        curator = CuratorFrameworkFactory.newClient(conf.getConnectStr(), new ExponentialBackoffRetry(3000, 3));
        curator.start();
        selectAdmin();
    }

    CuratorFramework getCurator() {
        return curator;
    }

    /**
     * 注册心跳监听器
     * @param listener 监听器实例
     */
    public void listenTick(TickListener listener){
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
        List<String> list = curator.getChildren().forPath(admin);
        if (!CollectionUtil.isSingle(list)) {
            return null;
        }
        String[] ipPort = list.get(0).split(":", 2);
        return new Address(ipPort[0], Integer.parseInt(ipPort[1]));
    }

    /**
     * 发起admin选举，只有一个节点会被选为admin
     */
    public void selectAdmin() {
        try (LeaderSelector leaderSelector = new LeaderSelector(curator, path(conf.getRoot(), selectPath), new LeaderSelectorListener() {

            private ReentrantLock lock = new ReentrantLock();

            private Condition cond = lock.newCondition();

            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {
                log.info("当选ADMIN");
                //当选admin
                //TODO 监听程序节点
                //TODO 打开心跳检测
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
        })) {
            leaderSelector.start();
        }
    }


    public void register(RegistryInfo info) throws Exception {
        String appName = info.getAppName();
        Hashtable<Address, Address> addressTable = info.getAddressTable();
        String appPath = path(conf.getRoot(), appName);
        Stat stat = curator.checkExists().forPath(appPath);
        if (stat == null) {
            curator.create().creatingParentsIfNeeded().forPath(appPath);
        }
        Enumeration<Address> keys = addressTable.keys();
        while (keys.hasMoreElements()) {
            Address tick = keys.nextElement();
            Address ctrl = addressTable.get(tick);
            curator.checkExists().forPath(path(appPath, tick.get()));

        }
    }

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
                    String[] tickInfo = tick.split(":", 2);
                    String[] ctrlInfo = ctrl.get(0).split(":", 2);
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
     * 按zk的规范拼接路劲
     *
     * @param path 路劲数组
     * @return 返回拼接后的路径
     */
    private String path(String... path) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.length; i++) {
            if (StringUtils.isBlank(path[i])) continue;
            if (i == 0 && !path[i].startsWith(SEP)) {
                sb.append(SEP);
            }
            if (i == path.length - 1) {
                if (path[i].endsWith(SEP)) {
                    sb.append(path[i], 0, path[i].length() - 1);
                } else {
                    sb.append(path[i]);
                }
            } else if (path[i].endsWith(SEP)) {
                sb.append(path[i]);
            } else {
                sb.append(path[i]).append(SEP);
            }

        }
        return sb.toString();
    }


    /**
     * 心跳监听
     */
    public interface TickListener {
        /**
         *
         * @param requestTime 请求时间戳
         * @param responseTime 响应时间戳
         * @param address 地址
         */
        void onTick(long requestTime, long responseTime, Address address);
    }

}

package me.zhaotb.app.api.register;

import lombok.extern.slf4j.Slf4j;
import me.zhaotb.app.api.Address;
import me.zhaotb.app.api.Env;
import me.zhaotb.app.api.Util;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.leader.LeaderSelector;

import java.io.IOException;
import java.util.List;

/**
 * @author zhaotangbo
 * @date 2019/3/4
 */
@Slf4j
public class ProgramNameListener implements PathChildrenCacheListener {

    private final Register register;

    public ProgramNameListener(Register register) {
        this.register = register;
    }

    @Override
    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
        PathChildrenCacheEvent.Type type = event.getType();
        String path = event.getData().getPath();

        switch (type){
            case CHILD_ADDED:
                PathChildrenCache pathChildrenCache = new PathChildrenCache(client, path, false);
                pathChildrenCache.getListenable().addListener(new TickIpListener(), Util.getCacheService());
                pathChildrenCache.start();
                Util.whenShutDown(new Thread(() -> {
                    try {
                        pathChildrenCache.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }));
                break;
            case CHILD_UPDATED:
                break;
            case CHILD_REMOVED:
                break;
            case CONNECTION_SUSPENDED:
                break;
            case CONNECTION_RECONNECTED:
                break;
            case CONNECTION_LOST:
                break;
            case INITIALIZED:
                break;
        }
    }

    private class TickIpListener implements PathChildrenCacheListener{

        @Override
        public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
            PathChildrenCacheEvent.Type type = event.getType();
            String path = event.getData().getPath();
            switch (type){
                case CHILD_ADDED:
                    try {
                        String ipPortSep = Env.getIpPortSep();
                        String[] tickIp = Util.lastPath(path).split(ipPortSep);
                        Address tickAddr = new Address(tickIp[0], Integer.parseInt(tickIp[1]));
                        List<String> list = client.getChildren().forPath(path);
                        if (list.size() != 1){
                           throw new Exception();
                        }
                        String[] ctrlIp = list.get(0).split(ipPortSep);
                        Address ctrlAddr = new Address(ctrlIp[0], Integer.parseInt(ctrlIp[1]));
                        LeaderCache.put(tickAddr, ctrlAddr);
                    }catch (Exception e){
                        log.warn("无效注册节点：" + path);
                    }
                    break;
            }
        }
    }
}

package me.zhaotb.app.api.register;

import lombok.extern.slf4j.Slf4j;
import me.zhaotb.app.api.Env;
import me.zhaotb.app.api.Util;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

import java.util.List;

import static me.zhaotb.app.api.Util.path;

/**
 * 按程序名归类
 * @author zhaotangbo
 * @date 2019/3/4
 */
@Slf4j
public class ProgramNameListener implements PathChildrenCacheListener {

    @Override
    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
        PathChildrenCacheEvent.Type type = event.getType();
        String path = event.getData().getPath();
        switch (type){
            case CHILD_ADDED:
                PathChildrenCache pathChildrenCache = new PathChildrenCache(client, path, false);
                pathChildrenCache.getListenable().addListener(new TickIpListener(), Util.getNodeWatchedService());
                pathChildrenCache.start();
                LeaderCache.putPathCache(path, pathChildrenCache);
                break;
            case CHILD_REMOVED:
                LeaderCache.closePathCache(path);
                break;
        }
    }

    private class TickIpListener implements PathChildrenCacheListener {

        @Override
        public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
            PathChildrenCacheEvent.Type type = event.getType();
            String path = event.getData().getPath();
            String ipPortSep = Env.getIpPortSep();
            String programName = Util.lastPath(Util.trimLast(path));
            switch (type){
                case CHILD_ADDED:
                    try {
                        List<String> list = client.getChildren().forPath(path);
                        if (list.size() != 1){
                            throw new Exception();
                        }
                        String ctrlPath = path(path, list.get(0));
                        String[] tickIp = Util.lastPath(path).split(ipPortSep);
                        Address tickAddr = new Address(tickIp[0], Integer.parseInt(tickIp[1]));
                        String[] ctrlIp = list.get(0).split(ipPortSep);
                        Address ctrlAddr = new Address(ctrlIp[0], Integer.parseInt(ctrlIp[1]));

                        NodeInfo nodeInfo = new NodeInfo(tickAddr, ctrlAddr);
                        ProgramInfo programInfo = new ProgramInfo();
                        String cmd = new String(client.getData().forPath(ctrlPath), Env.getCharset());
                        programInfo.setProgramName(programName);
                        programInfo.setCmd(cmd);
                        nodeInfo.setProgramInfo(programInfo);

                        LeaderCache.putNodeInfo(path, nodeInfo);

                        PathChildrenCache pidPathCache = new PathChildrenCache(client, ctrlPath, true);
                        pidPathCache.getListenable().addListener(new CtrlIpListener(), Util.getNodeWatchedService());
                        LeaderCache.putPathCache(path, pidPathCache);
                        pidPathCache.start();

                    }catch (Exception e){
                        log.warn("无效注册节点：" + path);
                        LeaderCache.removeNodeInfo(path);
                    }
                    break;
                case CHILD_REMOVED:
                    LeaderCache.removeNodeInfo(path);
                    LeaderCache.closePathCache(path);
                    break;
            }
        }
    }

    private class CtrlIpListener implements PathChildrenCacheListener {

        @Override
        public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
            PathChildrenCacheEvent.Type type = event.getType();
            String path = event.getData().getPath();
            String tickPath = Util.trimLast(Util.trimLast(path));
            NodeInfo nodeInfo = LeaderCache.getNodeInfo(tickPath);
            int pid = Integer.parseInt(Util.lastPath(path));
            //noinspection Duplicates
            switch (type){
                case CHILD_ADDED:
                    nodeInfo.addPid(pid);
                    break;
                case CHILD_REMOVED:
                    nodeInfo.remove(pid);
                    break;
            }

        }
    }
}

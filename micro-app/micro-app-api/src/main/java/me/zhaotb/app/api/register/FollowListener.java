package me.zhaotb.app.api.register;

import lombok.extern.slf4j.Slf4j;
import me.zhaotb.app.api.Env;
import me.zhaotb.app.api.Util;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

import java.util.List;

/**
 * 按节点归类
 * @author zhaotangbo
 * @date 2019/3/5
 */
@Slf4j
public class FollowListener implements PathChildrenCacheListener {

    @Override
    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
        PathChildrenCacheEvent.Type type = event.getType();
        String path = event.getData().getPath();
        String ipPortSep = Env.getIpPortSep();
        switch (type) {
            case CHILD_ADDED:
                try {
                    List<String> list = client.getChildren().forPath(path);
                    if (list.size() != 1) {
                        throw new Exception();
                    }
                    String[] tickIp = Util.lastPath(path).split(ipPortSep);
                    Address tickAddr = new Address(tickIp[0], Integer.parseInt(tickIp[1]));
                    String[] ctrlIp = list.get(0).split(ipPortSep);
                    Address ctrlAddr = new Address(ctrlIp[0], Integer.parseInt(ctrlIp[1]));

                    NodeInfo nodeInfo = new NodeInfo(tickAddr, ctrlAddr);
                    LeaderCache.putNodeInfo(path, nodeInfo);

                    PathChildrenCache programCache = new PathChildrenCache(client, Util.path(path, list.get(0)), true);
                    programCache.getListenable().addListener(new ProgramListener(), Util.getNodeWatchedService());
                    programCache.start();
                    LeaderCache.putPathCache(path, programCache);

                } catch (Exception e) {
                    log.warn("无效follow节点：" + path);
                    LeaderCache.removeNodeInfo(path);
                    LeaderCache.closePathCache(path);
                }
                break;
            case CHILD_REMOVED:
                LeaderCache.removeNodeInfo(path);
                LeaderCache.closePathCache(path);
                break;
        }
    }

    private class ProgramListener implements PathChildrenCacheListener {

        @Override
        public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
            PathChildrenCacheEvent.Type type = event.getType();
            String path = event.getData().getPath();
            String cmd = new String(event.getData().getData(), Env.getCharset());
            switch (type) {
                case CHILD_ADDED:
                    String tickPath = Util.trimLast(Util.trimLast(path));
                    NodeInfo nodeInfo = LeaderCache.getNodeInfo(tickPath);
                    ProgramInfo programInfo = new ProgramInfo();
                    String programName = Util.lastPath(path);
                    programInfo.setProgramName(programName);
                    programInfo.setCmd(cmd);
                    nodeInfo.setProgramInfo(programInfo);

                    PathChildrenCache pidCache = new PathChildrenCache(client, path, false);
                    pidCache.getListenable().addListener(new PidListener(), Util.getNodeWatchedService());
                    pidCache.start();
                    LeaderCache.putPathCache(path, pidCache);
                    break;
                case CHILD_REMOVED:
                    LeaderCache.closePathCache(path);
                    break;
            }
        }
    }

    private class PidListener implements PathChildrenCacheListener {
        @Override
        public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
            PathChildrenCacheEvent.Type type = event.getType();
            String path = event.getData().getPath();
            int pid = Integer.parseInt(Util.lastPath(path));
            NodeInfo nodeInfo = LeaderCache.getNodeInfo(path);
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

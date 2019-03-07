package me.zhaotb.app.api.register;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

/**
 * @author zhaotangbo
 * @date 2019/3/4
 */
@Slf4j
public class LeaderCache {

    /**
     * 路径与节点信息关联
     */
    private static Hashtable<String, NodeInfo> nodeInfoTable = new Hashtable<>();

    private static Hashtable<String, PathChildrenCache> pathChildrenCacheHashtable = new Hashtable<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(LeaderCache::clear));
    }

    /**
     * 初始化节点信息
     * @param programPath 程序名下心跳ip的zk路径，例如 programPath="/root/hello/ip:port"
     * @return the previous value of the specified key in this hashtable,
     *         or <code>null</code> if it did not have one
     */
    public static NodeInfo initNodeInfo(String programPath){
        return nodeInfoTable.put(programPath, new NodeInfo());
    }

    /**
     * 增加节点信息
     * @param path zk路径作为key
     * @param nodeInfo 节点信息
     */
    public static void putNodeInfo(String path, NodeInfo nodeInfo){
        nodeInfoTable.put(path, nodeInfo);
    }

    /**
     *
     * @param path zk路径
     * @return 节点信息
     */
    public static NodeInfo getNodeInfo(String path){
        return nodeInfoTable.get(path);
    }

    public static NodeInfo removeNodeInfo(String path){
        return nodeInfoTable.remove(path);
    }

    /**
     *
     * @return 返回节点信息视图
     */
    public static List<NodeInfo> listNodeInfo(){
        return new ArrayList<>(nodeInfoTable.values());
    }

    public static void putPathCache(String path, PathChildrenCache cache){
        pathChildrenCacheHashtable.put(path, cache);
    }

    public static PathChildrenCache getPathCache(String path){
        return pathChildrenCacheHashtable.get(path);
    }

    public static void closePathCache(String path){
        PathChildrenCache cache = pathChildrenCacheHashtable.remove(path);
        if (cache != null){
            try {
                cache.close();
            } catch (IOException e) {
                log.error("子节点缓存关闭异常", e);
            }
        }

    }

    /**
     * 某个程序的节点信息
     * @param program 程序名
     * @return 返回节点信息列表
     */
    public static List<NodeInfo> listNodeInfo(String program){
        ArrayList<NodeInfo> res = new ArrayList<>();
        List<NodeInfo> nodeInfos = listNodeInfo();
        for (NodeInfo nd: nodeInfos) {
            if (nd.getProgramInfo().getProgramName().equals(program)){
                res.add(nd);
            }
        }

        return res;
    }


    /**
     * 清除所有节点信息
     */
    public static void clear(){
        nodeInfoTable.clear();
        Collection<PathChildrenCache> values = pathChildrenCacheHashtable.values();
        for (PathChildrenCache cache : values) {
            if (cache != null){
                try {
                    cache.close();
                } catch (IOException e) {
                    log.error("子节点缓存关闭异常", e);
                }
            }
        }
        pathChildrenCacheHashtable.clear();
    }


}

package me.zhaotbdt.admin;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.fastjson.JSONArray;
import me.zhaotb.dt.cnode.api.CalRequest;
import me.zhaotb.dt.cnode.api.CalResponse;
import me.zhaotb.dt.cnode.api.CalService;
import me.zhaotb.dt.cnode.api.report.MonthlyAcctItemService;
import me.zhaotb.dt.sdk.api.DispatchRule;
import me.zhaotb.dt.sdk.api.DispatchType;
import me.zhaotb.dt.sdk.api.MergeRule;
import me.zhaotb.dt.sdk.api.TaskManager;
import me.zhaotb.dt.sdk.api.report.CalAcctItemManager;
import me.zhaotb.dt.sdk.impl.CalAcctItemManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("admin")
public class AdminController {

    private Executor executor = Executors.newCachedThreadPool();

    private ConcurrentHashMap<Class<? extends CalService>, ConcurrentHashMap<String, ReferenceConfig<CalService>>>
            configCache = new ConcurrentHashMap<>();

    private AdminTaskList taskList;

    @Autowired
    public AdminController(AdminTaskList taskList) {
        this.taskList = taskList;
    }

    @RequestMapping(value = "server/{serverName}", method = RequestMethod.POST)
    public Object doServe(@PathVariable("serverName") String serverName, Object param) {
        TaskManager taskManager = taskList.get(serverName);
        DispatchRule dispatchRule = taskManager.dispatchRule();
        List<Object> contents = dispatchRule.getContents();
        MergeRule mergeRule = taskManager.mergeRule();
        Class<? extends CalService> serviceClass = taskManager.forService();
        switch (dispatchRule.getType()) {
            case ONE_NODE:
                break;
            case FAST_FIRST:
                break;
            case ONE_BY_ONE:
                try {
                    List<String> arr = findCnodes(taskManager.forService().getName());
                    Iterator<Object> iterator = contents.iterator();

                    ConcurrentHashMap<String, ReferenceConfig<CalService>> cache = configCache.get(serviceClass);
                    if (cache == null){
                        cache = new ConcurrentHashMap<>();
                        ApplicationConfig ac = new ApplicationConfig();
                        ac.setName("ADMIN");
                        RegistryConfig rc = new RegistryConfig();
                        rc.setAddress("zookeeper://localhost:2191");
                        for (String node : arr) {
                            ReferenceConfig<CalService> config = new ReferenceConfig<>();
                            config.setApplication(ac);
                            config.setRegistry(rc);
                            String url = "dubbo://" + node;
                            config.setUrl(url);
                            cache.put(url, config);
                        }
                    }

                    ExecutorService pool = Executors.newFixedThreadPool(arr.size());

                    while (iterator.hasNext()) {
                        for (int i = 0; i < arr.size() && iterator.hasNext(); i++) {
                            String url = "dubbo://" + arr.get(i);
                            ReferenceConfig<CalService> config = cache.get(url);
                            pool.execute(() -> {
                                config.setInterface(MonthlyAcctItemService.class);
                                CalService calService = config.get();
                                CalRequest req = new CalRequest();
                                req.setContent(iterator.next());
                                CalResponse response = calService.doServe(req);
                                mergeRule.addResult(response);
                            });

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("走到默认来了,怎么回事 ?");
        }
        return mergeRule.finalResult();

    }

    @RequestMapping("acctItem")
    public Object monthlyAcctItem(@RequestParam("date") String date) {
        CalAcctItemManager manager = new CalAcctItemManagerImpl();
        DispatchRule dispatchRule = manager.dispatchRule();
        MergeRule mergeRule = manager.mergeRule();
        DispatchType type = dispatchRule.getType();
        switch (type) {
            case ONE_NODE:
                break;
            case FAST_FIRST:
                break;
            case ONE_BY_ONE:

                break;
            default:
                System.out.println("走到默认来了,怎么回事 ?");
        }
        return mergeRule.finalResult().getAttachment();
    }

    private List<String> findCnodes(String serviceName) throws IOException {
        URL url = new URL("http://localhost:9000/register/find?sn=" + serviceName);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream inputStream = conn.getInputStream();
        byte[] buffer = new byte[1024];
        int read;
        StringBuilder json = new StringBuilder();
        while ((read = inputStream.read(buffer)) > 0) {
            json.append(new String(buffer, 0, read));
        }
        JSONArray arr = (JSONArray) JSONArray.parse(json.toString());
        List<String> list = arr.toJavaList(String.class);
        return list;
    }

}

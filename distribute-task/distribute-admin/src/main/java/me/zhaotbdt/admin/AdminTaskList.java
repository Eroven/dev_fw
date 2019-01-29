package me.zhaotbdt.admin;

import me.zhaotb.dt.sdk.api.TaskManager;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Component
public class AdminTaskList {

    private HashMap<String , TaskManager> taskManagers = new LinkedHashMap<>();

    void translateTaskManager(Map<String, Object> map) {
        Object obj;
        Set<String> keys = map.keySet();
        for (String key : keys) {
            obj = map.get(key);
            if (obj instanceof TaskManager){
                TaskManager tm = (TaskManager) obj;
                taskManagers.put(tm.taskName(), tm);
            }
        }
    }

    public TaskManager get(String serverName) {
        TaskManager taskManager = taskManagers.get(serverName);
        if (taskManager == null){
            throw new ServiceNotFindException(serverName);
        }
        return taskManager;
    }
}

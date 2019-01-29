package me.zhaotb.framework.util;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author zhaotangbo
 * @date 2018/11/28
 */
public class FileUtil {

    /**
     *
     * @param location 文件绝对路径
     * @return 返回本地独一无二的文件绝对路径, 如果该文件名本来就唯一, 则直接返回; 若不存在文件,也直接返回
     *
     */
    public static String uniqueName(String location) {
        File file = new File(location);
        if (!file.exists() || file.isDirectory()){
            return location;
        }
        return file.getPath() + File.pathSeparator + uniqueName(file.getPath(), file.getName());
    }

    /**
     * @param path 路径
     * @param name 文件名
     * @return 返回本地独一无二的文件名, 如果该文件名本来就唯一, 则直接返回 name
     */
    public static String uniqueName(String path, String name){
       return uniqueName(path, name, 1);
    }

    /**
     *
     * @param path 文件路径
     * @param name 文件名
     * @param seq 序列号开始位
     * @return 返回唯一为本地文件名
     */
    public static String uniqueName(String path, String name, int seq){
        File file = new File(path, name);
        if (file.exists()){
            int i = name.lastIndexOf(".");
            if (i > 0 && i + 1 < name.length()){//点存在且不是最后一个字符
                name = name.substring(0, i) + "(" + seq + ")" + name.substring(i , name.length());
            }else {
                name = name + "(" + seq + ")";
            }
            return uniqueName(path, name, seq + 1);
        }
        return name;
    }
}

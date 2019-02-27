package me.zhaotb.framework.util;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author zhaotangbo
 * @date 2018/11/28
 */
public class FileUtil {

    private static String PRE_MARK = "(";
    private static String SUF_MARK = ")";

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
        return file.getParent() + File.separator + uniqueName(file.getParent(), file.getName());
    }

    /**
     * @param path 路径
     * @param name 文件名
     * @return 返回本地独一无二的文件名, 如果该文件名本来就唯一, 则直接返回 name
     */
    public static String uniqueName(String path, String name){
        File file = new File(path, name);
        if (!file.exists()){
            return name;
        }
       return uniqueName(path, name, 1);
    }

    /**
     *
     * @param path 文件路径
     * @param name 文件名
     * @param seq 序列号开始位
     * @return 返回唯一为本地文件名
     */
    private static String uniqueName(String path, String name, int seq){
        String fileDesc = "";
        int i = name.lastIndexOf(".");
        if (i > 0 && i + 1 < name.length()){//点存在且不是最后一个字符
            fileDesc = name.substring(i , name.length());
            name = name.substring(0, i);
        }
        String tmp;
        File file;
        while (seq < Integer.MAX_VALUE){
            tmp = name + PRE_MARK + (seq++) + SUF_MARK + fileDesc;
            file = new File(path, tmp);
            if (!file.exists()){
                return tmp;
            }
        }
        return uniqueName(path, name + PRE_MARK + Integer.MAX_VALUE + SUF_MARK + "_MAX" + fileDesc, 1);
    }

    /**
     * 将源文件移动到目标文件
     * @param srcFile 源文件
     * @param destFile 目标文件
     * @return true：成功，否则false
     * @throws OutOfDiskSpaceException 如果磁盘空间不足抛出该异常
     */
    public static boolean move(File srcFile, File destFile) throws OutOfDiskSpaceException {
        if (!srcFile.isFile()){
            return false;
        }

        File destPath = destFile.getParentFile();
        if (!destPath.isDirectory() && !destPath.mkdirs()){
            return false;
        }

        long usableSpace = destPath.getUsableSpace();
        long length = (long) (srcFile.length() * 1.5);
        if (length > usableSpace){
            throw new OutOfDiskSpaceException(destFile, length);
        }

        return srcFile.renameTo(destFile);
    }

    /**
     * 复制文件
     * @param srcFile 源文件
     * @param destFile 目标文件
     * @param append 是否追加，true：追加；false：不追加。不追加的时候如果目标文件已经存在则直接返回false
     * @return true：成功，其他false
     */
    public static boolean copy(File srcFile, File destFile, boolean append) throws FileNotFoundException {
        if (destFile.isFile() && !append){
            return false;
        }

        try(FileInputStream in = new FileInputStream(srcFile);
            FileOutputStream out = new FileOutputStream(destFile, append)) {
            long copy = IOUtils.copyLarge(in, out);
            return copy == srcFile.length();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }


}

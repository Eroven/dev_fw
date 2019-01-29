package me.zhaotb.framework.util;

import org.springframework.util.StreamUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author zhaotangbo
 * @date 2018/11/27
 */
public class ZipUtil {

    /**
     * @param targetPath 目标路径
     * @param outputPath 输出目录
     * @param outputName 输出文件名
     * @return 返回输出的绝对路径
     */
    public static String zip(String targetPath, String outputPath, String outputName, boolean showLog) throws IOException {
        if (StringUtil.isEmpty(outputName)){
            outputName = "out.zip";
        }
        if (!outputName.endsWith(".zip")){
            outputName += ".zip";
        }
        outputName = FileUtil.uniqueName(outputPath, outputName);
        File file = new File(targetPath);
        File outFile = new File(outputPath, outputName);
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFile));
        compress(file, out, file.getName(), showLog);
        out.finish();
        return outFile.getAbsolutePath();
    }

    //压缩
    private static void compress(File file, ZipOutputStream out, String base, boolean showLog) throws IOException {
        if (file.isDirectory()){
            File[] files = file.listFiles();
            if (files.length < 1){
                if (showLog){
                    System.out.println("compress: " + base);
                }
                out.putNextEntry(new ZipEntry(base + File.separator));
            }else {
                for (File f : files) {
                    compress(f, out, base + File.separator + f.getName(), showLog);
                }
            }
        }else {
            if (showLog){
                System.out.println("compress: " + base);
            }
            out.putNextEntry(new ZipEntry(base));
            try(FileInputStream in = new FileInputStream(file)) {
                StreamUtils.copy(in, out);
            }
        }
    }


    /**
     * 解压缩字符串
     * @param str 压缩目标字符串
     * @return 返回解压缩后的字符串
     */
    public static String ungzip(String str) throws IOException {
        if (StringUtil.isEmpty(str)){
            return str;
        }
        byte[] buffer = new BASE64Decoder().decodeBuffer(str);
        ByteArrayOutputStream out = new ByteArrayOutputStream(buffer.length);
        ByteArrayInputStream input = new ByteArrayInputStream(buffer);
        try(GZIPInputStream zinput = new GZIPInputStream(input)){
            StreamUtils.copy(zinput, out);
        }
        return out.toString();
    }

    /**
     * 压缩字符串
     * @param str 目标字符串
     * @return 返回压缩后的字符串
     */
    public static String gzip(String str) {
        if (StringUtil.isEmpty(str)){
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try(GZIPOutputStream gzipOut = new GZIPOutputStream(out)){
            gzipOut.write(str.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new BASE64Encoder().encode(out.toByteArray());
    }


}

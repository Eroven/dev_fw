package me.zhaotb.leecode.day20210115;


import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * 一个文件中有5亿行数据，每行一个不大于5亿的数字，先找出这5亿个数字中非重复数字。内存使用不得超过500MB
 * <p>
 * 分析1：long型数字占8字节64位，5亿÷64 = 7812500。使用两个这样长度的long数组即可找出题解。占用空间：7812500*8*2=120MB
 * 考虑到有一个符号位，实际能用63位。5亿÷63=7936507.936507937=7936508，占用空间7936508*8*2=122MB。
 *
 * @author zhaotangbo
 * @date 2021/1/25
 */
public class FindUnrepeatedNum {


    /**
     * 用来标记未出现的数字一直到出现1次
     */
    private long[] unknown = new long[7936508];
    /**
     * 用来标记数字出现第二次以上的
     */
    private long[] repeated = new long[7936508];

    public File find(File file) throws IOException {
        File noRepeated = new File(file.getParent(), "noRepeated.txt");
        final int bits = 63;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            reader.lines()
                    .filter(StringUtils::isNoneBlank)
                    .map(Integer::parseInt)
                    .forEach(num -> {
                        int idx = num / bits;
                        int offset = num % bits;
                        if (1 == (1 & (unknown[idx] >>> offset))) {
                            //标记重复
                            repeated[idx] = repeated[idx] + (1L << offset);
//                            System.out.println("重复数字：" + num);
                        } else {
                            //标记已出现
                            unknown[idx] = unknown[idx] + (1L << offset);
                        }
                    });
        }
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(noRepeated)))) {
            for (int i = 0; i < unknown.length; i++) {
                for (int j = 0; j < bits; j++) {
                    //出现过一次的数字
                    if ((((unknown[i] >>> j) & 1) == 1) &&
                            //并且未出现过第二次
                            (((repeated[i] >>> j) & 1) == 0)) {
                        writer.write(((long) bits * i + j) + "\n");
                        writer.flush();
                    }
                }
            }
        }

        return noRepeated;
    }

    public static void main(String[] args) throws IOException {
        File file = new File("F:\\ztb\\tmp\\numbers.txt");
        int max = 500000000;
//        int lines = max / 5; //文件大小大于500MB就行了
//        int mod = lines / 100;
//        long t1 = System.currentTimeMillis();
//        Random random = new Random();
//        FileOutputStream fos = new FileOutputStream(file);
//        for (int i = 0; i < lines; i++) {
//            String content = Integer.toString(random.nextInt(max));
//            if (i + 1 < lines) {
//                content += "\n";
//            }
//            fos.write(content.getBytes());
//            if (i % mod == 0) {
//                long t2 = System.currentTimeMillis();
//                System.out.println("写文件进度：" + (i/mod) + " %, 耗时：" + (t2 - t1) + " ms");
//            }
//        }
//        fos.close();

        long t3 = System.currentTimeMillis();
        System.out.println("计算前内存：" + (Runtime.getRuntime().totalMemory() / 1024 / 1024) + " MB");
        FindUnrepeatedNum findDuplicateNum = new FindUnrepeatedNum();
        File noRepeated = findDuplicateNum.find(file);
        long t4 = System.currentTimeMillis();
        System.out.println("计算后内存：" + (Runtime.getRuntime().totalMemory() / 1024 / 1024) + " MB");
        System.out.println("计算耗时： " + (t4 - t3) + " ms");


    }

}

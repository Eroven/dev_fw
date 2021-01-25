package me.zhaotb.leecode.day20210115;


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
 * 分析1：long型数字占8字节64位，5亿÷64 = 7812500。使用一个这样长度的long数组和两个文件即可找出题解。占用空间：7812500*8=62500000字节=60MB
 * 考虑到有一个符号位，实际能用63位。5亿÷63=7936507.936507937=7936508，占用空间61MB，使用两个数组，空间翻倍，即122MB。
 *
 * @author zhaotangbo
 * @date 2021/1/25
 */
public class FindRepeatedNum {


    /**
     * 用来标记未出现的数字一直到出现1次
     */
    private long[] unknown = new long[7936508];
    /**
     * 用来标记数字出现第二次以上的
     */
    private long[] repeated = new long[7936508];

    public void find(File file) throws IOException {
        File noRepeated = new File(file.getParent(), "noRepeated.txt");
        final int bits = 63;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            int num;
            int idx;
            int offset;
            int isShow;
            String line;
            while ((line = reader.readLine()) != null){
                num = Integer.parseInt(line);
                idx = num / bits;
                offset = num % bits;
                isShow = (int) (1 & (unknown[idx] >>> offset));
                if (isShow == 1) {
                    //标记重复
                    repeated[idx] = repeated[idx] + (1L << offset);
                } else {
                    //标记已出现
                    unknown[idx] = unknown[idx] + (1L << offset);
                }
            }

        }
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(noRepeated)))) {
            for (int i = 0; i < unknown.length; i++) {
                for (int j = 0; j < bits; j++) {
                    //出现过一次的数字
                    if ((((unknown[i] >>> j) & 1) == 1) &&
                            //并且未出现过第二次
                            (((repeated[i] >>> j) & 1) == 0)) {
                        writer.write(((long)bits * i + j) + "\n");
                        writer.flush();
                    }
                }
            }
        }

    }

    public static void main(String[] args) throws IOException {
        FindRepeatedNum findDuplicateNum = new FindRepeatedNum();
        findDuplicateNum.find(new File("F:\\ztb\\tmp\\numbers.txt"));

    }

}

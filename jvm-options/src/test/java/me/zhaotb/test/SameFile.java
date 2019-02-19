package me.zhaotb.test;

import org.junit.Test;

import java.io.File;

/**
 * @author zhaotangbo
 * @date 2019/2/1
 */
public class SameFile {

    @Test
    public void same(){
        File file = new File("F:\\tmp\\shard");
        File file2 = new File("F:\\tmp\\shard");

        System.out.println(file.equals(file2));
    }
}

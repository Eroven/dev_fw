package me.zhaotb.test;

import me.zhaotb.framework.util.StringUtil;
import me.zhaotb.log.LoggerNameDiscriminator;
import me.zhaotb.log.Main;
import me.zhaotb.log.async.Output;
import org.junit.Test;

import java.io.IOException;

/**
 * @author zhaotangbo
 * @date 2018/12/20
 */
public class TestLog {

    @Test
    public void write() throws InterruptedException {
        Thread main = new Thread(() -> {
            LoggerNameDiscriminator.setCityId("757");
            new Main().logAllLevel();
        });
        Thread output = new Thread(() -> {
            LoggerNameDiscriminator.setCityId("200");
            new Output().logAllInfo();
        });
        main.start();
        output.start();

        main.join();
        output.join();


    }

    @Test
    public void testEncode() throws IOException {
        String s = StringUtil.normalToHexString("沃尔玛（广东）商业零售有限公司河源中山大道分店_4000021219200361744_20181201.txt");
        System.out.println(s);
        System.out.println(StringUtil.hexToNormalString(s));

    }


    @Test
    public void testAnd(){
        System.out.println(0b101 & 0b010);
    }

}

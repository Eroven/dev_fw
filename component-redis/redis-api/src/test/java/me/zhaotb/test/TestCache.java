package me.zhaotb.test;

import me.zhaotb.redis.impl.CommonCacheOperation;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author zhaotangbo
 * @date 2019/2/25
 */
public class TestCache {

    @Test
    public void testCache(){
        try (Jedis jedis = new Jedis("192.168.179.132", 6379)) {
            CommonCacheOperation cache = new CommonCacheOperation();
            cache.setJedis(jedis);
            boolean b = cache.setString("key", "foo");
            boolean intV = cache.setInt("intV", 123);
            System.out.println(b);

        }
    }

}

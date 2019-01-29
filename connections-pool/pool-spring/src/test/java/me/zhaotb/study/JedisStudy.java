package me.zhaotb.study;

import org.junit.Test;
import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisStudy {

    @Test
    public void test(){

        Jedis jedis = new Jedis("ip", 200);
        jedis.connect();
        Client client = new Client();
        JedisPool pool;
    }
}

package me.zhaotb.redis.impl;

import com.alibaba.fastjson.JSON;
import me.zhaotb.redis.api.CacheOperation;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Protocol;

import java.io.Serializable;

/**
 * @author zhaotangbo
 * @date 2019/2/25
 */
public class CommonCacheOperation implements CacheOperation {

    private Jedis jedis;

    @Override
    public Jedis getJedis() {
        return jedis;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }

    @Override
    public String getString(String key) {
        return jedis.get(key);
    }

    @Override
    public boolean setString(String key, String val) {
        String res = jedis.set(key, val);
        return Protocol.Keyword.OK == Protocol.Keyword.valueOf(res);
    }

    @Override
    public Integer getInt(String key) {
        return Integer.valueOf(jedis.get(key));
    }

    @Override
    public boolean setInt(String key, int val) {
        String res = jedis.set(key, String.valueOf(val));
        return Protocol.Keyword.OK == Protocol.Keyword.valueOf(res);
    }

    @Override
    public Long getLong(String key) {
        return Long.valueOf(jedis.get(key));
    }

    @Override
    public boolean setLong(String key, long val) {
        String res = jedis.set(key, String.valueOf(val));
        return Protocol.Keyword.OK == Protocol.Keyword.valueOf(res);
    }

    @Override
    public <T extends Serializable> T getObject(String key, Class<T> type) {
        return JSON.parseObject(jedis.get(key), type);
    }

    @Override
    public <T extends Serializable> boolean setObject(String key, Class<T> type) {
        String res = jedis.set(key, JSON.toJSONString(type));
        return Protocol.Keyword.OK == Protocol.Keyword.valueOf(res);
    }

    @Override
    public long ttl(String key) {
        return jedis.ttl(key);
    }
}

package me.zhaotb.redis.api;

import redis.clients.jedis.Jedis;

import java.io.Serializable;

/**
 * @author zhaotangbo
 * @date 2019/2/25
 */
public interface CacheOperation {

    Jedis getJedis();

    String getString(String key);

    boolean setString(String key, String val);

    Integer getInt(String key);

    boolean setInt(String key, int val);

    Long getLong(String key);

    boolean setLong(String key, long val);

    <T extends Serializable> T getObject(String key, Class<T> type);

    <T extends Serializable> boolean setObject(String key, Class<T> type);

    long ttl(String key);


}

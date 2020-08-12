package com.open.redisLock;

import com.open.store.StoreClientInterface;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Author:  lining17
 * Date :  2020-08-12
 */
public class RedisLockImpl implements StoreClientInterface , InitializingBean {



    @Value("#{redisConf['redisConf.host']}")
    private  String host;
    @Value("#{redisConf['redisConf.port']}")
    private  int port;
    @Value("#{redisConf['redisConf.passwd']}")
    private  String auth;

    @Autowired
    private JedisPoolConfig poolConfig;


    static JedisPool pool = null;

    private static ThreadLocal<Jedis> jedisThreadLocal = new ThreadLocal<Jedis>() ;


    @Override
    public String getLock(String key) {
        return null;
    }

    @Override
    public Long setnx(String key, String value) {
        return null;
    }

    @Override
    public String getSet(String key, String value) {
        return null;
    }

    @Override
    public Long expire(String key, int expireTime) {
        return null;
    }

    @Override
    public Long del(String key) {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.pool = new JedisPool(poolConfig, host, port, 10000);
    }
}

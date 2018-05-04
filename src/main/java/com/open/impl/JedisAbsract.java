package com.open.impl;

import com.open.mort.LockUtils;
import com.open.service.DistributeLockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Author:  mort
 * Date :  03/05/2018
 */

public abstract class JedisAbsract implements DistributeLockService, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(JedisAbsract.class);

    static JedisPool pool = null;

    private static ThreadLocal<Jedis> jedisThreadLocal = new ThreadLocal<Jedis>() ;

    abstract void init();

    @Override
    public String get(String key) {
        setJedisThreadLocal();
        String ret = jedisThreadLocal.get().get(key);
        return ret;
    }

    @Override
    public Long setnx(String key, String value) {
        setJedisThreadLocal();
        Long ret = jedisThreadLocal.get().setnx(key, value);
        return ret;
    }

    @Override
    public String getSet(String key, String value) {
        setJedisThreadLocal();
        String ret = jedisThreadLocal.get().getSet(key, value);
        return ret;
    }

    @Override
    public Long expire(String key, int expireTime) {
        setJedisThreadLocal();
        Long ret = jedisThreadLocal.get().expire(key, expireTime);
        return ret;
    }

    @Override
    public Long del(String key) {
        setJedisThreadLocal();
        Long ret = jedisThreadLocal.get().del(key);
        return ret;
    }


    /**
     * AutoWired 是单例bean 构造函数只会在初始化bean的执行一次，
     * 但是要为每一个线程创建一个Resource连接,
     * 只能用ThreadLocal的check创建
     */
    public static void setJedisThreadLocal() {
        if(null == jedisThreadLocal.get()){
            jedisThreadLocal.set(pool.getResource());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }


    @Override
    protected void finalize() throws Throwable {
        if(null != jedisThreadLocal.get()){
            jedisThreadLocal.get().close();
        }
    }
}

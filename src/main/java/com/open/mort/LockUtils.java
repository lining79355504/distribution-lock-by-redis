package com.open.mort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.UUID;

/**
 * Author:  mort
 * Date :  26/04/2018
 */
@Service
public class LockUtils implements InitializingBean {


    private static final Logger logger = LoggerFactory.getLogger(LockUtils.class);

    private static JedisPool pool = null;

    @Value("#{redisConf['redisConf.host']}")
    private  String host;
    @Value("#{redisConf['redisConf.port']}")
    private  int port;
    @Value("#{redisConf['redisConf.passwd']}")
    private  String auth;
    @Autowired
    private JedisPoolConfig poolConfig;

//    private static final ThreadLocal<Jedis> jedisThreadLocal = new ThreadLocal<Jedis>() ;


    //此处构造函数的实现获取不到poolConfig bean  必须采用afterPropertiesSet()实现
    void init() {
        this.pool = new JedisPool(poolConfig, host, port, 10000);
    }

    //  一种一直持续等待拿到锁 , 持续等待拿到锁。

    // 一种锁被抢占 又业务自己重试 获取。

    /**
     * @param key
     * @param expire millis
     * @return
     */
    public static boolean getLock(String key, int expire, int waitTime) {
        Jedis jedis = pool.getResource();
//        jedisThreadLocal.set(pool.getResource());
        long startTime = System.currentTimeMillis();
        while (!getLockLoop(key, expire ,jedis)) {
            long waitEndTime = System.currentTimeMillis();
            if (waitEndTime - startTime > waitTime ) return false;
        }

        jedis.close();

        return true;

    }

    /**
     * @param key
     * @param expire
     * @return 过期时间戳 + _ + UUID   UUID解决多线程下毫秒并发问题
     */
    public static boolean getLockLoop(String key, int expire , Jedis jedis) {


        long expireTime = System.currentTimeMillis() + expire;

        String value = String.valueOf(expireTime) + "_" + UUID.randomUUID();
        try {
//            Jedis jedis = pool.getResource();
            Long ret = jedis.setnx(key, value);
            if (1 == ret.longValue()) {
                jedis.expire(key, expire/1000);
                logger.info("acquire lock {} , {} ",key , value);
                return true;
            }

            // key already set
            if (0 == ret.longValue()) {

                String old = jedis.get(key);
                String[] strings = old.split("_");
                long oldExpireTimeStamp = Long.valueOf(strings[0]);

                if (oldExpireTimeStamp < System.currentTimeMillis()) {

                    if (old.equals(jedis.getSet(key, value))) {
                        jedis.expire(key, expire/1000);        // UUID 作用
                        logger.info("acquire lock {} , {} , {} ,{} ",key ,old ,value,System.currentTimeMillis());
                        return true;
                    }
                }

                return false;
            }
        } catch (Exception e) {
            logger.error("exception {}", e);
            jedis.close();
            return false;
        }

        return false;

    }

    /**
     * @param key
     * @return
     */
    public static boolean releaseLock(String key){
        Jedis jedis = pool.getResource();
        jedis.del(key);
        jedis.close();
        return true ;
    }




    public void setPoolConfig(JedisPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }


    @Override
    public void afterPropertiesSet() throws Exception {

        this.init();
    }
}

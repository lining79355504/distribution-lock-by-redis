package com.open.impl;

import com.open.mort.LockUtils;
import com.open.service.DistributeLockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Author:  mort
 * Date :  03/05/2018
 */
@Service
public class JedisImpl extends JedisAbsract {

    private static final Logger logger = LoggerFactory.getLogger(JedisImpl.class);


    @Value("#{redisConf['redisConf.host']}")
    private  String host;
    @Value("#{redisConf['redisConf.port']}")
    private  int port;
    @Value("#{redisConf['redisConf.passwd']}")
    private  String auth;
    @Autowired
    private JedisPoolConfig poolConfig;


    public void init() {
        this.pool = new JedisPool(poolConfig, host, port, 10000);
    }


    public static Jedis getJedisResource(){
        return pool.getResource();
    }
}

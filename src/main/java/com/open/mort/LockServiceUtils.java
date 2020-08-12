package com.open.mort;

import com.open.service.DistributeLockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * Author:  mort
 * Date :  26/04/2018
 */
@Service
public class LockServiceUtils {


    private static final Logger logger = LoggerFactory.getLogger(LockServiceUtils.class);

    /*@PostConstruct：被@PostConstruct修饰的方法会在服务器加载Servle的时候运行，并且只会被服务器执行一次。
    PostConstruct在构造函数之后执行*/

    @Autowired
    private DistributeLockService distributeLockService;


    private static LockServiceUtils lockServiceUtils ;

    @PostConstruct
    public  void init(){
        lockServiceUtils = this;
        lockServiceUtils.distributeLockService = this.distributeLockService;
    }


    //  一种一直持续等待拿到锁 , 持续等待拿到锁。
    // 一种锁被抢占 又业务自己重试 获取。

    /**
     * @param key
     * @param expire millis
     * @return
     */
    public static boolean getLock(String key, int expire, int waitTime) {
        long startTime = System.currentTimeMillis();
        while (!getLockLoop(key, expire )) {
            long waitEndTime = System.currentTimeMillis();
            if (waitEndTime - startTime > waitTime ) return false;
        }
        return true;

    }

    /**
     * @param key
     * @param expire
     * @return 过期时间戳 + _ + UUID   UUID解决多线程下毫秒并发问题
     */
    public static boolean getLockLoop(String key, int expire ) {
        long currentTime = System.currentTimeMillis() ;
        long expireTime = currentTime + expire;

        String value = String.valueOf(expireTime) + "_" + UUID.randomUUID();
        try {
            Long ret = lockServiceUtils.distributeLockService.setnx(key, value);
            if (1 == ret.longValue()) {
                lockServiceUtils.distributeLockService.expire(key, expire/1000);
                logger.info("acquire lock {} , {} ",key , value);
                return true;
            }

            // key already set
            if (0 == ret.longValue()) {

                String old = lockServiceUtils.distributeLockService.get(key);
                if(null == old) return false;
                String[] strings = old.split("_");
                long oldExpireTimeStamp = Long.valueOf(strings[0]);

                if (oldExpireTimeStamp < currentTime) {

                    if (old.equals(lockServiceUtils.distributeLockService.getSet(key, value))) {
                        lockServiceUtils.distributeLockService.expire(key, expire/1000);        // UUID 作用
                        logger.info("acquire lock {} , {} , {} ,{} ",key ,old ,value,System.currentTimeMillis());
                        return true;
                    }
                }

                return false;
            }
        } catch (Exception e) {
            logger.error("exception {}", e);
            return false;
        }

        return false;

    }

    /**
     * @param key
     * @return
     */
    public static boolean releaseLock(String key){
        lockServiceUtils.distributeLockService.del(key);
        return true ;
    }


}

package com.open.store;

import com.open.mysqllock.MysqlLockImpl;
import com.open.redisLock.RedisLockImpl;

/**
 * Author:  lining17
 * Date :  2020-08-12
 * 锁数据的多存储实现 redis mysql 等
 *
 *  根据配置的LockStoreClientBeanFactory bean, 配置MySQL则使用MySQL 配置Redis则使用Redis  里面有两个连接的初始化mysql redis 等
 */

public class StoreClient implements StoreClientInterface{


    private ThreadLocal<StoreClientInterface> storeClientInterfaceThreadLocal = new ThreadLocal<StoreClientInterface>();

    private MysqlLockImpl mysqlLock;

    private RedisLockImpl redisLock;


    @Override
    public String getLock(String key) {
        return storeClientInterfaceThreadLocal.get().getLock(key);
    }

    @Override
    public Long setnx(String key, String value) {
        return storeClientInterfaceThreadLocal.get().setnx(key, value);
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
        return storeClientInterfaceThreadLocal.get().del(key);
    }
}

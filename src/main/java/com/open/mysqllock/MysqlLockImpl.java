package com.open.mysqllock;

import com.open.db.mapper.LockMapper;
import com.open.store.StoreClientInterface;

import javax.annotation.Resource;

/**
 * Author:  lining17
 * Date :  2020-08-12
 */
public class MysqlLockImpl implements StoreClientInterface {


    @Resource
    private LockMapper lockMapper;

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
}

package com.open.store;

/**
 * Author:  lining17
 * Date :  2020-08-12
 */
public interface StoreClientInterface {

    String getLock(String key);

    Long setnx(String key, String value);

    String getSet(String key, String value);

    Long expire(String key, int expireTime);

    Long del(String key);

}

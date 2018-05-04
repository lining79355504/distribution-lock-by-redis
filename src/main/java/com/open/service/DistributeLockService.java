package com.open.service;

/**
 * Author:  mort
 * Date :  26/04/2018
 */
public interface DistributeLockService {

    String get(String key);

    Long setnx(String key, String value);

    String getSet(String key, String value);

    Long expire(String key, int expireTime);

    Long del(String key);


}

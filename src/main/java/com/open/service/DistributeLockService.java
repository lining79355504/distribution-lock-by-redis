package com.open.service;

/**
 * Author:  mort
 * Date :  26/04/2018
 */
public interface DistributeLockService<K ,V> {


    void set(K key, V value);

    V get(Object key);

//    setNx();
//
//    getSet();
//
//    expire();


}

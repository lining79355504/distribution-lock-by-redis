package com.open.spring;

import com.open.store.StoreClient;
import com.open.store.StoreClientInterface;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;

/**
 * Author:  lining17
 * Date :  2020-08-12
 */
public class LockStoreClientBeanFactory implements FactoryBean, DisposableBean {

    private String redisConfHost;
    private String redisConfPort;
    private String redisConfPasswd;
    private int maxConnect;

    @Override
    public void destroy() throws Exception {

    }

    // 根据不同配置实现不同的存储 渲染StoreClient 对象不同的存储
    // 或者直接返回 mysql redis 等存储的实例
    @Override
    public StoreClientInterface getObject() throws Exception {
        //根据配置 return mysql db

        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}

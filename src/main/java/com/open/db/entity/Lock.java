package com.open.db.entity;

import java.util.Date;

/**
 * Author:  lining17 
 * Date :  2020-08-12
 */

public class Lock {
    private Integer id;

    private String lockKey;

    private Date time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLockKey() {
        return lockKey;
    }

    public void setLockKey(String lockKey) {
        this.lockKey = lockKey;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
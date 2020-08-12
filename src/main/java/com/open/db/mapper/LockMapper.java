package com.open.db.mapper;

import com.open.db.entity.Lock;

/**
 * Author:  lining17 
 * Date :  2020-08-12
 */

public interface LockMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Lock record);

    int insertSelective(Lock record);

    Lock selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Lock record);

    int updateByPrimaryKey(Lock record);
}
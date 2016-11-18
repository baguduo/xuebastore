package com.touchfuture.takeout.service;

import com.touchfuture.takeout.common.QueryBase;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by user on 2016/10/30.
 */

public interface BasicService<T> {

    @Transactional(isolation = Isolation.READ_COMMITTED,rollbackFor = Throwable.class)
    public int add(T t);

    @Transactional(isolation = Isolation.READ_COMMITTED,rollbackFor = Throwable.class)
    public int delete(T t);

    @Transactional(isolation = Isolation.READ_COMMITTED,rollbackFor = Throwable.class)
    public int update(T t);

    @Transactional(isolation = Isolation.READ_COMMITTED,rollbackFor = Throwable.class)
    public T get(int id);

    public void query(QueryBase queryBase);

}

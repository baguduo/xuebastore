package com.touchfuture.takeout.mapper;

import com.touchfuture.takeout.bean.User;
import com.touchfuture.takeout.common.QueryBase;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    boolean isExitByName(String name);

    User selectByName(String name);

    boolean isExitById(int id);

    List<User> queryAll(QueryBase queryBase);

    long countUser(QueryBase queryBase);
}
package com.touchfuture.takeout.mapper;

import com.touchfuture.takeout.bean.Admin;


public interface AdminMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Admin record);

    int insertSelective(Admin record);

    Admin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Admin record);

    int updateByPrimaryKey(Admin record);

    boolean isExitByName(String name);

    boolean isExitById(int id);

    Admin selectByName(String name);

}
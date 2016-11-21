package com.touchfuture.takeout.mapper;

import com.touchfuture.takeout.bean.Flight;
import com.touchfuture.takeout.bean.User;
import com.touchfuture.takeout.common.QueryBase;

import java.util.List;

public interface FlightMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Flight record);

    int insertSelective(Flight record);

    Flight selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Flight record);

    int updateByPrimaryKey(Flight record);

    boolean isExitByDateAndFltNo(Flight flight);

    boolean isExitById(int id);

    List<Flight> queryAll(QueryBase queryBase);

    long countFlight(QueryBase queryBase);

    List<Flight> queryTakeoffTimeNearest(QueryBase queryBase);

    long countTakeoffTimeNear(QueryBase queryBase);

    List<Flight> queryLandingTimeNearest(QueryBase queryBase);

    long countLandingTimeNear(QueryBase queryBase);
}
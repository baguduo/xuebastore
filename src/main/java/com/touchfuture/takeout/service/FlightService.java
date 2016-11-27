package com.touchfuture.takeout.service;

import com.touchfuture.takeout.bean.Flight;
import com.touchfuture.takeout.common.QueryBase;

/**
 * Created by user on 2016/11/3.
 */
public interface FlightService extends BasicService<Flight> {
    void queryTakeoffTimeNear(QueryBase queryBase);
    void  queryLandingTimeNear(QueryBase queryBase);
    void  queryTakeoffOrLandingTimeNear(QueryBase queryBase);
}

package com.touchfuture.takeout.serviceImpl;

import com.touchfuture.takeout.bean.Flight;
import com.touchfuture.takeout.bean.User;
import com.touchfuture.takeout.common.EncryptionUtil;
import com.touchfuture.takeout.common.QueryBase;
import com.touchfuture.takeout.common.Status;
import com.touchfuture.takeout.mapper.FlightMapper;
import com.touchfuture.takeout.service.FlightService;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import java.util.List;

/**
 * Created by user on 2016/11/3.
 */

@Service
public class FlightServiceImpl implements FlightService{

    @Autowired
    private FlightMapper flightMapper;

    private Log logger = LogFactory.getLog(FlightService.class);

    /**
     * 添加航班信息
     * @param flight 航班对象
     * @return 0-成功 6-存在 1-失败
     */
    @Override
    public int add(Flight flight) {
        if(flightMapper.isExitByDateAndFltNo(flight)){
            logger.warn("该航班信息已经存在");
            return Status.EXISTS;
        }
        if(flightMapper.insert(flight) > 0){
            logger.warn("航班信息添加成功");
            return Status.SUCCESS;
        }
        logger.warn("航班信息添加失败");
        return Status.ERROR;
    }

    /**
     * 删除航班信息
     * @param flight 航班
     * @return 0-成功 7-不存在 1-失败
     */
    @Override
    public int delete(Flight flight) {
        Flight flight_db = flightMapper.selectByPrimaryKey(flight.getId());
        if(flight_db == null){
            logger.warn("航班信息不存在，无法删除");
            return Status.NOT_EXISTS;
        }
        if(flightMapper.deleteByPrimaryKey(flight.getId()) > 0){
            logger.warn("a航班信息删除成功");
            return Status.SUCCESS;
        }
        logger.warn("航班信息删除失败");
        return Status.ERROR;
    }

    /**
     * 更新航班信息
     * @param flight 航班
     * @return 0-成功 7-不存在 1-失败
     */
    @Override
    public int update(Flight flight) {
        Flight flight_db = flightMapper.selectByPrimaryKey(flight.getId());
        if(flight_db == null){
            logger.warn("航班呢信息不存在，更新失败");
            return Status.NOT_EXISTS;
        }
        if(flightMapper.updateByPrimaryKeySelective(flight) > 0){
            logger.warn("航班信息更新成功");
            return Status.SUCCESS;
        }
        logger.warn("航班信息更新失败");
        return Status.ERROR;
    }

    /**
     * 获取航班信息
     * @param id 航班信息id
     * @return 0-成功 7-不存在 1-失败
     */
    @Cacheable(value = "FlightCache")
    @Override
    public Flight get(int id) {
        logger.warn("到数据库中查找该id航班");
        if(!flightMapper.isExitById(id)){
            logger.warn("该id不存在");
            return null;
        }
        Flight flight_db = flightMapper.selectByPrimaryKey(id);
        return flight_db;
    }


    @Override
    public void query(QueryBase queryBase) {
        if(logger.isDebugEnabled()){
            logger.debug("根据参数" + queryBase.getParameters() + "查询用户");
        }
        queryBase.getParameters().put("statusDelete","-1");
        //queryBase.setResults(userMapper.queryAll(queryBase));
        queryBase.setResults(flightMapper.queryAll(queryBase));
        queryBase.setTotalRow(flightMapper.countFlight(queryBase));
    }


    public void queryTakeoffTimeNear(QueryBase queryBase){
        if(logger.isDebugEnabled()){
            logger.debug("根据参数"+queryBase.getParameters()+"查询最近起飞航班信息");
        }
        queryBase.getParameters().put("statusDelete", "-1");

        queryBase.setResults(flightMapper.queryTakeoffTimeNearest(queryBase));
        queryBase.setTotalRow(flightMapper.countTakeoffTimeNear(queryBase));

    }

    public void queryLandingTimeNear(QueryBase queryBase){
        if(logger.isDebugEnabled()){
            logger.debug("根据参数"+queryBase.getParameters()+"查询用户");
        }
        queryBase.getParameters().put("statusDelete","-1");
        queryBase.setResults(flightMapper.queryLandingTimeNearest(queryBase));
        queryBase.setTotalRow(flightMapper.countLandingTimeNear(queryBase));
    }

    public void queryTakeoffOrLandingTimeNear(QueryBase queryBase){
        if(logger.isDebugEnabled()){
            logger.debug("根据参数"+queryBase.getParameters()+"查询航班");
        }
        queryBase.getParameters().put("statusDelete","-1");
        queryBase.setResults(flightMapper.queryTakeoffOrLandingTimeNearest(queryBase));
        queryBase.setTotalRow(flightMapper.countTakeoffOrLandingTimeNear(queryBase));
    }
}

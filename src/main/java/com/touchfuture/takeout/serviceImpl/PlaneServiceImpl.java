package com.touchfuture.takeout.serviceImpl;

import com.touchfuture.takeout.bean.Flight;
import com.touchfuture.takeout.bean.Plane;
import com.touchfuture.takeout.common.QueryBase;
import com.touchfuture.takeout.common.Status;
import com.touchfuture.takeout.mapper.FlightMapper;
import com.touchfuture.takeout.mapper.PlaneMapper;
import com.touchfuture.takeout.service.FlightService;
import com.touchfuture.takeout.service.PlaneService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * Created by user on 2016/11/24.
 */
@Service
public class PlaneServiceImpl implements PlaneService{

    @Autowired
    private PlaneMapper planeMapper;

    private Log logger = LogFactory.getLog(FlightService.class);

    /**
     * 添加飞机信息
     * @param plane 机型对象
     * @return 0-成功 6-存在 1-失败
     */
    @Override
    public int add(Plane plane) {
        if(planeMapper.isExitByType(plane)){
            logger.warn("该机型信息已经存在");
            return Status.EXISTS;
        }
        if(planeMapper.insert(plane) > 0){
            logger.warn("机型信息添加成功");
            return Status.SUCCESS;
        }
        logger.warn("机型信息添加失败");
        return Status.ERROR;
    }

    /**
     * 删除机型信息
     * @param plane 机型
     * @return 0-成功 7-不存在 1-失败
     */
    @Override
    public int delete(Plane plane) {
        Plane plane_db = planeMapper.selectByPrimaryKey(plane.getId());
        if(plane_db == null){
            logger.warn("机型信息不存在，无法删除");
            return Status.NOT_EXISTS;
        }
        if(planeMapper.deleteByPrimaryKey(plane.getId()) > 0){
            logger.warn("机型信息删除成功");
            return Status.SUCCESS;
        }
        logger.warn("机型信息删除失败");
        return Status.ERROR;
    }

    /**
     * 更新机型信息
     * @param plane 机型
     * @return 0-成功 7-不存在 1-失败
     */
    @Override
    public int update(Plane plane) {
        Plane plane_db = planeMapper.selectByPrimaryKey(plane.getId());
        if(plane_db == null){
            logger.warn("机型信息不存在，更新失败");
            return Status.NOT_EXISTS;
        }
        if(planeMapper.isExitByType(plane)){
            logger.warn("该机型信息已经存在，更新失败");
            return  Status.EXISTS;
        }

        if(planeMapper.updateByPrimaryKeySelective(plane) > 0){
            logger.warn("机型信息更新成功");
            return Status.SUCCESS;
        }
        logger.warn("机型信息更新失败");
        return Status.ERROR;
    }

    /**
     * 获取机型信息
     * @param id 机型信息id
     * @return 0-成功 7-不存在 1-失败
     */
    @Override
    public Plane get(int id) {
        if(!planeMapper.isExitById(id)){
            logger.warn("该机型不存在");
            return null;
        }
        Plane plane_db = planeMapper.selectByPrimaryKey(id);
        return plane_db;
    }

    @Override
    public void query(QueryBase queryBase) {
        if(logger.isDebugEnabled()){
            logger.debug("根据参数" + queryBase.getParameters() + "查询机型");
        }
        queryBase.getParameters().put("statusDelete","-1");
        //queryBase.setResults(userMapper.queryAll(queryBase));
        queryBase.setResults(planeMapper.queryAll(queryBase));
        queryBase.setTotalRow(planeMapper.countPlane(queryBase));
    }

}

package com.touchfuture.takeout.serviceImpl;

import com.sun.xml.internal.ws.developer.Serialization;
import com.touchfuture.takeout.bean.Plane;
import com.touchfuture.takeout.bean.PlaneImage;
import com.touchfuture.takeout.common.QueryBase;
import com.touchfuture.takeout.common.Status;
import com.touchfuture.takeout.mapper.PlaneImageMapper;
import com.touchfuture.takeout.mapper.PlaneMapper;
import com.touchfuture.takeout.service.FlightService;
import com.touchfuture.takeout.service.PlaneImageService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by user on 2016/12/22.
 */
@Component("planeImageService")
public class PlaneImageServiceImpl implements PlaneImageService{
    @Autowired
    private PlaneImageMapper planeImageMapper;

    private Log logger = LogFactory.getLog(FlightService.class);

    /**
     * 添加图片信息
     * @param planeImage   图片对象
     * @return 0-成功 6-存在 1-失败
     */
    @Override
    public int add(PlaneImage planeImage) {

//        int id = planeImage.getId();
//        if(planeImageMapper.isExitById(id)){
//            logger.warn("该图片已经存在");
//            return Status.EXISTS;
//        }
        if(planeImageMapper.insert(planeImage) > 0){
            logger.warn("图片添加成功");
            return Status.SUCCESS;
        }
        logger.warn("图片添加失败");
        return Status.ERROR;
    }

    /**
     * 删除图片
     * @param planeImage 图片
     * @return 0-成功 7-不存在 1-失败
     */
    @Override
    public int delete(PlaneImage planeImage) {
        PlaneImage planeImage_db = planeImageMapper.selectByPrimaryKey(planeImage.getId());
        if(planeImage_db == null){
            logger.warn("图片不存在，无法删除");
            return Status.NOT_EXISTS;
        }
        if(planeImageMapper.deleteByPrimaryKey(planeImage.getId()) > 0){
            logger.warn("图片删除成功");
            return Status.SUCCESS;
        }
        logger.warn("图片删除失败");
        return Status.ERROR;
    }

    /**
     * 更新图片信息
     * @param planeImage 图片
     * @return 0-成功 7-不存在 1-失败
     */
    @Override
    public int update(PlaneImage planeImage) {
        PlaneImage planeImage_db = planeImageMapper.selectByPrimaryKey(planeImage.getId());
        if(planeImage_db == null){
            logger.warn("图片不存在，更新失败");
            return Status.NOT_EXISTS;
        }

        if(planeImageMapper.updateByPrimaryKeySelective(planeImage) > 0){
            logger.warn("图片信息更新成功");
            return Status.SUCCESS;
        }
        logger.warn("图片信息更新失败");
        return Status.ERROR;
    }

    /**
     * 获取图片
     * @param id 图片id
     * @return 0-成功 7-不存在 1-失败
     */
    @Override
    public PlaneImage get(int id) {
        if(!planeImageMapper.isExitById(id)){
            logger.warn("图片不存在");
            return null;
        }
        PlaneImage planeImage_db = planeImageMapper.selectByPrimaryKey(id);
        return planeImage_db;
    }

    /**
     * 根据参数查询图像
     * @param queryBase 查询参数
     */
    @Override
    public void query(QueryBase queryBase) {
        if(logger.isDebugEnabled()){
            logger.debug("根据参数" + queryBase.getParameters() + "查询图像");
        }
        queryBase.getParameters().put("statusDelete","-1");
        //queryBase.setResults(userMapper.queryAll(queryBase));
        queryBase.setResults(planeImageMapper.queryAll(queryBase));
        queryBase.setTotalRow(planeImageMapper.countImage(queryBase));
    }
}

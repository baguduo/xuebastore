package com.touchfuture.takeout.action;

import com.touchfuture.takeout.bean.Flight;
import com.touchfuture.takeout.bean.Plane;
import com.touchfuture.takeout.common.QueryBase;
import com.touchfuture.takeout.common.Response;
import com.touchfuture.takeout.common.Status;
import com.touchfuture.takeout.service.FlightService;
import com.touchfuture.takeout.service.PlaneService;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * Created by user on 2016/11/27.
 */

@Controller
public class PlaneAction {
    Log logger = LogFactory.getLog(FlightAction.class);

    @Autowired
    PlaneService planeService;

    /**
     * 添加机型信息
     * @param request 请求
     * @param plane 机型
     * @return 0-成功 1-失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/plane/add",method = RequestMethod.POST)
    public Object add(HttpServletRequest request,@RequestBody Plane plane){
        int status;
        String message = "";
        status = planeService.add(plane);
        if(status == Status.SUCCESS){
            message = "机型信息添加成功";
            return new Response(status,message,plane);
        }
        message = "机型信息添加失败";
        return new Response(status,message,plane);
    }

    /**
     * 删除机型信息
     * @param request 请求
     * @param id 机型信息id
     * @return 0-成功 1-失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/plane/delete/{id}",method = RequestMethod.DELETE)
    public Object delete(HttpServletRequest request,@PathVariable int id){
        int status;
        String message = "";
        Plane plane = new Plane();
        plane.setId(id);
        status = planeService.delete(plane);
        if(status == Status.SUCCESS){
            message = "机型信息删除成功";
            return new Response(status,message,plane);
        }
        message = "机型信息删除失败";
        return new Response(status,message,plane);
    }

    /**
     * 更新机型信息
     * @param request 请求
     * @param plane 要更新的机型信息
     * @return  0-成功 1-失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/plane/update",method = RequestMethod.PATCH)
    public Object update(HttpServletRequest request,@RequestBody Plane plane){
        int status;
        String message = "";
        status = planeService.update(plane);
        if(status == Status.SUCCESS){
            message = "机型信息更新成功";
            return new Response(status,message,plane);
        }
        message = "机型信息更新失败";
        return new Response(status,message,plane);
    }

    /**
     * 查询机型信息
     * @param request 请求
     * @param id 机型信息id
     * @return 0-成功 1-失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/plane/get/{id}",method = RequestMethod.POST)
    public Object get(HttpServletRequest request,@PathVariable int id){
        int status;
        String message = "";
        Plane plane = planeService.get(id);
        if(plane != null){
            status = Status.SUCCESS;
            message = "机型信息查询成功";
            return new Response(status,message,plane);
        }
        status = Status.ERROR;
        message = "机型信息查询失败";
        return new Response(status,message,null);
    }

    /**
     * 查询所有机型信息
     * @param request 请求
     * @param query 查询参数对象
     * @return 0-成功 1-失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/plane/query",method = RequestMethod.GET)
    public Object query(HttpServletRequest request,QueryBase query){
        String message;
        if(query == null){
            query = new QueryBase();
        }
        planeService.query(query);
        message = "查询成功";
        logger.warn(query.getResults());

        return new Response(Status.SUCCESS,message,query.getResults(),query.getTotalRow());
    }
}

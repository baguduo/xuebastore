package com.touchfuture.takeout.action;

import com.touchfuture.takeout.bean.Flight;
import com.touchfuture.takeout.bean.User;
import com.touchfuture.takeout.common.QueryBase;
import com.touchfuture.takeout.common.Response;
import com.touchfuture.takeout.common.Status;
import com.touchfuture.takeout.mapper.FlightMapper;
import com.touchfuture.takeout.service.FlightService;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by user on 2016/11/13.
 */

@Controller
public class FlightAction {

    Log logger = LogFactory.getLog(FlightAction.class);

    @Autowired
    FlightService flightService;

    /**
     * 添加航班信息
     * @param request 请求
     * @param flight 航班
     * @return 0-成功 1-失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/flight/add",method = RequestMethod.POST)
    public Object add(HttpServletRequest request,@RequestBody Flight flight){
        int status;
        String message = "";
        status = flightService.add(flight);
        if(status == Status.SUCCESS){
            message = "航班信息添加成功";
            return new Response(status,message,flight);
        }
        message = "航班信息添加失败";
        return new Response(status,message,flight);
    }

    /**
     * 删除航班信息
     * @param request 请求
     * @param id 航班信息id
     * @return 0-成功 1-失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/flight/delete/{id}",method = RequestMethod.DELETE)
    public Object delete(HttpServletRequest request,@PathVariable int id){
        int status;
        String message = "";
        Flight flight = new Flight();
        flight.setId(id);
        status = flightService.delete(flight);
        if(status == Status.SUCCESS){
            message = "航班信息删除成功";
            return new Response(status,message,flight);
        }
        message = "航班信息删除失败";
        return new Response(status,message,flight);
    }

    /**
     * 更新航班信息
     * @param request 请求
     * @param flight 要更新的航班信息
     * @return  0-成功 1-失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/flight/update",method = RequestMethod.PATCH)
    public Object update(HttpServletRequest request,@RequestBody Flight flight){
        int status;
        String message = "";
        status = flightService.update(flight);
        if(status == Status.SUCCESS){
            message = "航班信息更新成功";
            return new Response(status,message,flight);
        }
        message = "航班信息更新失败";
        return new Response(status,message,flight);
    }

    /**
     * 查询航班信息
     * @param request 请求
     * @param id 航班信息id
     * @return 0-成功 1-失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/flight/get/{id}",method = RequestMethod.POST)
    public Object get(HttpServletRequest request,@PathVariable int id){
        int status;
        String message = "";
        Flight flight = flightService.get(id);
        if(flight != null){
            status = Status.SUCCESS;
            message = "航班信息查询成功";
            return new Response(status,message,flight);
        }
        status = Status.ERROR;
        message = "航班信息查询失败";
        return new Response(status,message,null);
    }

    /**
     * 查询所有航班信息
     * @param request 请求
     * @param query 查询参数对象
     * @return 0-成功 1-失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/flight/query",method = RequestMethod.GET)
    public Object query(HttpServletRequest request,QueryBase query){
        String message;
        if(query == null){
            query = new QueryBase();
        }
        flightService.query(query);
        message = "查询成功";
        logger.warn(query.getResults());

        return new Response(Status.SUCCESS,message,query.getResults(),query.getTotalRow());
    }

    /**
     * 查询据当前时间最接近的降落航班信息
     * @param request 请求
     * @param query 查询参数对象
     * @return 0-成功 1-失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/flight/timeTakeoffNear",method = RequestMethod.GET)
    public Object queryTimeTakeoffNear(HttpServletRequest request,QueryBase query){
        String message;
        if(query == null){
            query = new QueryBase();
        }
        flightService.queryTakeoffTimeNear(query);
        message = "查询成功";
        logger.warn(query.getResults());

        return new Response(Status.SUCCESS,message,query.getResults(),query.getTotalRow());
    }

    /**
     * 查询据当前时间最接近的起飞航班信息
     * @param request 请求
     * @param query 查询参数对象
     * @return 0-成功 1-失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/flight/timeLandingNear",method = RequestMethod.GET)
    public Object queryTimeLandingNear(HttpServletRequest request,QueryBase query){
        String message;
        if(query == null){
            query = new QueryBase();
        }
        flightService.queryLandingTimeNear(query);
        message = "查询成功";
        logger.warn(query.getResults());

        return new Response(Status.SUCCESS,message,query.getResults(),query.getTotalRow());
    }
}

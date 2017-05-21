package com.touchfuture.takeout.action;

import com.touchfuture.takeout.bean.Flight;
import com.touchfuture.takeout.bean.OneDayWeatherInf;
import com.touchfuture.takeout.bean.User;
import com.touchfuture.takeout.bean.WeatherInf;
import com.touchfuture.takeout.bean.viewBean.view_Flight_Plane;
import com.touchfuture.takeout.common.*;
import com.touchfuture.takeout.mapper.FlightMapper;
import com.touchfuture.takeout.service.*;
import com.touchfuture.takeout.service.WeatherService;
import com.touchfuture.takeout.serviceImpl.WeatherServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by user on 2016/11/13.
 */

@Controller
public class FlightAction {

    Log logger = LogFactory.getLog(FlightAction.class);

    @Autowired
    @Qualifier("weatherservice")
    WeatherService weaService;

    @Autowired
    @Qualifier("iplocservice")
    IPlocService iPlocService;

    @Autowired
    FlightService flightService;


    NewRedisUtil newRedisUtil = new NewRedisUtil();

   // WeatherServiceImpl weatherService = new WeatherServiceImpl();

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
     * 查询据当前时间最接近的起飞航班信息
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
     * 查询据当前时间最接近的降落航班信息
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
        List<? extends  Object> list = query.getResults();
        //Flight flight = (Flight)list.get(0);

        return new Response(Status.SUCCESS,message,query.getResults(),query.getTotalRow());
    }

    /**
     * 根据用户ip所在地、天气和机型推荐航班
     */
    @ResponseBody
    @RequestMapping(value = "api/flight/flightRecommend",method = RequestMethod.GET)
    public Object flightRecommend(HttpServletRequest request,QueryBase query)throws  Exception{


        String message;
        if(query == null){
            query = new QueryBase();
        }

        //根据IP获取用户所在城市
        AddressUtil addressUtils = new AddressUtil();
       // String ip ="125.70.11.136" ;
        String ip = "58.31.255.251";
        String cityName = null;

        //使用redis对ip所在城市信息进行缓存
        if (newRedisUtil.exitKey(ip)) {
            cityName = newRedisUtil.getValue(ip);
            System.out.println("从redis获取到ip所在城市拉！");
        } else {
            String address = "";
            try {
                //address = addressUtils.getAddresses("ip="+ip, "utf-8");
                address = iPlocService.getAddresses("ip="+ip, "utf-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            cityName = address.substring(0, address.length() - 1);
            System.out.println(cityName);
            newRedisUtil.setValue(ip,cityName);
            System.out.println("把城市信息存入redis中咯！！");

        }

        Map<String,Object> parameters = query.getParameters();
        parameters.put("city", cityName);


        flightService.queryTakeoffOrLandingTimeNear(query);
        message = "查询成功";
        logger.warn(query.getResults());

        //bind the city name with date
        Calendar calendar = Calendar.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append(calendar.get(Calendar.YEAR));
        sb.append(calendar.get(Calendar.MONTH));
        sb.append(calendar.get(Calendar.DAY_OF_MONTH));

        System.out.println("日期" + sb.toString());

        WeatherInf weatherInf = new WeatherInf();
        WeatherInf weatherInf1 = new WeatherInf();
        weatherInf = weaService.getWeather(cityName,sb.toString());
//        if(weatherInf1.isUseCache()!=weatherInf.isUseCache())
//        {
//            System.out.println("使用cache啦！！！！！！！！！");
//        }
//        else
//        {
//            System.out.println("没用到cache....");
//        }

        OneDayWeatherInf[] one = weatherInf.getWeatherInfs();
        Map<String,String> weather = new HashMap<>();
        for(OneDayWeatherInf oneday:one){
            weather.put(ActionUtil.getSameTypeDate(oneday.getDate()), oneday.getWeather());
            //System.out.println(ActionUtil.getSameTypeDate(oneday.getDate()) + " " + oneday.getWeather());
        }
//        Set<String> dates = weather.keySet();
//        for(String date:dates){
//            System.out.print(date);
//            System.out.println(weather.get(date));
//        }

        List<? extends  Object> list = query.getResults();
        int length = list.size();
        view_Flight_Plane[] flightInfo = new view_Flight_Plane[length];

        for(int i = 0;i < length;++i){
            flightInfo[i] = (view_Flight_Plane)list.get(i);
        }


        for(view_Flight_Plane sub:flightInfo){
            String date = sub.getDate().toString();

            if(weather.containsKey(date)){
                String onedayWeather = weather.get(date);

                Pattern pattern1 = Pattern.compile("[晴]");
                Pattern pattern2 = Pattern.compile("[云]");
                Pattern pattern3 = Pattern.compile("[阴]");
                Pattern pattern4 = Pattern.compile("[雨]");
                Matcher matcher1 = pattern1.matcher(onedayWeather);
                Matcher matcher2 = pattern2.matcher(onedayWeather);
                Matcher matcher3 = pattern3.matcher(onedayWeather);
                Matcher matcher4 = pattern4.matcher(onedayWeather);
                if(matcher1.find()){
                    System.out.println("晴 "+date+onedayWeather);
                    Float score= sub.getScore() + 10;
                    sub.setScore(score);
                    sub.setWeather(onedayWeather);
                }
                else if(matcher2.find()){
                    System.out.println("云 "+date+onedayWeather);
                    Float score= sub.getScore() + 8;
                    sub.setScore(score);
                    sub.setWeather(onedayWeather);
                }
                else if(matcher3.find()){
                    System.out.println("阴 " + date + onedayWeather);
                    Float score= sub.getScore() + 7;
                    sub.setScore(score);
                    sub.setWeather(onedayWeather);
                }
                else if(matcher4.find()){
                    System.out.println("雨 "+date+onedayWeather);
                    Float score= sub.getScore() + 6;
                    sub.setScore(score);
                    sub.setWeather(onedayWeather);
                }
                else{
                    System.out.println("其他 "+date+onedayWeather);
                    Float score= sub.getScore() + 5;
                    sub.setScore(score);
                    sub.setWeather("暂无天气信息");
                }
            }
            else {
                Float score= sub.getScore() + 5;
                sub.setScore(score);
            }
        }

        return new Response(Status.SUCCESS,message,query.getResults(),query.getTotalRow());
    }


}

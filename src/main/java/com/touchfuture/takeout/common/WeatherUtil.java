//package com.touchfuture.takeout.common;
//
//import com.touchfuture.takeout.bean.OneDayWeatherInf;
//import com.touchfuture.takeout.bean.WeatherInf;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//import org.springframework.cache.annotation.Cacheable;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.URL;
//import java.net.URLConnection;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
///**
// * Created by user on 2016/11/27.
// */
//public class WeatherUtil {
//
//    Log logger = LogFactory.getLog(WeatherUtil.class);
//
//
//    private String location;
//
//
//    public String getLocation() {
//        return location;
//    }
//
//    public void setLocation(String location) {
//        this.location = location;
//    }
//
//    public String getWeatherInfor(String link) throws Exception {
//        String weather;
//        URL url = new URL(link);
//        URLConnection connection = url.openConnection();
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
//        StringBuffer stringBuffer = new StringBuffer();
//        String line = null;
//        while ((line = bufferedReader.readLine()) != null) {
//            stringBuffer.append(line);
//        }
//        bufferedReader.close();
//        weather = stringBuffer.toString();
//        return weather;
//    }
//
//    /**
//     * 对获取的天气json数据进行解析
//     * @param strPar
//     * @return
//     */
//    public WeatherInf resolveWeatherInf(String strPar) {
//
//        JSONObject dataOfJson = JSONObject.fromObject(strPar);
//
//        if (dataOfJson.getInt("error") != 0) {
//            return null;
//        }
//
//        //保存全部的天气信息。
//        WeatherInf weatherInf = new WeatherInf();
//
//        //从json数据中取得的时间�?
//        String date = dataOfJson.getString("date");
//        int year = Integer.parseInt(date.substring(0, 4));
//        int month = Integer.parseInt(date.substring(5, 7));
//        int day = Integer.parseInt(date.substring(8, 10));
//        Date today = new Date(year - 1900, month - 1, day);
//
//        JSONArray results = dataOfJson.getJSONArray("results");
//        JSONObject results0 = results.getJSONObject(0);
//
//        String location = results0.getString("currentCity");
//        int pmTwoPointFive;
//
//        if (results0.getString("pm25").isEmpty()) {
//            pmTwoPointFive = 0;
//        } else {
//            pmTwoPointFive = results0.getInt("pm25");
//        }
//        //System.out.println(results0.get("pm25").toString()+"11");
//
//        try {
//
//            JSONArray index = results0.getJSONArray("index");
//
//            JSONObject index0 = index.getJSONObject(0);//穿衣
//            JSONObject index1 = index.getJSONObject(1);//洗车
//            JSONObject index2 = index.getJSONObject(2);//感冒
//            JSONObject index3 = index.getJSONObject(3);//运动
//            JSONObject index4 = index.getJSONObject(4);//紫外线强度
//
//
//            String dressAdvise = index0.getString("des");//穿衣建议
//            String washCarAdvise = index1.getString("des");//洗车建议
//            String coldAdvise = index2.getString("des");//感冒建议
//            String sportsAdvise = index3.getString("des");//运动建议
//            String ultravioletRaysAdvise = index4.getString("des");//紫外线建议
//
//            weatherInf.setDressAdvise(dressAdvise);
//            weatherInf.setWashCarAdvise(washCarAdvise);
//            weatherInf.setColdAdvise(coldAdvise);
//            weatherInf.setSportsAdvise(sportsAdvise);
//            weatherInf.setUltravioletRaysAdvise(ultravioletRaysAdvise);
//
//        } catch (Exception jsonExp) {
//
//            weatherInf.setDressAdvise("要温度，也要风度。天热缓减衣，天凉及添衣！");
//            weatherInf.setWashCarAdvise("你洗还是不洗，灰尘都在哪里，不增不减。");
//            weatherInf.setColdAdvise("一天一个苹果，感冒不来找我！多吃水果和蔬菜。");
//            weatherInf.setSportsAdvise("生命在于运动！不要总宅在家里哦！");
//            weatherInf.setUltravioletRaysAdvise("心灵可以永远年轻，皮肤也一样可以！");
//        }
//
//        JSONArray weather_data = results0.getJSONArray("weather_data");//weather_data中有四项�?
//
//        //OneDayWeatherInf是自己定义的承载某一天的天气信息的实体类，详细定义见后面。
//        OneDayWeatherInf[] oneDayWeatherInfS = new OneDayWeatherInf[4];
//        for (int i = 0; i < 4; i++) {
//            oneDayWeatherInfS[i] = new OneDayWeatherInf();
//        }
//
//        for (int i = 0; i < weather_data.size(); i++) {
//
//            JSONObject OneDayWeatherinfo = weather_data.getJSONObject(i);
//            String dayData = OneDayWeatherinfo.getString("date");
//            OneDayWeatherInf oneDayWeatherInf = new OneDayWeatherInf();
//
//            if(today.getDate()>=10)
//              oneDayWeatherInf.setDate((today.getYear() + 1900) + "-" + (today.getMonth() + 1) + "-" + today.getDate());
//            else
//                oneDayWeatherInf.setDate((today.getYear() + 1900) + "-" + (today.getMonth() + 1) + "-0" + today.getDate());
//            today.setDate(today.getDate() + 1);//增加一天
//
//            oneDayWeatherInf.setLocation(location);
//            oneDayWeatherInf.setPmTwoPointFive(pmTwoPointFive);
//
//            if (i == 0) {//第一个，也就是当天的天气，在date字段中最后包含了实时天气
//                int beginIndex = dayData.indexOf("：");
//                int endIndex = dayData.indexOf(")");
//                if (beginIndex > -1) {
//                    oneDayWeatherInf.setTempertureNow(dayData.substring(beginIndex + 1, endIndex));
//                    oneDayWeatherInf.setWeek(OneDayWeatherinfo.getString("date").substring(0, 2));
//                } else {
//                    oneDayWeatherInf.setTempertureNow(" ");
//                    oneDayWeatherInf.setWeek(OneDayWeatherinfo.getString("date").substring(0, 2));
//                }
//
//            } else {
//                oneDayWeatherInf.setWeek(OneDayWeatherinfo.getString("date"));
//            }
//
//            oneDayWeatherInf.setTempertureOfDay(OneDayWeatherinfo.getString("temperature"));
////            oneDayWeatherInf.setWeather(<span style="font-family: Arial, Helvetica, sans-serif;">OneDayWeatherinfo.getString("weather")</span><span style="font-family: Arial, Helvetica, sans-serif;">);</span>
////                    oneDayWeatherInf.setWeather(weather);
//            oneDayWeatherInf.setWind(OneDayWeatherinfo.getString("wind"));
//            oneDayWeatherInf.setWeather(OneDayWeatherinfo.getString("weather"));
//
//            oneDayWeatherInfS[i] = oneDayWeatherInf;
//        }
//
//        weatherInf.setWeatherInfs(oneDayWeatherInfS);
//
//        return weatherInf;
//    }
//
//    public void locationTrans(String originLocation){
//        System.out.println(originLocation);
//        if(originLocation.equals("成都") ){
//            this.location = "shuangliu";
//        }
//        else if(originLocation.equals("北京")){
//            this.location = "beijing";
//        }
//        else if(originLocation.equals("厦门")){
//            this.location = "xiamen";
//        }
//        else if(originLocation.equals("广州")){
//            this.location = "guangzhou";
//        }
//        else if(originLocation.equals("深圳")){
//            this.location = "shenzhen";
//        }
//        else if(originLocation.equals("上海")){
//            this.location = "shanghai";
//        }
//        else if(originLocation.equals("大连")){
//            this.location = "dalian";
//        }
//        else if(originLocation.equals("西安")){
//            this.location = "xian";
//        }
//        else if(originLocation.equals("重庆")){
//            this.location = "shuangliu";
//        }
//        else if(originLocation.equals("南京")){
//            this.location = "nanjing";
//        }
//        else if(originLocation.equals("乌鲁木齐")){
//            this.location = "wulumuqi";
//        }
//        else if(originLocation.equals("杭州")){
//            this.location = "hangzhou";
//        }
//        else if(originLocation.equals("昆明")){
//            this.location = "kunming";
//        }
//        else if(originLocation.equals("长沙")){
//            this.location = "changsha";
//        }
//        else if(originLocation.equals("海口")){
//            this.location = "haikou";
//        }
//        else {
//            this.location = "zhangzhou";
//        }
//
//    }
//
//    @Cacheable(value = "WeatherCache")
//    public WeatherInf getWeather(String originLocation) throws Exception{
//
//        logger.warn("从api获取天气信息");
//
//        WeatherUtil weatherUtil = new WeatherUtil();
//        weatherUtil.locationTrans(originLocation);
//        System.out.println(weatherUtil.location);
//        String link = "http://api.map.baidu.com/telematics/v3/weather?location="+weatherUtil.location+"&output=json&ak=ryENpSxUWnSIi3LjTwG2Pfyp3HBnS1Gv";
//        String weather = weatherUtil.getWeatherInfor(link);
//        System.out.print(weather);
//        WeatherInf weatherInf = new WeatherInf();
//        weatherInf = weatherUtil.resolveWeatherInf(weather);
//        //System.out.println('\n');
//        return weatherInf;
//    }
//
//}

package com.touchfuture.takeout.service;

import com.touchfuture.takeout.bean.WeatherInf;

/**
 * Created by user on 2016/12/17.
 */
public interface WeatherService {
     WeatherInf getWeather(String originLocation,String nowDate)throws Exception;
}

package com.example.falling.weatherapp;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by falling on 16/2/29.
 */
public class WeatherThread extends Thread {

    public static final String SUCCESS = "success";
    private Weather mWeather;
    private Context mContext;
    private Gson mGson;
    private WeatherDatabaseUtil mWeatherDatabaseUtil;
    public WeatherThread(Context context){
        this.mContext = context;

        mGson = new Gson();
        mWeather = new Weather();
        mWeatherDatabaseUtil = new WeatherDatabaseUtil(context);
    }

    @Override
    public void run() {
        WeatherBean weatherBean = mGson.fromJson(mWeather.getWeather(), WeatherBean.class);

        if(TextUtils.equals(weatherBean.getErrMsg(), SUCCESS)){
            //insert into database;
            mWeatherDatabaseUtil.insert(weatherBean);

        }


    }

}

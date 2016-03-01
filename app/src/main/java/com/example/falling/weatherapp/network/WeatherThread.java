package com.example.falling.weatherapp.network;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.falling.weatherapp.bean.WeatherBean;
import com.example.falling.weatherapp.database.WeatherDatabaseUtil;
import com.example.falling.weatherapp.network.Weather;
import com.google.gson.Gson;

import javax.security.auth.login.LoginException;

/**
 * Created by falling on 16/2/29.
 */
public class WeatherThread extends Thread {

    public static final String SUCCESS = "success";
    private Weather mWeather;
    private Gson mGson;
    private WeatherDatabaseUtil mWeatherDatabaseUtil;
    public WeatherThread(Context context){
        mGson = new Gson();
        mWeather = new Weather();
        mWeatherDatabaseUtil = new WeatherDatabaseUtil(context);
    }

    @Override
    public void run() {
        while(true) {
            WeatherBean weatherBean = mGson.fromJson(mWeather.getWeather(), WeatherBean.class);

            Log.i("Tag","获取一次");
            if (TextUtils.equals(weatherBean.getErrMsg(), SUCCESS)) {
                //insert into database;
                mWeatherDatabaseUtil.insert(weatherBean);
            }

            synchronized(this) {
                try {
                    //每隔5秒（示例作用，实际可改为1小时）获取一次网络信息写入数据库。
                    //可通过按钮来唤醒，提前获取数据
                    this.wait(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


    }

}

package com.example.falling.weatherapp.network;

import android.text.TextUtils;
import android.util.Log;

import com.example.falling.weatherapp.MainActivity;
import com.example.falling.weatherapp.bean.WeatherBean;
import com.example.falling.weatherapp.database.WeatherDatabaseUtil;
import com.google.gson.Gson;

/**
 * Created by falling on 16/2/29.
 */
public class WeatherThread extends Thread {

    public static final String SUCCESS = "success";
    private Weather mWeather;
    private Gson mGson;
    private WeatherDatabaseUtil mWeatherDatabaseUtil;
    private MainActivity mActivity;

    public WeatherThread(MainActivity activity) {
        this.mActivity = activity;
        mGson = new Gson();
        mWeather = new Weather();
        mWeatherDatabaseUtil = new WeatherDatabaseUtil(activity);
    }

    @Override
    public void run() {
        while (true) {
            WeatherBean weatherBean = mGson.fromJson(mWeather.getWeather(), WeatherBean.class);
            Log.i("Tag", "获取一次");

            if (weatherBean!=null && TextUtils.equals(weatherBean.getErrMsg(), SUCCESS)) {
                //获取数据成功
                //insert into database;
                mWeatherDatabaseUtil.insert(weatherBean);
                mActivity.getShowTask().cancel(true);
                mActivity.reSetShowTask().getShowTask().execute();

            }

            synchronized (this) {
                try {
                    //每隔5秒（示例作用，实际可改为1小时）获取一次网络信息写入数据库。
                    //因为是用wait，所以实际获取时间超过5秒
                    //可通过按钮来唤醒，提前获取数据
                    this.wait(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


    }

}

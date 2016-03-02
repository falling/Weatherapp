package com.example.falling.weatherapp.network;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.example.falling.weatherapp.MainActivity;
import com.example.falling.weatherapp.R;
import com.example.falling.weatherapp.bean.WeatherBean;
import com.example.falling.weatherapp.database.WeatherDatabaseUtil;
import com.google.gson.Gson;

import java.util.Calendar;

/**
 * Created by falling on 16/2/29.
 */
public class WeatherThread extends Thread {

    public static final String SUCCESS = "success";
    public static final int MODE_PRIVATE = 0;
    public static final String LAST_UPDATE_TIME = "last_update_time";
    private WeatherRequest mWeatherRequest;
    private Gson mGson;
    private WeatherDatabaseUtil mWeatherDatabaseUtil;
    private MainActivity mActivity;
    private WeatherBean mWeatherBean;

    public WeatherThread(MainActivity activity) {
        this.mActivity = activity;
        mGson = new Gson();
        mWeatherRequest = new WeatherRequest();
        mWeatherDatabaseUtil = new WeatherDatabaseUtil(activity);
    }

    @Override
    public void run() {
        while (true) {

            if (Internet.isNetworkConnected(mActivity)) {
                mWeatherBean = mGson.fromJson(mWeatherRequest.getWeather(), WeatherBean.class);
                if (mWeatherBean != null && TextUtils.equals(mWeatherBean.getErrMsg(), SUCCESS)) {
                    //获取数据成功
                    //insert into database;
                    Log.i("Tag", "获取一次");
                    mWeatherDatabaseUtil.insert(mWeatherBean);
                    //写入更新的时间到SharedPreference
                    insertTimeIntoSharedPreference();

                    mActivity.getShowTask().cancel(true);
                    mActivity.reSetShowTask().getShowTask().execute();
                } else {
                    sendMessageToMainActivity(mActivity.getString(R.string.error_get_info_failed));
                }
            } else {
                sendMessageToMainActivity(mActivity.getString(R.string.error_network_connect));
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

    private void sendMessageToMainActivity(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.MESSAGE, str);
        Message msg = mActivity.getMessageHandler().obtainMessage();
        msg.what = MainActivity.NET_THREAD_MESSAGE;
        msg.setData(bundle);
        mActivity.getMessageHandler().sendMessage(msg);
    }

    private void insertTimeIntoSharedPreference() {
        SharedPreferences sharedPreferences = mActivity.getSharedPreferences("time", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        String update_time = year + "/" + (month + 1) + "/" + date + "  " +
                hour + ":" + minute + ":" + second;
        editor.putString(LAST_UPDATE_TIME, update_time);
        editor.commit();
    }

}

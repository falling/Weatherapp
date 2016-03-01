package com.example.falling.weatherapp;

import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by falling on 16/2/29.
 */
public class WeatherThread extends Thread {

    private Weather mWeather;


    @Override
    public void run() {
        mWeather = new Weather();
        Gson gson = new Gson();
        WeatherBean weatherBean = gson.fromJson(mWeather.getWeather(), WeatherBean.class);
        System.out.println();

    }

}

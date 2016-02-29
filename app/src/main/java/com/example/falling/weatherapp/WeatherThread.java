package com.example.falling.weatherapp;

import android.util.Log;

/**
 * Created by falling on 16/2/29.
 */
public class WeatherThread extends Thread {

    private Weather mWeather;


    @Override
    public void run() {
        mWeather = new Weather();
        System.out.print(mWeather.getWeather());

    }

}

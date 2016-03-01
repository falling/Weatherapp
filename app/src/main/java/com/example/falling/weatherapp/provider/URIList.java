package com.example.falling.weatherapp.provider;

import com.example.falling.weatherapp.database.WeatherDatabase;

/**
 * Created by falling on 16/3/1.
 */
public class URIList {
    public static final String CONTENT = "content://";
    public static final String AUTHORITY = "com.example.falling.weatherapp";

    public static final String WEATHER_URI = CONTENT + AUTHORITY + "/" + WeatherDatabase.TABLE_NAME_WEATHER;
}

package com.example.falling.weatherapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by falling on 16/2/29.
 */
public class WeatherDatabase extends SQLiteOpenHelper {


    public static final String WEATHER = "Weather";
    public static final int VERSION = 1;
    public static final String TABLE_NAME_WEATHER = "weatherData";

    public WeatherDatabase(Context context) {
        super(context, WEATHER, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME_WEATHER + " (id PRIMARY KEY ON CONFLICT REPLACE,city,date,week,curTemp,aqi,fengXiang,fengLi,highTemp,lowTemp,type)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

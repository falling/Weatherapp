package com.example.falling.weatherapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by falling on 16/3/1.
 */
public class WeatherDatabaseUtil {
    private Context mContext;

    public WeatherDatabaseUtil(Context context) {
        mContext = context;
    }

    public void insert(WeatherBean weatherBean) {
        WeatherDatabase weatherDatabase = new WeatherDatabase(mContext);
        SQLiteDatabase db = weatherDatabase.getWritableDatabase();
        db.execSQL(weatherBean.getInsertSql());
    }
}

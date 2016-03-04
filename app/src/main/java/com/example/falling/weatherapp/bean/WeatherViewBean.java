package com.example.falling.weatherapp.bean;

import android.database.Cursor;
import android.text.TextUtils;

/**
 * Created by falling on 16/3/2.
 * 这个是数据库存储里对应的bean。用于方便显示数据。
 */

public class WeatherViewBean {
    private String date;
    private String week;
    private String curTemp;
    private String aqi;
    private String fengXiang;
    private String fengLi;
    private String highTemp;
    private String lowTemp;
    private String type;


    public WeatherViewBean(Cursor cursor) {
        this.setDate(cursor.getString(2));
        this.setWeek(cursor.getString(3));
        this.setCurTemp(cursor.getString(4));
        this.setAqi(cursor.getString(5));
        this.setFengXiang(cursor.getString(6));
        this.setFengLi(cursor.getString(7));
        this.setHighTemp(cursor.getString(8));
        this.setLowTemp(cursor.getString(9));
        this.setType(cursor.getString(10));
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getCurTemp() {
        return curTemp;
    }

    public void setCurTemp(String curTemp) {
        this.curTemp = curTemp;
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getFengXiang() {
        return fengXiang;
    }

    public void setFengXiang(String fengXiang) {
        this.fengXiang = fengXiang;
    }

    public String getFengLi() {
        return fengLi;
    }

    public void setFengLi(String fengLi) {
        this.fengLi = fengLi;
    }

    public String getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(String highTemp) {
        this.highTemp = highTemp;
    }

    public String getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(String lowTemp) {
        this.lowTemp = lowTemp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        String result;
        //说明是未来几天的预报
        if (TextUtils.equals(curTemp, "")) {
            result = week + "\n" +
                    type + "\n" +
                    lowTemp + " ~ " + highTemp + "\n" +
                    fengXiang + " " + fengLi + "\n";
        } else {
            result = date + " " + week + "\n" +
                    type + "\n" +
                    "当前温度:" + curTemp + "\n" +
                    lowTemp + " ~ " + highTemp + "\n" +
                    "PM2.5：" + aqi + "\n" +
                    fengXiang + " " + fengLi + "\n";
        }
        return result;
    }
}

package com.example.falling.weatherapp.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by falling on 16/2/29.
 */
public class WeatherBean {
    private String errMsg;
    private RetData retData;

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public RetData getRetData() {
        return retData;
    }

    public void setRetData(RetData retData) {
        this.retData = retData;
    }

    public static class RetData {
        private String city;
        private Today today;
        private List<Forecast> forecast = new ArrayList<>();

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public Today getToday() {
            return today;
        }

        public void setToday(Today today) {
            this.today = today;
        }

        public List<Forecast> getForecast() {
            return forecast;
        }

        public void setForecast(List<Forecast> forecast) {
            this.forecast = forecast;
        }
    }

    public static class Today {
        private String date;//日期
        private String week;//周几
        private String curTemp;//当前温度
        private String aqi;//pm2.5
        private String fengxiang;//风向
        private String fengli;
        private String hightemp;
        private String lowtemp;
        private String type;//晴、雨、阴 等

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

        public String getFengxiang() {
            return fengxiang;
        }

        public void setFengxiang(String fengxiang) {
            this.fengxiang = fengxiang;
        }

        public String getFengli() {
            return fengli;
        }

        public void setFengli(String fengli) {
            this.fengli = fengli;
        }

        public String getHightemp() {
            return hightemp;
        }

        public void setHightemp(String hightemp) {
            this.hightemp = hightemp;
        }

        public String getLowtemp() {
            return lowtemp;
        }

        public void setLowtemp(String lowtemp) {
            this.lowtemp = lowtemp;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static  class Forecast{
        private String date;
        private String week;
        private String fengxiang;
        private String fengli;
        private String hightemp;
        private String lowtemp;
        private String type;

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

        public String getFengxiang() {
            return fengxiang;
        }

        public void setFengxiang(String fengxiang) {
            this.fengxiang = fengxiang;
        }

        public String getFengli() {
            return fengli;
        }

        public void setFengli(String fengli) {
            this.fengli = fengli;
        }

        public String getHightemp() {
            return hightemp;
        }

        public void setHightemp(String hightemp) {
            this.hightemp = hightemp;
        }

        public String getLowtemp() {
            return lowtemp;
        }

        public void setLowtemp(String lowtemp) {
            this.lowtemp = lowtemp;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public String getInsertSql(){
        int i  =1;
        //本日的天气信息
        String sql = "insert into weatherData " +
                "select "+
                i+" ,"+
                "'"+retData.city +"',"+
                "'"+retData.today.date+"',"+
                "'"+retData.today.week+"',"+
                "'"+retData.today.curTemp+"',"+
                "'"+retData.today.aqi+" ',"+
                "'"+retData.today.fengxiang+"',"+
                "'"+retData.today.fengli+"',"+
                "'"+retData.today.hightemp+"',"+
                "'"+retData.today.lowtemp+"', "+
                "'"+retData.today.type+"' ";

        //未来几天的天气预报信息
        i++;
        for(Forecast forecast:retData.forecast){
            sql += "union all select "+i+","+
                    "'"+retData.city+"',"+
                    "'"+forecast.date+"',"+
                    "'"+forecast.week+"',"+
                    "''"+","+
                    "''"+","+
                    "'"+forecast.fengxiang+"',"+
                    "'"+forecast.fengli+"',"+
                    "'"+forecast.hightemp+"',"+
                    "'"+forecast.lowtemp+"',"+
                    "'"+forecast.type+"' ";
            i++;
        }

        return sql;
    }

}

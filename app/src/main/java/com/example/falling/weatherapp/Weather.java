package com.example.falling.weatherapp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by falling on 16/2/29.
 */
public class Weather {

    public static final int TIMEOUT_MILLIS = 8000;
    public static final String APIKEY = "6ec6a78c57e809cb5bb79c2f2b9c5bab";

    public String getWeather() {
        String httpUrl = "http://apis.baidu.com/apistore/weatherservice/recentweathers";
        String httpArg = "cityid=101210101";
        String jsonResult = request(httpUrl, httpArg);
        System.out.println(jsonResult);
        return jsonResult;
    }

    public static String request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(TIMEOUT_MILLIS);
            // 填入apikey到HTTP header
            connection.setRequestProperty("apikey", APIKEY);
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}

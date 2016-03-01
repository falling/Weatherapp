package com.example.falling.weatherapp;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.falling.weatherapp.network.Weather;
import com.example.falling.weatherapp.network.WeatherThread;
import com.example.falling.weatherapp.provider.URIList;

public class MainActivity extends AppCompatActivity {
    private TextView mTextView;
    private Button mButton;
    private WeatherThread mWeatherThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.View_weather);
        mButton = (Button) findViewById(R.id.getWeather_Button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.getWeather_Button){
                    //立即获取数据
                    synchronized(mWeatherThread) {
                        mWeatherThread.notifyAll();
                    }

                }
            }
        });

        //开启获取天气信息的线程
        mWeatherThread = new WeatherThread(this);
        mWeatherThread.start();


        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(Uri.parse(URIList.WEATHER_URI), null, null, null, null, null);
        String weather = "";
        while (cursor != null && cursor.moveToNext()) {
            for (int i = 2; i < 11; i++) {
                weather += cursor.getString(i);
            }
            weather +="\n";
        }
        mTextView.setText(weather);
        if (cursor != null)
            cursor.close();

    }

}

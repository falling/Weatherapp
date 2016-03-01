package com.example.falling.weatherapp;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.falling.weatherapp.network.WeatherThread;
import com.example.falling.weatherapp.provider.URIList;

public class MainActivity extends AppCompatActivity {
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.View_weather);
        new WeatherThread(this).start();


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

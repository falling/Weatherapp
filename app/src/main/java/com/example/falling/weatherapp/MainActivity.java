package com.example.falling.weatherapp;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.falling.weatherapp.network.WeatherThread;
import com.example.falling.weatherapp.provider.URIList;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    public final static int MESSAGE_CODE = 1000;
    private TextView mTextView;
    private weatherHandler mWeatherHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.text);
        mWeatherHandler = new weatherHandler(this);
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


    public weatherHandler getWeatherHandler() {
        return mWeatherHandler;
    }

    private static class weatherHandler extends Handler {
        public final WeakReference<MainActivity> mMainActivityWeakReference;

        public weatherHandler(MainActivity view) {
            mMainActivityWeakReference = new WeakReference<MainActivity>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            MainActivity mainActivity = mMainActivityWeakReference.get();
            switch (msg.what) {
                case MESSAGE_CODE:
                    mainActivity.mTextView.setText(msg.getData().getString("Json"));
                    break;
            }
        }
    }
}

package com.example.falling.weatherapp;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

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
        new WeatherThread().start();
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

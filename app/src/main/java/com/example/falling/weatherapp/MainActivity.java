package com.example.falling.weatherapp;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.falling.weatherapp.bean.WeatherViewBean;
import com.example.falling.weatherapp.network.InternetUtil;
import com.example.falling.weatherapp.network.WeatherThread;
import com.example.falling.weatherapp.provider.URIList;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    public static final int NET_THREAD_MESSAGE = 1000;
    public static final String MESSAGE = "message";
    private final WeatherThread mWeatherThread = new WeatherThread(this);
    private TextView mTodayWeather;
    private TextView mF1Weather;
    private TextView mF2Weather;
    private TextView mF3Weather;
    private TextView mF4Weather;
    private TextView mUpdateTime;
    private Button mButton;
    private showTask mShowTask = new showTask();
    private MessageHandler mMessageHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //显示数据库的数据
        mShowTask.execute();
        //开启获取天气信息的线程
        mWeatherThread.start();
        mMessageHandler = new MessageHandler(this);

        findId();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.getWeather_Button) {
                    //如果有网络 则立即获取数据
                    if (InternetUtil.isNetworkConnected(v.getContext())) {
                        synchronized (mWeatherThread) {
                            mWeatherThread.notifyAll();
                        }
                        Toast.makeText(v.getContext(), R.string.getting_update, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(v.getContext(), getString(R.string.error_network_connect), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void findId() {
        mTodayWeather = (TextView) findViewById(R.id.View_Today_weather);
        mF1Weather = (TextView) findViewById(R.id.View_F1_weather);
        mF2Weather = (TextView) findViewById(R.id.View_F2_weather);
        mF3Weather = (TextView) findViewById(R.id.View_F3_weather);
        mF4Weather = (TextView) findViewById(R.id.View_F4_weather);
        mUpdateTime = (TextView) findViewById(R.id.update_time);
        mButton = (Button) findViewById(R.id.getWeather_Button);
    }

    public MessageHandler getMessageHandler() {
        return mMessageHandler;
    }

    public showTask getShowTask() {
        return mShowTask;
    }

    public MainActivity reSetShowTask() {
        mShowTask = new showTask();
        return this;//方便链式操作
    }

    public class showTask extends AsyncTask<Void, Void, String[]> {

        /**
         * @param params
         * @return result[0] 存的是当天的天气信息。
         * result[1~4]存的是未来几天的信息
         * result[5] 存的是最近更新时间信息
         */
        @Override
        protected String[] doInBackground(Void... params) {
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(Uri.parse(URIList.WEATHER_URI), null, null, null, null, null);
            String[] results = new String[6];
            int i = 0;
            while (cursor != null && cursor.moveToNext()) {
                WeatherViewBean weatherViewBean = new WeatherViewBean(cursor);
                results[i] = weatherViewBean.toString();
                i++;
            }
            if (cursor != null) {
                cursor.close();
            }

            SharedPreferences sharedPreferences = getSharedPreferences("time", MODE_PRIVATE);
            results[5] = "更新于 " + sharedPreferences.getString(WeatherThread.LAST_UPDATE_TIME, getString(R.string.never_get));

            return results;
        }

        @Override
        protected void onPostExecute(String[] s) {
            super.onPostExecute(s);
            Log.i("Tag", "显示一次");

            mTodayWeather.setText(s[0]);
            mF1Weather.setText(s[1]);
            mF2Weather.setText(s[2]);
            mF3Weather.setText(s[3]);
            mF4Weather.setText(s[4]);
            mUpdateTime.setText(s[5]);

        }


    }

    public static class MessageHandler extends Handler {
        public static WeakReference<MainActivity> sMainActivityWeakReference;

        public MessageHandler(MainActivity mainActivity) {
            sMainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity activity = sMainActivityWeakReference.get();
            switch (msg.what) {
                case NET_THREAD_MESSAGE:
                    Toast.makeText(activity, msg.getData().getString(MESSAGE), Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    }

}

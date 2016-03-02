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

import com.example.falling.weatherapp.network.Internet;
import com.example.falling.weatherapp.network.WeatherThread;
import com.example.falling.weatherapp.provider.URIList;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    public static final int NET_THREAD_MESSAGE = 1000;
    public static final String MESSAGE = "message";
    private final WeatherThread mWeatherThread = new WeatherThread(this);
    private TextView mWeather;
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
        mWeather = (TextView) findViewById(R.id.View_weather);
        mUpdateTime = (TextView) findViewById(R.id.update_time);
        mButton = (Button) findViewById(R.id.getWeather_Button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.getWeather_Button) {
                    //如果有网络 则立即获取数据
                    if (Internet.isNetworkConnected(v.getContext())) {
                        synchronized (mWeatherThread) {
                            mWeatherThread.notifyAll();
                        }
                    } else {
                        Toast.makeText(v.getContext(), getString(R.string.error_network_connect), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

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

        @Override
        protected String[] doInBackground(Void... params) {
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(Uri.parse(URIList.WEATHER_URI), null, null, null, null, null);
            String[] results = new String[2];
            results[0] = "";
            while (cursor != null && cursor.moveToNext()) {
                for (int i = 2; i < 11; i++) {
                    results[0] += cursor.getString(i) + " ";
                }
                results[0] += "\n";
            }
            if(cursor!=null){
                cursor.close();
            }

            SharedPreferences sharedPreferences = getSharedPreferences("time", MODE_PRIVATE);
            results[1] = "更新于 "+sharedPreferences.getString(WeatherThread.LAST_UPDATE_TIME, getString(R.string.never_get));

            return results;
        }

        @Override
        protected void onPostExecute(String[] s) {
            super.onPostExecute(s);
            Log.i("Tag", "显示一次");

            mWeather.setText(s[0]);
            mUpdateTime.setText(s[1]);

        }


    }

    public static class MessageHandler extends Handler {
        public static WeakReference<MainActivity> sMainActivityWeakReference;

        public MessageHandler(MainActivity mainActivity) {
            sMainActivityWeakReference = new WeakReference<MainActivity>(mainActivity);
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

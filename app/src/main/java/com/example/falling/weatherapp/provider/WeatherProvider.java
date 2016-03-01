package com.example.falling.weatherapp.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.falling.weatherapp.database.WeatherDatabase;
import com.example.falling.weatherapp.database.WeatherDatabaseUtil;

/**
 * Created by falling on 16/3/1.
 */
public class WeatherProvider extends ContentProvider {

    private static UriMatcher sUriMatcher;
    private WeatherDatabaseUtil mWeatherDatabaseUtil;


    public static final int URI_MATH_WEATHER = 1;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        sUriMatcher.addURI(URIList.AUTHORITY, WeatherDatabase.TABLE_NAME_WEATHER, URI_MATH_WEATHER);
    }
    @Override
    public boolean onCreate() {
        mWeatherDatabaseUtil = new WeatherDatabaseUtil(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String tableName = getTableName(uri);
        if(TextUtils.isEmpty(tableName)){
            return  null;
        }

        return mWeatherDatabaseUtil.queryAll();
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String tableName = getTableName(uri);
        if(TextUtils.isEmpty(tableName)){
            return  null;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private String getTableName(Uri uri){
        int type = sUriMatcher.match(uri);
        String tableName = null;
        switch (type){
            case URI_MATH_WEATHER:
                tableName = WeatherDatabase.TABLE_NAME_WEATHER;
                break;
        }

        return tableName;
    }
}

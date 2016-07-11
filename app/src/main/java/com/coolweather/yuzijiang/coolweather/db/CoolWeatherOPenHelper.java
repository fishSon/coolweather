package com.coolweather.yuzijiang.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 鱼呀鱼呀鱼籽酱 on 2016/7/11.
 */
public class CoolWeatherOPenHelper extends SQLiteOpenHelper {

    private static final String CREATE_PROVINCE = "CREATE TABLE Province (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "province_name TEXT," +
            "province_code TEXT" +
            ")";
    private static final String CREATE_CITY = "CREATE TABLE City (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "city_name TEXT," +
            "city_code TEXT," +
            "province_id INTEGER" +
            ")";
    private static final String CREATE_COUNTY = "CREATE TABLE County (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "county_name TEXT," +
            "county_code TEXT," +
            "city_id INTEGER" +
            ")";


    public CoolWeatherOPenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_PROVINCE);
        sqLiteDatabase.execSQL(CREATE_CITY);
        sqLiteDatabase.execSQL(CREATE_COUNTY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

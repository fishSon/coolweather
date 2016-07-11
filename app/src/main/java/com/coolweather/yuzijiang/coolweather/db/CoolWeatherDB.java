package com.coolweather.yuzijiang.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coolweather.yuzijiang.coolweather.model.City;
import com.coolweather.yuzijiang.coolweather.model.County;
import com.coolweather.yuzijiang.coolweather.model.Province;
import com.coolweather.yuzijiang.coolweather.utils.DBNameUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 鱼呀鱼呀鱼籽酱 on 2016/7/11.
 */
public class CoolWeatherDB {

    /**数据库名
     * */
    public static final String DB_NAME = "cool_weather";

    /**数据库的版本
     * */
    public static final int DB_VERSION = 1;

    private static CoolWeatherDB coolWeatherDB;
    private SQLiteDatabase db;

    /**单例模式, 私有化构造方法
     * */
    private CoolWeatherDB (Context context) {
        CoolWeatherOPenHelper dbOpenHelper = new CoolWeatherOPenHelper(context, DB_NAME, null, DB_VERSION);
        db = dbOpenHelper.getWritableDatabase();
    }

    /**
     * 获取CoolWeatherDB 的实例
     * */
    public synchronized static CoolWeatherDB getInstance (Context context) {
        if (coolWeatherDB == null) {
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    /**
     * 将Province对象存入数据库
     * */
    public void saveProvince (Province province) {
        if (province != null) {
            ContentValues values = new ContentValues();
            values.put(DBNameUtil.PROVINCE_NAME, province.getProvinceName());
            values.put(DBNameUtil.PROVINCE_CODE, province.getProvinceCode());
            db.insert(DBNameUtil.TABLE_PROVINCE, null, values);
        }
    }

    /**
     * 从数据库读取全国所有的省份信息
     * */
    public List<Province> loadProvinces () {

        List <Province> list = new ArrayList<Province>();

        Cursor cursor = db.query(DBNameUtil.TABLE_PROVINCE, null, null, null, null, null, null);

        while (cursor != null && cursor.moveToNext()) {

            Province province = new Province();
            province.setId(cursor.getInt(cursor.getColumnIndex("id")));
            province.setProvinceCode(cursor.getString(cursor.getColumnIndex(DBNameUtil.PROVINCE_CODE)));
            province.setProvinceName(cursor.getString(cursor.getColumnIndex(DBNameUtil.PROVINCE_NAME)));

            list.add(province);
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;

    }

    /**
     * 将City对象存入 数据库
     * */
    public void saveCity (City city) {
        if (city != null) {
            ContentValues values = new ContentValues();
            values.put(DBNameUtil.CITY_NAME, city.getCityName());
            values.put(DBNameUtil.CITY_CODE, city.getCityCode());
            values.put(DBNameUtil.PROVINCE_ID, city.getProvinceId());
            db.insert(DBNameUtil.TABLE_CITY, null, values);
        }
    }

    /**
     * 从数据库读取某一个省份所有城市的信息
     * */
    public List<City> loadCities (int provinceId) {
        List <City> list = new ArrayList<>();

        Cursor cursor = db.query(DBNameUtil.TABLE_CITY, null, DBNameUtil.PROVINCE_ID+"=?", new String [] {String.valueOf(provinceId)}, null, null, null);

        while (cursor != null && cursor.moveToNext()) {

            City city = new City();
            city.setId(cursor.getInt(cursor.getColumnIndex("id")));
            city.setCityCode(cursor.getString(cursor.getColumnIndex(DBNameUtil.CITY_CODE)));
            city.setCityName(cursor.getString(cursor.getColumnIndex(DBNameUtil.CITY_NAME)));
            city.setProvinceId(cursor.getInt(cursor.getColumnIndex(DBNameUtil.PROVINCE_ID)));

        }
        if (cursor != null) {
            cursor.close();
        }

        return list;
    }

    /**
     * 将 County 实例存入数据库
     * */
    public void saveCounty (County county) {
        if (county != null) {
            ContentValues values = new ContentValues();
            values.put (DBNameUtil.CITY_ID, county.getCityId());
            values.put(DBNameUtil.COUNTY_CODE, county.getCountyCode());
            values.put(DBNameUtil.COUNTY_NAME, county.getCountyName());
            db.insert(DBNameUtil.TABLE_COUNTY, null, values);
        }
    }

    /**
     * 从数据库读取某个城市下的县信息
     * */
    public List<County> loadCounties (int cityId) {

        List<County> list = new ArrayList<>();

        Cursor cursor = db.query(DBNameUtil.TABLE_CITY, null, DBNameUtil.CITY_ID+"=?", new String [] {String.valueOf(cityId)}, null, null, null);

        while (cursor != null && cursor.moveToNext()) {
            County county = new County();
            county.setId(cursor.getInt(cursor.getColumnIndex("id")));
            county.setCityId(cursor.getInt(cursor.getColumnIndex(DBNameUtil.CITY_ID)));
            county.setCountyCode(cursor.getString(cursor.getColumnIndex(DBNameUtil.COUNTY_CODE)));
            county.setCountyName(cursor.getString(cursor.getColumnIndex(DBNameUtil.COUNTY_NAME)));

            list.add(county);

        }

        if (cursor != null) {
            cursor.close();
        }

        return list;
    }
}

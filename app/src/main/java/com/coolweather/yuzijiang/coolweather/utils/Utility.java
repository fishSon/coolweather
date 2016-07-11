package com.coolweather.yuzijiang.coolweather.utils;

import android.text.TextUtils;

import com.coolweather.yuzijiang.coolweather.db.CoolWeatherDB;
import com.coolweather.yuzijiang.coolweather.model.City;
import com.coolweather.yuzijiang.coolweather.model.County;
import com.coolweather.yuzijiang.coolweather.model.Province;

/**
 * Created by 鱼呀鱼呀鱼籽酱 on 2016/7/11.
 */
public class Utility {

    /**
     * 解析和处理服务器返回的省级数据
     * 从网络上得到所有的省份记录，存入本地的数据库
     * */
    public synchronized static boolean handleProvincesResponse (CoolWeatherDB coolWeatherDB, String response) {

        if (!TextUtils.isEmpty(response)) {
            String [] allProvinces = response.split(",");

            if (allProvinces != null && allProvinces.length > 0) {
                //返回了至少一个省份
                for (String p : allProvinces) {
                    //每一个p都是一个省份
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);

                    //将解析出来的数据存储在Province类
                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析处理服务器返回的市级数据
     * 从网络上得到市级数据，存入本地数据库
     * */
    public static  boolean handleCitiesResponse (CoolWeatherDB coolWeatherDB, String response, int provinceId) {

        if (!TextUtils.isEmpty(response)) {
            String[] allCities = response.split(",");

            if (allCities != null && allCities.length > 0) {

                for (String p : allCities) {
                    String[] arrar = p.split("\\|");
                    City city = new City();
                    city.setCityCode(arrar[0]);
                    city.setCityName(arrar[1]);
                    city.setProvinceId(provinceId);

                    coolWeatherDB.saveCity(city);

                }
                return true;
            }
        }
        return false;

    }

    /**
     * 解析和处理服务器返回的县级数据
     * 从服务器上得到所有的县级数据，然后存入本地数据库
     * */
    public static boolean handleCountiesResponse (CoolWeatherDB coolWeatherDB, String response, int cityId) {

        if (!TextUtils.isEmpty(response)) {
            String[] allCounties = response.split(",");

            if (allCounties != null && allCounties.length > 0) {
                for (String p : allCounties) {
                    String[] array = p.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);

                    coolWeatherDB.saveCounty(county);

                }
            }
            return true;

        }
        return false;
    }
}

package com.coolweather.yuzijiang.coolweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.yuzijiang.coolweather.R;
import com.coolweather.yuzijiang.coolweather.db.CoolWeatherDB;
import com.coolweather.yuzijiang.coolweather.model.City;
import com.coolweather.yuzijiang.coolweather.model.County;
import com.coolweather.yuzijiang.coolweather.model.Province;
import com.coolweather.yuzijiang.coolweather.utils.HttpCallbackListener;
import com.coolweather.yuzijiang.coolweather.utils.HttpUtil;
import com.coolweather.yuzijiang.coolweather.utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 鱼呀鱼呀鱼籽酱 on 2016/7/11.
 */
public class ChooseAreaActivity extends Activity {

    private static final int LEVEL_PROVINCE = 0;
    private static final int LEVEL_CITY = 1;
    private static final int LEVEL_COUNTY = 2;
    private int currentLevel;

    private ListView listView;
    private TextView tvTitleText;

    private List<String> dataList = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private CoolWeatherDB coolWeatherDB;

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;

    private Province selectedProvince;
    private City selectedCity;
    private County selectedCounty;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);

        listView = (ListView) findViewById(R.id.lv_select);
        tvTitleText = (TextView) findViewById(R.id.tv_title_text);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        //这个coolWeatherDB是自己写的类，可以进行增和查的操作
        coolWeatherDB = CoolWeatherDB.getInstance(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get (index);
                    queryCities ();
                }else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(index);
                    queryCounties ();
                }else {

                }
            }


        });
        queryProvinces ();
    }

    /**
     * 根据传入的代码和类型从服务器上查询省市县数据
     * */
    private void queryFromServer(String cityCode, final String type) {
        String address ;
        if (!TextUtils.isEmpty(cityCode)) {
            address = getString(R.string.net_address) + cityCode + ".html";

        }else {
            address = getString(R.string.net_address) + ".html";
        }
        showProgressDialog ();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false ;
                if ("province".equals(type)) {
                    result = Utility.handleProvincesResponse(coolWeatherDB, response);
                }else if ("city".equals(type)) {
                    result = Utility.handleCitiesResponse(coolWeatherDB, response, selectedProvince.getId());
                }else if ("county".equals(type)) {
                    result = Utility.handleCountiesResponse(coolWeatherDB, response, selectedCity.getId());
                }
                if (result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog ();
                            if ("province".equals(type)) {
                                queryProvinces();
                            }else if ("city".equals(type)) {
                                queryCities();
                            }else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog ();
                        Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 关闭进度条对话框
     * */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * 启动进度条对话框
     * */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }


    /**
     * 查询选中的市内所有的县，优先查询数据库，没有再去网络上查询
     * */
    private void queryCounties() {
        countyList = coolWeatherDB.loadCounties(selectedCity.getId());
        tvTitleText.setText(selectedCity.getCityName());
        currentLevel = LEVEL_COUNTY;
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
        } else {
            queryFromServer (selectedCity.getCityCode(), "county");
        }

    }

    /**
     * 查询选中省内所有的市， 优先查询数据库, 没有再去网络上查询
     * */
    private void queryCities() {
        cityList = coolWeatherDB.loadCities(selectedProvince.getId());
        tvTitleText.setText(selectedProvince.getProvinceName());
        currentLevel = LEVEL_CITY;

        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);

        }else {
            queryFromServer (selectedProvince.getProvinceCode(), "city");
        }
    }

    /**
     * 查询全国所有的省。优先查询本地数据库
     * */
    private void queryProvinces() {
        tvTitleText.setText("中国");
        currentLevel = LEVEL_PROVINCE;

        provinceList = coolWeatherDB.loadProvinces();
        if (provinceList.size() > 0) {
            dataList.clear();

            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
        }else {
            //数据库里面没有数据
            queryFromServer (null, "province");
        }
    }

    @Override
    public void onBackPressed() {
        if (currentLevel == LEVEL_COUNTY) {
            queryCities();
        }else if (currentLevel == LEVEL_CITY) {
            queryProvinces();
        }else {
            finish();
        }
    }
}

package com.coolweather.yuzijiang.coolweather.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.coolweather.yuzijiang.coolweather.R;
import com.coolweather.yuzijiang.coolweather.db.CoolWeatherDB;

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
                    /*selectedProvince = provinceList.get (index);
                    queryCities ();*/
                }else if (currentLevel == LEVEL_CITY) {

                }else {

                }
            }
        });

    }
}

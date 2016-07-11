package com.coolweather.yuzijiang.coolweather.utils;

/**
 * Created by 鱼呀鱼呀鱼籽酱 on 2016/7/11.
 */
public interface HttpCallbackListener {
    void onFinish (String response);
    void onError (Exception e);

}

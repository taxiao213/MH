package com.haxi.mh.network.manager;

import android.text.TextUtils;

import com.haxi.mh.utils.model.LogUtils;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 拦截请求数据，进行输出打印
 * Created by Han on 2017/12/29
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class RequestLoggingInterceptor {

    private static RequestLoggingInterceptor requestLoggingInterceptor = null;

    public RequestLoggingInterceptor() {
        requestLoggingInterceptor = new RequestLoggingInterceptor();
    }

    public static RequestLoggingInterceptor getInstace() {
        if (requestLoggingInterceptor == null) {
            synchronized (RequestLoggingInterceptor.class) {
                if (requestLoggingInterceptor == null) {
                    requestLoggingInterceptor = new RequestLoggingInterceptor();
                }
            }
        }
        return requestLoggingInterceptor;
    }

    /**
     * 请求接口返回数据拦截器
     *
     * @return
     */
    public HttpLoggingInterceptor getHttpLoggingInterceptor() {
        //日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                if (TextUtils.isEmpty(message)) {
                    return;
                }
                String s = message.substring(0, 1);
                if ("{".equals(s) || "[".equals(s)) {
                    //输出日志
                    if (RxRetrofitApp.isDebug()) {
                        LogUtils.e("接口返回数据--->>> " + message);
                    }
                }
            }
        });
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }
}

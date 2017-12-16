package com.haxi.mh.utils.network;

import io.reactivex.Observable;
import retrofit2.Retrofit;

/**
 * http请求基类
 * Created by Han on 2017/12/16
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 */
public abstract class BaseApi {

    protected int connectTime; //连接时间
    protected String baseUrl;   //baseUrl

    protected abstract Observable getObservable(Retrofit retrofit);

    public int getConnectTime() {
        return connectTime;
    }

    public void setConnectTime(int connectTime) {
        this.connectTime = connectTime;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}

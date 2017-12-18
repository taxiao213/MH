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
    /*请求超时时间 默认6秒*/
    private int connectTime = 6;
    /*baseUrl*/
    private String baseUrl;
    /*方法-如果需要缓存必须设置这个参数；不需要不用設置*/
    private String method = "";
    /*设置是否显示加载圈,默认显示*/
    private boolean isShowProgress = true;
    /*设置是否能取消加载圈,默认能取消*/
    private boolean isCancle = false;
    /*设置是否有缓存,默认没有*/
    private boolean isCache = false;
    /*重试次数*/
    private int count = 1;
    /*延迟时间*/
    private long delay = 100;

    protected abstract Observable getObservable(Retrofit retrofit);

    /**
     * 设置连接时间
     *
     * @param connectTime
     */
    public void setConnectTime(int connectTime) {
        this.connectTime = connectTime;
    }

    public int getConnectTime() {
        return connectTime;
    }


    /**
     * 设置baseUrl
     *
     * @param baseUrl
     */
    protected void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    protected String getBaseUrl() {
        return baseUrl;
    }


    /**
     * 设置是否显示加载圈,默认显示
     *
     * @param showProgress
     */
    public void setShowProgress(boolean showProgress) {
        isShowProgress = showProgress;
    }

    public boolean isShowProgress() {
        return isShowProgress;
    }

    /**
     * 设置是否能取消加载圈,默认能取消
     *
     * @param isCancle
     */
    protected void setCache(boolean isCancle) {
        this.isCancle = isCancle;
    }

    public boolean isCache() {
        return isCache;
    }

    /**
     * 设置是否有缓存,默认没有
     *
     * @param isCache
     */
    protected void setCancle(boolean isCache) {
        this.isCache = isCache;
    }

    public boolean isCancle() {
        return isCancle;
    }


    /**
     * 重试次数
     *
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }


    /**
     * 延迟时间
     *
     * @param delay
     */
    public void setDelay(long delay) {
        this.delay = delay;
    }

    public long getDelay() {
        return delay;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}

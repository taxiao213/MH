package com.haxi.mh.network.manager;

import io.reactivex.Observable;
import retrofit2.Retrofit;

/**
 * http请求基类
 * Created by Han on 2017/12/16
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public abstract class BaseApi {
    /*请求超时时间 默认6秒*/
    private int connectTime = 6;
    /*有网情况下的本地缓存时间默认60秒*/
    private int cookieNetWorkTime = 60;
    /*无网络的情况下本地缓存时间默认30天*/
    private int cookieNoNetWorkTime = 24 * 60 * 60 * 30;
    /*baseUrl*/
    private String baseUrl;
    /*方法-如果需要缓存必须设置这个参数；保证唯一,不需要不用設置*/
    private String method = "";
    /*设置是否显示加载框,默认显示*/
    private boolean isShowProgress = true;
    /*设置是否能取消加载框,默认能取消*/
    private boolean isCancle = false;
    /*设置是否有缓存,默认没有*/
    private boolean isCache = false;
    /*重试次数*/
    private int count = 1;
    /*延迟时间*/
    private long delay = 100;

    /**
     * retrofit网络请求
     * @param retrofit
     * @return
     */
    protected abstract Observable getObservable(Retrofit retrofit);

    /**
     * 设置连接时间
     *
     * @param connectTime
     */
    protected void setConnectTime(int connectTime) {
        this.connectTime = connectTime;
    }

    protected int getConnectTime() {
        return connectTime;
    }

    /**
     * 返回 无网情况下的本地缓存时间
     *
     * @return
     */
    protected int getCookieNoNetWorkTime() {
        return cookieNoNetWorkTime;
    }

    protected void setCookieNoNetWorkTime(int cookieNoNetWorkTime) {
        this.cookieNoNetWorkTime = cookieNoNetWorkTime;
    }

    /**
     * 返回 有网情况下的本地缓存时间
     *
     * @return
     */
    protected int getCookieNetWorkTime() {
        return cookieNetWorkTime;
    }

    protected void setCookieNetWorkTime(int cookieNetWorkTime) {
        this.cookieNetWorkTime = cookieNetWorkTime;
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
     * 如果需要缓存，返回url
     *
     * @return
     */
    protected String getUrl() {
        return (baseUrl + method);
    }

    /**
     * 设置是否显示加载框,默认显示
     *
     * @param showProgress
     */
    protected void setShowProgress(boolean showProgress) {
        isShowProgress = showProgress;
    }

    protected boolean isShowProgress() {
        return isShowProgress;
    }

    /**
     * 设置是否能取消加载框,默认能取消
     *
     * @param isCancle
     */
    protected void setCache(boolean isCancle) {
        this.isCancle = isCancle;
    }

    protected boolean isCache() {
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

    protected boolean isCancle() {
        return isCancle;
    }


    /**
     * 重试次数
     *
     * @param count
     */
    protected void setCount(int count) {
        this.count = count;
    }

    protected int getCount() {
        return count;
    }


    /**
     * 延迟时间
     *
     * @param delay
     */
    protected void setDelay(long delay) {
        this.delay = delay;
    }

    protected long getDelay() {
        return delay;
    }

    protected String getMethod() {
        return method;
    }

    protected void setMethod(String method) {
        this.method = method;
    }
}

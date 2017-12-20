package com.haxi.mh.network.manager;

import android.text.TextUtils;

import com.haxi.mh.network.listener.HttpOnNextListener;
import com.haxi.mh.network.listener.HttpOnNextObservableListener;
import com.haxi.mh.utils.model.LogUtils;
import com.haxi.mh.network.exception.ExceptionFunction;
import com.haxi.mh.network.exception.ResulteFunction;
import com.haxi.mh.network.exception.RetryNetworkException;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.lang.ref.SoftReference;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * http交互处理类
 * Created by Han on 2017/12/16
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class HttpsManager {

    private HttpOnNextListener onNextListener;
    /*软引用对象*/
    private SoftReference<HttpOnNextObservableListener> onNextObservableListener;
    /*rxlifecycle2*/
    private RxAppCompatActivity rxAppCompatActivity;


    public HttpsManager(HttpOnNextObservableListener onNextObservableListener, RxAppCompatActivity rxAppCompatActivity) {
        this.onNextObservableListener = new SoftReference(onNextObservableListener);
        this.rxAppCompatActivity = rxAppCompatActivity;
    }

    public HttpsManager(HttpOnNextListener onNextListener, RxAppCompatActivity rxAppCompatActivity) {
        this.onNextListener = onNextListener;
        this.rxAppCompatActivity = rxAppCompatActivity;
    }

    public void doHttpDeal(BaseApi baseApi) {
        Retrofit retrofit = getRetrofit(baseApi.getConnectTime(), baseApi.getBaseUrl());
        requestHttp(baseApi.getObservable(retrofit), baseApi);
    }


    /**
     * 返回Retrofit
     *
     * @param connectTime
     * @param bserUrl
     * @return
     */
    public Retrofit getRetrofit(int connectTime, String bserUrl) {

        /**
         * 创建OKHttpClient对象
         */
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())//http请求拦截器 用来拼装公共参数
                .connectionPool(new ConnectionPool(100, 10, TimeUnit.MINUTES))//连接池
                .connectTimeout(connectTime, TimeUnit.SECONDS)//请求超时时间
                .readTimeout(60, TimeUnit.SECONDS)//读取超时时间
                .writeTimeout(60, TimeUnit.SECONDS);//写入超时时间

        //增加请求和响应拦截器
        if (RxRetrofitApp.isDebug()) {
            builder.addInterceptor(getHttpLoggingInterceptor());
        }

        /**
         * 创建Retrofit对象
         */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(bserUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();
        return retrofit;
    }

    /**
     * 请求数据
     *
     * @param observable
     * @param baseApi
     */
    private void requestHttp(Observable observable, BaseApi baseApi) {
        observable.retryWhen(new RetryNetworkException(baseApi.getCount(), baseApi.getDelay()))
                //异常处理
                .onErrorResumeNext(new ExceptionFunction())
                //Note:手动设置在activity onDestroy的时候取消订阅
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .map(new ResulteFunction())
                //http请求线程
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                //回调在主线程
                .observeOn(AndroidSchedulers.mainThread());
        //回调
        if (null != onNextObservableListener && null != onNextObservableListener.get()) {
            onNextObservableListener.get().onNext(observable, baseApi.getMethod());
        }

        if (null != onNextListener) {
            //显示进度加载框
            ProgressObserver observer = new ProgressObserver(baseApi, onNextListener, rxAppCompatActivity);
            observable.subscribe(observer);
        }

    }


    /**
     * 日志输出
     *
     * @return
     */
    private HttpLoggingInterceptor getHttpLoggingInterceptor() {
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
                    LogUtils.e("HttpsManager--->>>日志输出--->>>" + message);
                }
            }
        });
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }
}

package com.haxi.mh.utils.network;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.lang.ref.SoftReference;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * http交互处理类
 * Created by Han on 2017/12/16
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 */

public class HttpsManager {

    private HttpOnNextListener onNextListener;
    /**
     * 软引用对象
     */
    private SoftReference<HttpOnNextObservableListener> onNextObservableListenerSoftReference;
    private RxAppCompatActivity rxAppCompatActivity;

    public HttpsManager(HttpOnNextObservableListener onNextObservableListener, RxAppCompatActivity rxAppCompatActivity) {
        this.onNextObservableListenerSoftReference = new SoftReference(onNextObservableListener);
        this.rxAppCompatActivity = rxAppCompatActivity;
    }

    public HttpsManager(HttpOnNextListener onNextListener, RxAppCompatActivity rxAppCompatActivity) {
        this.onNextListener = onNextListener;
        this.rxAppCompatActivity = rxAppCompatActivity;
    }

    private void doHttpDeal(BaseApi api){

    }
    public Retrofit getRetrofit(String bserUrl){



        OkHttpClient builder = new OkHttpClient.Builder()
                    .addInterceptor(new LoggingInterceptor())

                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(bserUrl)
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Scheduler))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}

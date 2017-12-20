package com.haxi.mh;

import android.app.Application;
import android.os.Handler;

import com.haxi.mh.network.manager.RxRetrofitApp;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;


/**
 * 1.获取主线程 名称 id
 * 2.获取上下文
 * Created by Han on 2017/12/11
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class MyApplication extends Application {
    //获取上下文
    public static MyApplication mContext;
    //获取主线程 名称
    private static String mThreadName;
    //获取主线程 id
    private static long mTthreadId;
    // 获取到主线程的handler
    private static Handler mMainThreadHandler = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Thread thread = Thread.currentThread();
        mThreadName = thread.getName();
        mTthreadId = thread.getId();
        mMainThreadHandler = new Handler();
        //注册Bugly
        CrashReport.initCrashReport(getApplicationContext(), "d540dcf53e", true);

        //初始化Logger
        Logger.addLogAdapter(new AndroidLogAdapter());
        //        Logger.clearLogAdapters(); //清除log

        //设置后http请求会被拦截并且输出
        RxRetrofitApp.getInstances().setDebug();
    }

    /**
     * 获取上下文
     *
     * @return
     */
    public static MyApplication getMyApplication() {
        return mContext;
    }

    /**
     * 获取主线程 名称
     *
     * @return
     */
    public static String getMainThreadName() {
        return mThreadName;
    }

    /**
     * 获取主线程 id
     *
     * @return
     */
    public static long getMainThreadId() {
        return mTthreadId;
    }

    /**
     * 获取主线程
     * @return
     */
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

}

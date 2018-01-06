package com.haxi.mh;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.multidex.MultiDex;

import com.haxi.mh.network.manager.RxRetrofitApp;
import com.haxi.mh.utils.exception.HandlerException;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;



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

        //捕获异常 与 bugly 不能同时开启
        HandlerException.getInstance().init(mContext);

        //Bugly获取渠道号
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(mContext);
        strategy.setAppChannel(getChannel());
        //注册Bugly 如果您之前使用过Bugly SDK，请将以下这句注释掉
        //CrashReport.initCrashReport(getApplicationContext(), "58aa13feb7", true, strategy);
        Bugly.init(getApplicationContext(), "58aa13feb7", true, strategy);
        //参数1：isManual 用户手动点击检查，非用户点击操作请传false 参数2：isSilence 是否显示弹窗等交互，[true:没有弹窗和toast] [false:有弹窗或toast]
        Beta.checkUpgrade(false,false);

        //初始化Logger
        Logger.addLogAdapter(new AndroidLogAdapter());
        //        Logger.clearLogAdapters(); //清除log

        //设置后http请求会被拦截并且输出
        RxRetrofitApp.getInstances().setDebug();

        //友盟统计
        MobclickAgent.setScenarioType(mContext, MobclickAgent.EScenarioType.E_UM_NORMAL);

        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
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
     *
     * @return
     */
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    /**
     * 获取渠道id
     *
     * @return
     */
    private String getChannel() {
        if (mContext == null) {
            return null;
        }
        String channel = null;
        try {
            ApplicationInfo info = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
            if (info != null && info.metaData != null) {
                channel = info.metaData.getString("UMENG_CHANNEL");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channel;
    }
}

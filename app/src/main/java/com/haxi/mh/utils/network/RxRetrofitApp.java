package com.haxi.mh.utils.network;

/**
 * retrofit调控初始化
 * Created by Han on 2017/12/16
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 */
public class RxRetrofitApp {

    private static boolean Debug = false;//是否调试默认是false
    private static RxRetrofitApp retrofitApp = null;

    private RxRetrofitApp() {
        Debug = true;
    }

    public static RxRetrofitApp getInstances() {
        if (retrofitApp == null) {
            synchronized (RxRetrofitApp.class) {
                if (retrofitApp == null) {
                    retrofitApp = new RxRetrofitApp();
                }
            }
        }
        return retrofitApp;
    }


    public static boolean isDebug() {
        return Debug;
    }

    public void setDebug() {
        Debug = true;
    }

}

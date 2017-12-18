package com.haxi.mh.utils.network;


import io.reactivex.Observable;

/**
 * 回调Observable对象
 * Created by Han on 2017/12/16
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 */

public interface HttpOnNextObservableListener {

    /**
     * ober成功回调
     *
     * @param observable
     * @param method
     */
    void onNext(Observable observable, String method);
}

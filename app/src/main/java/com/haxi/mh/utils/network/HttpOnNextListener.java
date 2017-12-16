package com.haxi.mh.utils.network;


import com.haxi.mh.utils.network.exception.ApiException;

/**
 * 成功回调处理
 * Created by Han on 2017/12/11
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 */
public interface HttpOnNextListener {
    /**
     * 成功后回调方法
     * @param resulte
     * @param method
     */
   void onNext(String resulte, String method);

    /**
     * 失败
     * 失败或者错误方法
     * 自定义异常处理
     * @param e
     */
    void onError(ApiException e);
}

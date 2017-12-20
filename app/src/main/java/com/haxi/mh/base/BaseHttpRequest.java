package com.haxi.mh.base;

import com.haxi.mh.network.HttpRequestApi;
import com.haxi.mh.network.exception.ApiException;
import com.haxi.mh.network.listener.HttpOnNextListener;
import com.haxi.mh.network.manager.HttpsManager;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

/**
 * 网络请求基类
 * Created by Han on 2017/12/19
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public abstract class BaseHttpRequest<T> implements HttpOnNextListener {

    private HttpsManager manager = null;
    protected Map<String, Object> mParams;
    protected Consumer<T> consumerNext;//一个参数
    protected Consumer<ApiException> consumerError;//一个参数
    protected BiConsumer<T, ApiException> functionNext;//两个参数

    public BaseHttpRequest(RxAppCompatActivity rxAppCompatActivity) {
        manager = new HttpsManager(this, rxAppCompatActivity);
    }

    protected abstract HttpRequestApi reuqest();

    /**
     * 添加请求参数
     *
     * @param key
     * @param value
     * @return
     */
    protected BaseHttpRequest addParams(String key, String value) {
        if (mParams == null) {
            synchronized (BaseHttpRequest.this) {
                if (mParams == null) {
                    mParams = new HashMap<>();
                }
            }
        }
        mParams.put(key, value);
        return this;
    }

    /**
     * 开始网络请求，返回值
     *
     * @param
     * @return
     */
    public BaseHttpRequest requestBack() {
        manager.doHttpDeal(reuqest());
        return this;
    }

    /**
     * 开始网络请求，返回值数据
     *
     * @param consumerNext
     * @return
     */
    public BaseHttpRequest requestBack(Consumer<T> consumerNext) {
        manager.doHttpDeal(reuqest());
        this.consumerNext = consumerNext;
        return this;
    }

    /**
     * 开始网络请求，返回异常
     *
     * @param consumerError
     * @return
     */
    public BaseHttpRequest requestError(Consumer<ApiException> consumerError) {
        manager.doHttpDeal(reuqest());
        this.consumerError = consumerError;
        return this;
    }

    /**
     * 开始网络请求，返回数据和异常
     *
     * @param functionNext
     * @return
     */
    public BaseHttpRequest requestBack(BiConsumer<T,ApiException> functionNext) {
        manager.doHttpDeal(reuqest());
        this.functionNext = functionNext;
        return this;
    }

}

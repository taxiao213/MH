package com.haxi.mh.network.manager;


import android.support.v7.app.AppCompatActivity;

import com.haxi.mh.network.cookie.CookieResulte;
import com.haxi.mh.network.exception.ApiException;
import com.haxi.mh.network.exception.CodeException;
import com.haxi.mh.network.exception.CustomTimeException;
import com.haxi.mh.network.listener.HttpOnNextListener;
import com.haxi.mh.utils.db.CookieUtils;
import com.haxi.mh.utils.net.NetUtils;
import com.haxi.mh.utils.ui.progress.ProgressDialog;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 * Created by Han on 2017/12/18
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class ProgressObserver<T> implements Observer<T> {
    /*是否显示加载框*/
    private boolean showPorgress = true;
    private AppCompatActivity mActivity;
    /*加载框可自己定义*/
    private ProgressDialog progress;
    /*回调接口*/
    private HttpOnNextListener mHttpOnNextListener;
    /*请求数据*/
    private BaseApi api;


    public ProgressObserver(BaseApi api, HttpOnNextListener mHttpOnNextListener, AppCompatActivity mActivity) {
        this.api = api;
        this.mHttpOnNextListener = mHttpOnNextListener;
        this.mActivity = mActivity;
        if (api.isShowProgress()) {
            initProgressDialog(api.isCancle());
        }
    }


    /**
     * 初始化加载框
     */
    private void initProgressDialog(boolean cancel) {
        if (progress == null && mActivity != null) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progress = new ProgressDialog(mActivity);
                    progress.setCancelable(cancel);
                }
            });
        }
    }


    /**
     * 显示加载框
     */
    private void showProgressDialog() {
        if (progress == null || mActivity == null)
            return;
        if (progress != null && !progress.isShowing()) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!mActivity.isFinishing()) {
                            progress.show();
                        }
                    } catch (Exception e) {
                    }
                }
            });
        }
    }


    /**
     * 隐藏
     */
    private void dismissProgressDialog() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }


    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onSubscribe(Disposable disposable) {
        showProgressDialog();
        /*缓存并且有网*/
        if (api.isCache() && NetUtils.isNetworkConnected(mActivity)) {
            /*获取缓存数据*/
            CookieResulte cookieResulte = CookieUtils.getInstance().queryByUrl(api.getUrl());
            if (cookieResulte != null) {
                long time = (System.currentTimeMillis() - cookieResulte.getTime()) / 1000;//(单位是秒)
                if (time < api.getCookieNetWorkTime()) {
                    if (mHttpOnNextListener != null) {
                        mHttpOnNextListener.onNext(cookieResulte.getResulte(), api.getMethod());
                    }
                    if (disposable != null) {
                        disposable.dispose();//取消订阅
                    }
                    onComplete();
                }
            }
        }
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
         /*缓存处理*/
        if (api.isCache()) {
            CookieResulte resulte = CookieUtils.getInstance().queryByUrl(api.getUrl());
            long time = System.currentTimeMillis();
            /*保存和更新本地数据*/
            if (resulte == null) {
                resulte = new CookieResulte(api.getUrl(), t.toString(), time);
                CookieUtils.getInstance().save(resulte);
            } else {
                resulte.setResulte(t.toString());
                resulte.setTime(time);
                CookieUtils.getInstance().update(resulte);
            }
        }
        if (mHttpOnNextListener != null) {
            mHttpOnNextListener.onNext((String) t, api.getMethod());
        }
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     * onError()后不会走onComplete()
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        /*需要緩存并且本地有缓存才返回*/
        if (api.isCache()) {
            getCache();
        } else {
            errorDo(e);
        }
        dismissProgressDialog();
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onComplete() {
        dismissProgressDialog();
    }

    /**
     * 获取cache数据
     */
    private void getCache() {
        Observable.just(api.getUrl()).subscribeOn(Schedulers.io()).subscribe(new Observer<String>() {
            Disposable disposable = null;

            @Override
            public void onSubscribe(Disposable d) {
                this.disposable = d;
            }

            @Override
            public void onNext(String s) {
                /*获取缓存数据*/
                CookieResulte cookieResulte = CookieUtils.getInstance().queryByUrl(s);
                if (cookieResulte == null) {
                    CustomTimeException exception = new CustomTimeException(CustomTimeException.NO_CACHE_ERROR);
                    mHttpOnNextListener.onError(new ApiException(exception, CodeException.RUNTIME_ERROR, exception.getApiExceptionMessage(CustomTimeException.NO_CACHE_ERROR)));
                } else {
                    long time = (System.currentTimeMillis() - cookieResulte.getTime()) / 1000;//(单位是秒)
                    if (time < api.getCookieNoNetWorkTime()) {
                        if (mHttpOnNextListener != null) {
                            mHttpOnNextListener.onNext(cookieResulte.getResulte(), api.getMethod());
                        }
                    } else {
                        CookieUtils.getInstance().delete(cookieResulte);
                        CustomTimeException exception = new CustomTimeException(CustomTimeException.CHACE_TIMEOUT_ERROR);
                        mHttpOnNextListener.onError(new ApiException(exception, CodeException.RUNTIME_ERROR, exception.getApiExceptionMessage(CustomTimeException.CHACE_TIMEOUT_ERROR)));
                    }
                }

            }

            @Override
            public void onError(Throwable e) {
                errorDo(e);
                if (disposable != null) {
                    disposable.dispose();
                }
            }

            @Override
            public void onComplete() {
                if (disposable != null) {
                    disposable.dispose();
                }
            }
        });

    }


    /**
     * 错误统一处理
     *
     * @param e
     */
    private void errorDo(Throwable e) {
        if (mActivity == null)
            return;
        if (mHttpOnNextListener == null)
            return;
        if (e instanceof ApiException) {
            mHttpOnNextListener.onError((ApiException) e);
            int code = ((ApiException) e).getCode();
            if (code == CodeException.HTTP_ERROR || code == CodeException.RUNTIME_ERROR || code == CodeException.UNKOWNHOST_ERROR) {
                //ToastUtil.showSafeToast(mActivity.getResources().getString(R.string.tip_error));
            }
        } else if (e instanceof CustomTimeException) {
            CustomTimeException exception = (CustomTimeException) e;
            mHttpOnNextListener.onError(new ApiException(exception, CodeException.RUNTIME_ERROR, exception.getMessage()));
        } else {
            mHttpOnNextListener.onError(new ApiException(e, CodeException.UNKNOWN_ERROR, e.getMessage()));
        }
    }


}
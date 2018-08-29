package com.haxi.mh.mvp.model;

import com.haxi.mh.mvp.base.BaseActivityM;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Han on 2018/8/29
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class WxModel {
    private BaseActivityM baseActivityM;
    private OnLoadDataListener onLoadDataListener;
    private Disposable disposable;

    public WxModel(BaseActivityM baseActivityM, OnLoadDataListener onLoadDataListener) {
        this.baseActivityM = baseActivityM;
        this.onLoadDataListener = onLoadDataListener;
    }

    public void load( ) {
        //倒计时
        Observable.interval(1000, 1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        onLoadDataListener.success(1, aLong);
                    }

                    @Override
                    public void onError(Throwable e) {
                        onLoadDataListener.error(1);
                    }

                    @Override
                    public void onComplete() {
                        onLoadDataListener.success(1, null);
                    }
                });
    }

    public void onClick() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

}

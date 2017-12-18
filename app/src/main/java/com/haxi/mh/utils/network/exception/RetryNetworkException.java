package com.haxi.mh.utils.network.exception;

import java.net.ConnectException;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 重试 网络请求的异常
 * Created by Han on 2017/12/18
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 */

public class RetryNetworkException implements Function<Observable<? extends Throwable>, Observable<?>> {
    /*retry次数*/
    private int count = 1;
    /*延迟*/
    private long delay = 100;
    /*叠加延迟*/
    private long increaseDelay = 100;

    public RetryNetworkException() {

    }

    public RetryNetworkException(int count, long delay) {
        this.count = count;
        this.delay = delay;
    }

    public RetryNetworkException(int count, long delay, long increaseDelay) {
        this.count = count;
        this.delay = delay;
        this.increaseDelay = increaseDelay;
    }

    @Override
    public Observable<?> apply(final Observable<? extends Throwable> observable) throws Exception {
        //我们先去查询重试的次数，查询成功后使用flatMap操作符再发射一个Wrapper对象，此时的对象已经转换为Wrapper对象，最后根据异常情况去判断,是否进入oncomplate
        Observable<Object> objectObservable = observable.zipWith(Observable.range(1, count + 1), new BiFunction<Throwable, Integer, Wrapper>() {
            @Override
            public Wrapper apply(Throwable throwable, Integer integer) throws Exception {
                return new Wrapper(throwable, integer);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<Wrapper, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Wrapper wrapper) throws Exception {
                        Throwable throwable = wrapper.throwable;
                        if ((throwable instanceof ConnectException
                                || throwable instanceof SocketException
                                || throwable instanceof TimeoutException)
                                //如果超出重试次数也抛出错误，否则默认是会进入onCompleted
                                && (wrapper.index < (count + 1))) {
                            return Observable.timer(delay + (wrapper.index - 1) * increaseDelay, TimeUnit.MICROSECONDS);
                        }

                        return Observable.error(throwable);
                    }
                });

        return objectObservable;
    }

    private class Wrapper {
        private int index;
        private Throwable throwable;

        public Wrapper(Throwable throwable, int index) {
            this.index = index;
            this.throwable = throwable;
        }
    }
}

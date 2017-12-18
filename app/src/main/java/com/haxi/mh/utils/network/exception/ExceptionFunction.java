package com.haxi.mh.utils.network.exception;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * 异常处理
 * Created by Han on 2017/12/11
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 */

public class ExceptionFunction implements Function<Throwable, Observable> {

    @Override
    public Observable apply(Throwable throwable) throws Exception {
        return Observable.error(FactoryException.analysisExcetpion(throwable));
    }
}

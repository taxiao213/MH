package com.haxi.mh.utils.network.exception;


import io.reactivex.functions.Function;

/**
 * 服务器返回数据判断
 * Created by Han on 2017/12/11
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 */

public class ResulteFunction implements Function<Object, Object> {

    @Override
    public Object apply(Object o) throws Exception {
        if (null == o || "".equals(o.toString())) {
            throw new CustomTimeException("数据错误");
        }
        return o;
    }
}

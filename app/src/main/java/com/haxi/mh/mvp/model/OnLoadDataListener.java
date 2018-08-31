package com.haxi.mh.mvp.model;

/**
 * 加载数据接口
 * Created by Han on 2018/8/29
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public interface OnLoadDataListener<T> {
    void error(int type);

    void success(int type, T object);

    void destroy();
}

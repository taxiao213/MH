package com.haxi.mh.mvp.base;

/**
 * view接口 基类
 * Created by Han on 2018/8/25
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public interface IBaseView {
    /* 加载中 */
    void loading();

    /* 加载成功 */
    void loadingSuccess();

    /* 加载失败 */
    void loadingError();
}

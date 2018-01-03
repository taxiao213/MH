package com.haxi.mh.utils.ui.recyclerview.interfaces;

/**
 * 加载更多的回调方法
 * Created by Han on 2018/01/03
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public interface OnLoadMoreListener {
    /**
     * 加载更多的回调方法
     * @param isReload 是否是重新加载，只有加载失败后，点击重新加载时为true
     */
    void onLoadMore(boolean isReload);
}

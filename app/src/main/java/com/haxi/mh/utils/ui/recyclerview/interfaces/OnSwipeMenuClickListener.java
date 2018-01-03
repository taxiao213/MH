package com.haxi.mh.utils.ui.recyclerview.interfaces;


import com.haxi.mh.utils.ui.recyclerview.ViewHolder;

/**
 * Created by Han on 2018/01/03
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public interface OnSwipeMenuClickListener<T> {
    void onSwipMenuClick(ViewHolder viewHolder, T data, int position);
}

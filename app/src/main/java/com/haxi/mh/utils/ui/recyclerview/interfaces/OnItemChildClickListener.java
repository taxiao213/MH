package com.haxi.mh.utils.ui.recyclerview.interfaces;


import android.support.v7.widget.RecyclerView;

/**
 * 条目点击回调
 * Created by Han on 2018/01/03
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public interface OnItemChildClickListener<T> {
    void onItemChildClick(RecyclerView.ViewHolder viewHolder, T data, int position);
}

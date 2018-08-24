package com.haxi.mh.utils.ui.smartrefreshlayout.listener;

import android.support.annotation.NonNull;

import com.haxi.mh.utils.ui.smartrefreshlayout.api.RefreshLayout;

/**
 * 刷新监听器
 * Created by SCWANG on 2017/5/26.
 */

public interface OnRefreshListener {
    void onRefresh(@NonNull RefreshLayout refreshLayout);
}

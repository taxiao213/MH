package com.haxi.mh.ui.fragment;

import android.widget.ImageView;

import com.haxi.mh.R;
import com.haxi.mh.base.BaseFragment;
import com.haxi.mh.model.MyAffairsYibanbiBean;
import com.haxi.mh.utils.ui.view.xListView.XListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;


/**
 * 已处理业务
 * Created by Han on 2018/5/2
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class ProcessedBusinessFragment extends BaseFragment implements XListView.IXListViewListener {
    @BindView(R.id.fl_back)
    ImageView mFlBack;
    @BindView(R.id.affairsListview)
    XListView mylistView;

    private String type;
    private int totalPages;
    private int pages;
    List<MyAffairsYibanbiBean> lishiList;
    private boolean isInit = false;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_processed_business;
    }

    @Override
    protected void initView() {
        //当tab没有预加载时 点击非相邻界面，走这个方法
        if (getUserVisibleHint()) {
            init();
        }
    }

    @Override
    protected void initData() {

    }

    private void init() {
        pages = 1;
        lishiList = new ArrayList<>();
        if (!isInit) {
            //UI只加载一次，每次请求数据刷新
            mylistView.setXListViewListener(this);
        }
        isInit = true;
        getList(1, 10);
    }

    private void getList(final int page, final int rows) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {

            }
        });

    }


    @Override
    public void onRefresh() {
    }

    @Override
    public void onLoadMore() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //当tab 预加载过后，点击时走此方法
        if (isVisibleToUser && isVisible()) {
            init();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInit = false;

    }
}

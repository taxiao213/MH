package com.haxi.mh.ui.activity.pay.wxpay;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.haxi.mh.R;
import com.haxi.mh.mvp.adapter.WxAdapter;
import com.haxi.mh.mvp.base.BaseActivityM;
import com.haxi.mh.mvp.entity.WxInfoDao;
import com.haxi.mh.mvp.present.WxPresent;
import com.haxi.mh.mvp.view.IWxView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 微信支付
 * Created by Han on 2018/8/28
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class WxpayActivity extends BaseActivityM implements IWxView {

    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.ry)
    RecyclerView ry;
    private WxPresent wxPresent;
    private ArrayList<WxInfoDao> list;
    private WxAdapter adapter;


    @Override
    protected int getLayout() {
        return R.layout.activity_wx;
    }

    @Override
    protected void initView() {
        list = new ArrayList<>();
        adapter = new WxAdapter(mActivity, list);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        ry.setLayoutManager(manager);
        ry.setItemAnimator(new DefaultItemAnimator());
        ry.setAdapter(adapter);
    }

    @Override
    protected void getData() {
        wxPresent = new WxPresent(mActivity, this);
        wxPresent.loadData();
    }

    @OnClick(R.id.tv_content)
    public void onViewClicked() {
        if (wxPresent != null) {
            wxPresent.onClick();
            wxPresent.loadAdapterData();
        }
    }

    @Override
    public void loadingData() {
        if (wxPresent != null) {
            wxPresent.loadData();
        }
    }

    @Override
    public void loading() {
        load();
    }

    @Override
    public void loadingSuccess() {
        success();
    }

    @Override
    public void loadingError() {
        error();
    }


    @Override
    public void setText(Long log) {
        tvContent.setText(String.valueOf(log));
    }

    @Override
    public void setAdapter(Object object) {
        if (object != null) {
            List<WxInfoDao> wxInfoList = (List<WxInfoDao>) object;
            list.addAll(wxInfoList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wxPresent!=null){
            wxPresent.destroy();
        }
    }
}

package com.haxi.mh.ui.activity.pay.wxpay;

import android.widget.TextView;

import com.haxi.mh.R;
import com.haxi.mh.mvp.base.BaseActivityM;
import com.haxi.mh.mvp.present.WxPresent;
import com.haxi.mh.mvp.view.IWxView;

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
    private WxPresent wxPresent;


    @Override
    protected int getLayout() {
        return R.layout.activity_wx;
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


}

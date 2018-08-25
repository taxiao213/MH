package com.haxi.mh.ui.activity.pay.alipay;

import android.content.Intent;
import android.view.View;

import com.haxi.mh.R;
import com.haxi.mh.base.BaseActivity;

import butterknife.OnClick;

/**
 * 支付界面 包含支付宝支付 微信支付 银联支付
 * Created by Han on 2018/8/25
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class PayActivity extends BaseActivity {

    @Override
    protected int getLayoutRes() {
        return R.layout.payactivity;
    }

    @Override
    protected void getData() {

    }


    @OnClick({R.id.tv_alipay, R.id.tv_wxpay, R.id.tv_ylpay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_alipay:
                startActivity(new Intent(mActivity, AlipayActivity.class));
                break;
            case R.id.tv_wxpay:
                break;
            case R.id.tv_ylpay:
                break;
        }
    }
}

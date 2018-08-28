package com.haxi.mh.ui.activity.pay.wxpay;

import android.view.View;

import com.haxi.mh.R;
import com.haxi.mh.mvp.base.BaseActivity1;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Han on 2018/8/28
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class WxpayActivity extends BaseActivity1 {

    @Override
    protected void getData() {
        loading();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                     loadingError();
                    }
                });
            }
        }, 2000);
    }

    @Override
    protected View getLayout() {
        return View.inflate(this, R.layout.activity_wx, null);
    }


    @Override
    public void loading() {
        load();
    }

    @Override
    public void loadingSuccess() {

    }

    @Override
    public void loadingError() {

    }
}

package com.haxi.mh.ui.activity;

import android.content.Intent;
import android.content.IntentFilter;

import com.haxi.mh.R;
import com.haxi.mh.base.BaseActivity;
import com.haxi.mh.constant.Constant;
import com.haxi.mh.utils.ui.view.test.AppWidget;

/**
 * 自定义View组合使用
 * Created by Han on 2018/7/25
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class CustomizeActivity extends BaseActivity {

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_customize;
    }

    @Override
    protected void getData() {
        register();
        sendBroadcast(new Intent(Constant.RECEIVER_WIDGET));
    }

    private void register() {
        AppWidget appWidget = new AppWidget();
        IntentFilter filter = new IntentFilter(Constant.RECEIVER_WIDGET);
        registerReceiver(appWidget,filter);
    }

}

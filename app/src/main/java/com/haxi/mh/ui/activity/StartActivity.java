package com.haxi.mh.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * 启动页面
 * 解决白屏问题，不在代码中setContentView(),在资源文件中设置style，APP秒开
 * Created by Han on 2018/2/3
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //解决白屏问题，不在代码中setContentView(),在资源文件中设置style，
        //<item name="android:windowBackground">@drawable/splash_dot1</item>
        //<item name="android:windowFullscreen">true</item>
        startActivity(new Intent(this, SplashActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }
}

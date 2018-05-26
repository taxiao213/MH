package com.haxi.mh.ui.activity;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.TextView;

import com.haxi.mh.R;
import com.haxi.mh.base.BaseActivity;

import butterknife.BindView;

/**
 * 新控件
 * 可折叠使用方法
 * Created by Han on 2018/5/21
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class MaterialDesignActivity extends BaseActivity {

    @BindView(R.id.collapsingtoolbar)
    CollapsingToolbarLayout collapsingtoolbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.tv_content)
    TextView tvContent;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_material_design;
    }

    @Override
    protected void getData() {
        Window window = this.getWindow();
        window.addFlags(Window.FEATURE_ACTIVITY_TRANSITIONS);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingtoolbar.setTitle("水果");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 50; i++) {
            builder.append("水果哈哈哈。。。。");
            builder.append("水果哈哈哈。。。。");
        }
        tvContent.setText(builder.toString());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

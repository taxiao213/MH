package com.haxi.mh.mvp.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.haxi.mh.R;
import com.haxi.mh.utils.ui.ActivityManager;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * mvp BaseActivity
 * Created by Han on 2018/8/28
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public abstract class BaseActivityM extends AppCompatActivity implements IBaseView {


    private FrameLayout fl;
    private ImageView ivBaseError;
    private ProgressBar proBaseLoding;
    private View view;
    private Unbinder bind;
    public BaseActivityM mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = View.inflate(this, R.layout.activity_basem, null);
        fl = rootView.findViewById(R.id.fl);
        ivBaseError = rootView.findViewById(R.id.iv_base_error);
        proBaseLoding = rootView.findViewById(R.id.pro_base_loding);

        view = View.inflate(this, getLayout(), null);
        if (view != null) {
            view.setVisibility(View.GONE);
            fl.addView(view);
        }
        setContentView(rootView);
        bind = ButterKnife.bind(this);
        ActivityManager.getInstances().add(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mActivity = this;
        getData();
    }

    protected abstract void getData();

    protected abstract int getLayout();

    @OnClick(R.id.iv_base_error)
    public void onViewClicked() {
        load();
        loadingData();
    }

    public void load() {
        proBaseLoding.setVisibility(View.VISIBLE);
        ivBaseError.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
    }

    public void error() {
        proBaseLoding.setVisibility(View.GONE);
        ivBaseError.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);
    }

    public void success() {
        proBaseLoding.setVisibility(View.GONE);
        ivBaseError.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
        ActivityManager.getInstances().remove(this);
    }
}

package com.haxi.mh.mvp.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.haxi.mh.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Han on 2018/8/28
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public abstract class BaseActivity1 extends AppCompatActivity implements IBaseView {

    @BindView(R.id.iv_base_error)
    ImageView ivBaseError;
    @BindView(R.id.pro_base_loding)
    ProgressBar proBaseLoding;
    @BindView(R.id.fl)
    FrameLayout fl;
    private View view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base1);
        ButterKnife.bind(this);
        view = getLayout();
        view.setVisibility(View.GONE);
        fl.addView(view);

        getData();
    }

    protected abstract void getData();

    protected abstract View getLayout();

    @OnClick(R.id.iv_base_error)
    public void onViewClicked() {

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


}

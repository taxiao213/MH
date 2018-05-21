package com.haxi.mh.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haxi.mh.R;
import com.haxi.mh.base.BaseFragment;
import com.haxi.mh.ui.activity.MaterialDesignActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 文档
 * Created by Han on 2017/12/27
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class HomeCreateTaskFragment extends BaseFragment {

    @BindView(R.id.title_back)
    ImageView titleBack;
    @BindView(R.id.title_tv)
    TextView titleTv;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_homecreatetask;
    }

    @Override
    protected void initView() {
        titleBack.setVisibility(View.GONE);
        titleTv.setText(R.string.homecreatetask_name);
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.bt_01, R.id.bt_02, R.id.bt_03})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_01:
                startActivity(new Intent(mActivity, MaterialDesignActivity.class));
                break;
            case R.id.bt_02:
                break;
            case R.id.bt_03:
                break;
        }
    }
}

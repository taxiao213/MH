package com.haxi.mh.ui.activity;

import android.widget.Button;

import com.haxi.mh.R;
import com.haxi.mh.base.BaseActivity;
import com.haxi.mh.network.request.LoginRequest;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * Created by Han on 2017/12/13
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class MainActivity extends BaseActivity {
    @BindView(R.id.button)
    Button button;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void getData() {

    }

    @OnClick(R.id.button)
    public void onViewClicked() {
        new LoginRequest(mActivity).requestBack(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {

            }
        });
    }
}

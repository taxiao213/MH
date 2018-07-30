package com.haxi.mh.ui.activity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;

import com.haxi.mh.R;
import com.haxi.mh.base.BaseActivity;
import com.haxi.mh.constant.Constant;
import com.haxi.mh.utils.ui.view.Rotate3DAnimation;
import com.haxi.mh.utils.ui.view.test.AppWidget;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 自定义View组合使用
 * Created by Han on 2018/7/25
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class CustomizeActivity extends BaseActivity {

    @BindView(R.id.iv_attributes1)
    ImageView ivAttributes1;
    @BindView(R.id.iv_attributes2)
    ImageView ivAttributes2;
    @BindView(R.id.iv_attributes3)
    ImageView ivAttributes3;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_customize;
    }

    @Override
    protected void getData() {
        register();
        sendBroadcast(new Intent(Constant.RECEIVER_WIDGET));

    }

    private void animation() {
        int width = ivAttributes3.getWidth();
        int height = ivAttributes3.getHeight();
        Rotate3DAnimation animation = new Rotate3DAnimation(0, 300, width / 2, height / 2, 1000, false);
        animation.setDuration(2000);
        ivAttributes3.startAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setLayoutAnimation(controller);
    }

    private void register() {
        AppWidget appWidget = new AppWidget();
        IntentFilter filter = new IntentFilter(Constant.RECEIVER_WIDGET);
        registerReceiver(appWidget, filter);
    }

    @OnClick({R.id.tv_remote, R.id.iv_attributes3, R.id.iv_attributes2, R.id.iv_attributes1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_remote:
                break;
            case R.id.iv_attributes3:
                //View 动画
                animation();
                break;
            case R.id.iv_attributes2:
                //属性动画
                ObjectAnimator.ofFloat(ivAttributes2,"translationY",-ivAttributes2.getHeight()).start();
                break;
            case R.id.iv_attributes1:
                ValueAnimator animator = ObjectAnimator.ofInt(ivAttributes1, "backgroundColor", Color.RED, Color.BLUE);
                animator.setDuration(3000);
                animator.setEvaluator(new ArgbEvaluator());
                animator.setRepeatCount(ValueAnimator.INFINITE);
                animator.setRepeatMode(ValueAnimator.RESTART);
                animator.start();
                break;
        }


    }

}

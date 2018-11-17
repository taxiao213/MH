package com.haxi.mh.ui.activity.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.haxi.mh.R;
import com.haxi.mh.base.BaseActivity;
import com.haxi.mh.utils.animation.AnimationUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Create by Han on 2018/11/16
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class DrawActivity extends BaseActivity {
    @BindView(R.id.tv_draw1)
    TextView tvDraw1;
    @BindView(R.id.fl_draw)
    DrawView flDraw;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_draw;
    }

    @Override
    protected void getData() {

    }

    @OnClick({R.id.tv_draw1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_draw1:
                draw();
                break;

        }
    }

    private void draw() {
        flDraw.setClear(true);
        AlphaAnimation animation=new AlphaAnimation(0.0f,1.0f);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setDuration(2000);
        animation.start();
        flDraw.setAnimation(animation);
        flDraw.postInvalidate();
//        flDraw.invalidate();
    }

}

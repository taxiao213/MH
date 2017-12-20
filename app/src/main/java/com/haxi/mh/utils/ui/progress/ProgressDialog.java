package com.haxi.mh.utils.ui.progress;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.haxi.mh.utils.ui.UIUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 进度圈
 * Created by Han on 2017/12/18
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class ProgressDialog extends BaseProgressDialog {
    private static final float ANIMATE_SPEED = 1;
    private SpinView mIndeterminateView;

    private boolean isWait = true;
    private boolean canCancle;

    public ProgressDialog(Context context, boolean isWait) {
        super(context);
        this.isWait = isWait;
    }

    @Override
    protected View setContentView() {
        mIndeterminateView = new SpinView(getContext());
        mIndeterminateView.setAnimationSpeed(ANIMATE_SPEED);
        int widthMeasureSpec = UIUtil.dip2px(40);
        int heightMeasureSpec = UIUtil.dip2px(40);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(widthMeasureSpec, heightMeasureSpec);
        mIndeterminateView.setLayoutParams(lp);
        return mIndeterminateView;
    }

    public ProgressDialog(Context context) {
        super(context);
    }

    private void dismissLong() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isShowing()) {
                    dismiss();
                }
            }
        }, 60000);
    }

    @Override
    public void show() {
        super.show();
        if (isWait)
            dismissLong();
    }

    public void showNoClick() {
        setCancelable(false);
        show();
    }

    @Override
    public void setCancelable(boolean flag) {
        this.canCancle = flag;
        super.setCancelable(flag);
    }


    @Override
    public boolean dispatchKeyEvent(@NonNull KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
            dismiss();
        return super.dispatchKeyEvent(event);
    }


    /**
     * 改变动画的速度
     *
     * @param scale 默认 1, 2 速度提升2倍, 0.5 速度减少半倍..等
     */
    public void setAnimationSpeed(int scale) {
        mIndeterminateView.setAnimationSpeed(scale);
    }
}

package com.haxi.mh.ui.activity;

import android.animation.ArgbEvaluator;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
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

public class CustomizeActivity extends BaseActivity implements View.OnTouchListener {

    @BindView(R.id.iv_attributes1)
    ImageView ivAttributes1;
    @BindView(R.id.iv_attributes2)
    ImageView ivAttributes2;
    @BindView(R.id.iv_attributes3)
    ImageView ivAttributes3;
    @BindView(R.id.but1)
    Button but1;
    @BindView(R.id.but2)
    Button but2;
    private WindowManager.LayoutParams layoutParams;
    private WindowManager windowservice;
    private Button button;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_customize;
    }

    @Override
    protected void getData() {
        register();
        sendBroadcast(new Intent(Constant.RECEIVER_WIDGET));
        windowservice = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);

    }

    /**
     * layoutParams=new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
     * 会报错 Unable to add window android.view.ViewRootImpl
     */
    private void floatView() {

        layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.x = 100;
        layoutParams.y = 300;
        button = new Button(this);
        button.setText("click me");
        button.setOnTouchListener(this);
        windowservice.addView(button, layoutParams);
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int x = (int) event.getRawX();
                int y = (int) event.getRawY();
                layoutParams.x = x;
                layoutParams.y = y;
                windowservice.updateViewLayout(button, layoutParams);
                break;
        }
        return false;
    }

    @OnClick({R.id.tv_remote, R.id.iv_attributes3, R.id.iv_attributes2, R.id.iv_attributes1, R.id.but1, R.id.but2, R.id.but3})
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
                ObjectAnimator.ofFloat(ivAttributes2, "translationY", -ivAttributes2.getHeight()).start();
                break;
            case R.id.iv_attributes1:
                ValueAnimator animator = ObjectAnimator.ofInt(ivAttributes1, "backgroundColor", Color.RED, Color.BLUE);
                animator.setDuration(3000);
                animator.setEvaluator(new ArgbEvaluator());
                animator.setRepeatCount(ValueAnimator.INFINITE);
                animator.setRepeatMode(ValueAnimator.RESTART);
                animator.start();
                break;
            case R.id.but1:
                performClickBut();
                break;
            case R.id.but2:
                buttonAnimator(but2, 1, 400);
                break;
            case R.id.but3:
                removeView();
                floatView();
                break;
        }


    }

    private void removeView() {
        if (button != null) {
            if (windowservice != null) {
                windowservice.removeView(button);
            }
        }
    }

    /**
     * 第一次能动画
     */
    private void performClickBut() {
        ViewWrapper wrapper = new ViewWrapper(but1);
        ObjectAnimator.ofInt(wrapper, "width", 400).setDuration(2000).start();
    }

    /**
     * 点击多次都能动画
     *
     * @param targetView
     * @param start
     * @param end
     */
    private void buttonAnimator(View targetView, int start, int end) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 100);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            IntEvaluator intEvaluator = new IntEvaluator();

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();//获取当前的进度值
                float fraction = animation.getAnimatedFraction();//获取当前进度占整个进度的比例
                Integer evaluate = intEvaluator.evaluate(fraction, start, end);
                targetView.getLayoutParams().width = evaluate;
                targetView.requestLayout();
            }
        });
        valueAnimator.setDuration(2000).start();
    }


    public class ViewWrapper {
        private View view;

        public ViewWrapper(View view) {
            this.view = view;
        }

        public void setWidth(int width) {
            view.getLayoutParams().width = width;
            view.requestLayout();
        }

        public int getWidth() {
            return view.getLayoutParams().width;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        removeView();
    }
}

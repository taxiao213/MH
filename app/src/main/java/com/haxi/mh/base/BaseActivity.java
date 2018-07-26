package com.haxi.mh.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.haxi.mh.R;
import com.haxi.mh.model.MessageEvent;
import com.haxi.mh.utils.ui.ActivityManager;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Activity基类
 * Created by Han on 2017/12/13
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public abstract class BaseActivity extends RxAppCompatActivity {
    protected BaseActivity mActivity;
    private Unbinder bind;
    private View view;
    private LinearLayout layout_error;
    private ProgressBar progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        bind = ButterKnife.bind(this);
        this.mActivity = this;
        //竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        EventBus.getDefault().register(this);
        ActivityManager.getInstances().add(this);
        getData();
        //沉浸式状态栏
        //        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
        //            //透明状态栏
        //            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //            //透明导航栏
        //            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //        }

        ViewGroup contentView = findViewById(android.R.id.content);
        ViewGroup childAt = (ViewGroup) contentView.getChildAt(0);
//        childAt.setVisibility(View.GONE);
//        childAt.getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnDrawListener() {
//            @Override
//            public void onDraw() {
//                childAt.setVisibility(View.VISIBLE);
//                setProgressView(false);
//            }
//        });

        view = getView();
//        contentView.addView(view);
//        setProgressView(true);
    }

    private View getView() {
        View childView = LayoutInflater.from(this).inflate(R.layout.activity_error_layout, null);
        layout_error = childView.findViewById(R.id.layout_error);
        progress = childView.findViewById(R.id.progress);
        childView.findViewById(R.id.tv_error_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRetryViewVisibile(true);
            }
        });


        return childView;
    }

    /**
     * 点击重新获取网络的View
     *
     * @param isShow
     */
    public void setRetryViewVisibile(boolean isShow) {
        if (progress != null && layout_error != null) {
            layout_error.setVisibility(!isShow ? View.VISIBLE : View.GONE);
            progress.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
    }

    public void setProgressView(boolean isShow) {
        if (progress != null && layout_error != null) {
            progress.setVisibility(isShow ? View.VISIBLE : View.GONE);
            layout_error.setVisibility(!isShow ? View.VISIBLE : View.GONE);
        }
    }


    /**
     * 获取资源文件
     *
     * @return
     */
    protected abstract int getLayoutRes();

    /**
     * 获取数据
     */
    protected abstract void getData();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {

    }


    /**
     * 隐藏输入法
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            //flags --->> RESULT_UNCHANGED_SHOWN  0
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
        EventBus.getDefault().unregister(this);
        ActivityManager.getInstances().remove(this);
    }


}

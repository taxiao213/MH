package com.haxi.mh.utils.ui.view.test;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * 上下滑动的
 * Created by Han on 2018/7/24
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class StickyLayout extends LinearLayout {

    private int mLastX = 0;//记录上次点击的坐标X
    private int mLastY = 0;//记录上次点击的坐标Y

    // 分别记录上次滑动的坐标(onInterceptTouchEvent)
    private int mLastXIntercept = 0;
    private int mLastYIntercept = 0;

    private boolean mDisallowInterceptTouchEventOnHeader = false;

    public StickyLayout(Context context) {
        this(context, null);
    }

    public StickyLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickyLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int intercept = 0;
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastXIntercept = x;
                mLastYIntercept = y;
                mLastX = x;
                mLastY = y;
                intercept = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastXIntercept;
                int deltaY = y - mLastYIntercept;
                if (mDisallowInterceptTouchEventOnHeader && y <= getHeaderHeight()) {
                    intercept = 0;
                }else if(Math.abs(deltaY)<=Math.abs(deltaX)){
                    intercept=0;
                }
                break;
        }


        return intercept!=0;
    }

    /**
     * 获取头布局高度
     *
     * @return
     */
    private int getHeaderHeight() {

        return 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        return super.onTouchEvent(event);
    }
}

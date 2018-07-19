package com.haxi.mh.utils.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * 场景 外部左右滑动 里面是上下滑动，在里部的View和外部的View都做判断
 * Created by Han on 2018/7/19
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class ListViewEx extends ListView {
    // 记录上次滑动的坐标
    private int mLastX = 0;
    private int mLastY = 0;
    private HorizontalScrollViewEX mHorizontalScrollViewEx2;

    public ListViewEx(Context context) {
        this(context, null);
    }

    public ListViewEx(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void setHorizontalScrollViewEx2(HorizontalScrollViewEX horizontalScrollViewEx2) {
        this.mHorizontalScrollViewEx2 = horizontalScrollViewEx2;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mHorizontalScrollViewEx2.requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                int delatX = x - mLastX;
                int delatY = y - mLastY;
                if (Math.abs(delatY) < Math.abs(delatX)) {
                    mHorizontalScrollViewEx2.requestDisallowInterceptTouchEvent(false);
                }
                break;
        }

        mLastX = x;
        mLastY = y;
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }
}

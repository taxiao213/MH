package com.haxi.mh.utils.fileselector;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * 不让viewgroup向下传递触摸事件的LinearLayout
 * Created by Han on 2018/1/5
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class InterceptLinearLayout extends LinearLayout {

    public InterceptLinearLayout(Context context) {
        super(context);
    }

    public InterceptLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InterceptLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // ViewGroup.onInterceptEvent()中返回true时，将截获MotionEvent，View
    // Tree下面的View将无法获得MotionEvent，转而交给当前ViewGroup的onTouchEvent()方法
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

}

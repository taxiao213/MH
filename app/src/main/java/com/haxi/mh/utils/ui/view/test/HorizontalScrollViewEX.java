package com.haxi.mh.utils.ui.view.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 仿ViewPager 实现左右滑动
 * 场景 外部左右滑动 里面是上下滑动，在里部的View和外部的View都做判断
 * Created by Han on 2018/7/18
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class HorizontalScrollViewEX extends ViewGroup {
    private String Tag = "---HorizontalScrollView---";
    private Scroller mScroller;
    // 用于跟踪触摸事件的速度，用于实现投掷和其他此类手势
    private VelocityTracker mVelocityTracker;

    // 分别记录上次滑动的坐标
    private int mLastX = 0;
    private int mLastY = 0;

    // 分别记录上次滑动的坐标(onInterceptTouchEvent)
    private int mLastXIntercept = 0;
    private int mLastYIntercept = 0;

    private int mChildSize = 0;
    private int mChildWidth = 0;
    private int mChildIndex = 0;

    public HorizontalScrollViewEX(Context context) {
        this(context, null);
    }

    public HorizontalScrollViewEX(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalScrollViewEX(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mScroller = new Scroller(getContext());
        mVelocityTracker = VelocityTracker.obtain();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = 0;
        int measuredHeight = 0;
        int childCount = getChildCount();
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int widthMeasureSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMeasureMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMeasureSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMeasureMode = MeasureSpec.getMode(heightMeasureSpec);
        if (childCount == 0) {
            setMeasuredDimension(0, 0);
        } else if (heightMeasureMode == MeasureSpec.AT_MOST && widthMeasureMode == MeasureSpec.AT_MOST) {
            View childAt = getChildAt(0);
            measuredWidth = childAt.getMeasuredWidth() * childCount;
            measuredHeight = childAt.getMeasuredHeight();
            setMeasuredDimension(measuredWidth, measuredHeight);
        } else if (heightMeasureMode == MeasureSpec.AT_MOST) {
            View childAt = getChildAt(0);
            measuredHeight = childAt.getMeasuredHeight();
            setMeasuredDimension(widthMeasureSize, measuredHeight);
        } else if (widthMeasureMode == MeasureSpec.AT_MOST) {
            View childAt = getChildAt(0);
            measuredWidth = childAt.getMeasuredWidth() * childCount;
            setMeasuredDimension(measuredWidth, heightMeasureSize);
        } else {
            View childAt = getChildAt(0);
            measuredWidth = childAt.getMeasuredWidth() * childCount;
            measuredHeight = childAt.getMeasuredHeight();
            setMeasuredDimension(measuredWidth, measuredHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;
        int childCount = getChildCount();
        mChildSize = childCount;
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() != View.GONE) {
                int childWidth = childAt.getMeasuredWidth();
                mChildWidth = childWidth;
                childAt.layout(childLeft, 0, childLeft + childWidth, childAt.getMeasuredHeight());
                childLeft += childWidth;
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mLastX = x;
            mLastY = y;
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();//优化滑动体验
                return true;
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                scrollBy(-deltaX, 0);
                break;
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();//View 左滑动正数  右滑动负数
                if (mChildWidth == 0) {
                    break;
                }
                // int scrollToChildIndex = scrollX / mChildWidth;
                mVelocityTracker.computeCurrentVelocity(1000);//1秒时间运动的像素
                float xVelocity = mVelocityTracker.getXVelocity();//获取当前x的速度
                if (Math.abs(xVelocity) >= 50) {
                    mChildIndex = xVelocity > 0 ? mChildIndex - 1 : mChildIndex + 1;
                } else {
                    mChildIndex = (scrollX + mChildWidth / 2) / mChildWidth;
                }

                mChildIndex = Math.max(0, Math.min(mChildIndex, mChildSize - 1));
                int dx = mChildIndex * mChildWidth - scrollX;
                smoothScrollBy(dx, 0);
                mVelocityTracker.clear();
                break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }

    private void smoothScrollBy(int dx, int i) {
        mScroller.startScroll(getScrollX(), 0, dx, 0, 500);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        mVelocityTracker.clear();
        super.onDetachedFromWindow();
    }
}

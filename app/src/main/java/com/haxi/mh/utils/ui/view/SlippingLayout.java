package com.haxi.mh.utils.ui.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;

/**
 * 侧滑布局
 * Created by Han on 2018/01/03
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class SlippingLayout extends RelativeLayout {

    private ViewDragHelper dragHelper;
    private View content;
    private View delete;
    private int deleteWidth;

    public SlippingLayout(Context context) {
        this(context, null);
    }

    public SlippingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlippingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        dragHelper = ViewDragHelper.create(this, callback);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        content = getChildAt(0);
        delete = getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取宽和高
        deleteWidth = delete.getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        content.layout(0, 0, content.getMeasuredWidth(), content.getMeasuredHeight());
        delete.layout(content.getMeasuredWidth(), 0, content.getMeasuredWidth() + delete.getMeasuredWidth(), delete.getMeasuredHeight());

    }

    //ViewDragHelper拦截事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = dragHelper.shouldInterceptTouchEvent(ev);
        return result;
    }

    private float downX, downY;
    private long downTime;

    //自己消费事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                downTime = System.currentTimeMillis();
                if (mlistener != null) {
                    mlistener.onTouchDown(SlippingLayout.this);
                }
                requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                //1.获取移动的dx和dy
                float dx = moveX - downX;
                float dy = moveY - downY;
                //2.判断谁大就是偏向于谁的方向
                if (Math.abs(dx) > Math.abs(dy)) {
                    //说明偏向于水平，我们就认为是想滑动条目，则请求listview不要拦截
                    requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                //1.按下抬起的时间
                long duration = System.currentTimeMillis() - downTime;
                //2.计算按下抬起的距离
                float deltaX = event.getX() - downX;
                float deltaY = event.getY() - downY;
                float distance = (float) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
                //如果按下抬起的时间小于500，并且按下抬起的距离小于8像素,认为是满足了执行点击的条件
                if (duration < ViewConfiguration.getLongPressTimeout() && distance < ViewConfiguration.getTouchSlop()) {
                    //作用就是执行OnClickListener的onClick方法
                    performClick();
                }
                break;
        }
        dragHelper.processTouchEvent(event);
        return true;
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return 1;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {

            if (child == content) {
                left = getLeft(left);
            } else if (child == delete) {
                if (left < content.getMeasuredWidth() - delete.getMeasuredWidth()) {

                    left = content.getMeasuredWidth() - delete.getMeasuredWidth();

                } else if (left > content.getMeasuredWidth()) {
                    left = content.getMeasuredWidth();
                }
            }

            return left;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (changedView == content) {
                //让delete伴随移动
                int newLeft = delete.getLeft() + dx;
                //Log.e("tag", "getleft" + delete.getLeft() + "dx===" + dx);
                delete.layout(newLeft, 0, newLeft + delete.getMeasuredWidth(), delete.getBottom());
            } else if (changedView == delete) {
                //让content伴随移动
                int newLeft = content.getLeft() + dx;
                content.layout(newLeft, 0, newLeft + content.getMeasuredWidth(), content.getBottom());
            }

            if (content.getLeft() == 0) {
                //关闭了content
                if (mlistener != null) {
                    mlistener.close(SlippingLayout.this);
                }
            } else if (content.getLeft() == -delete.getMeasuredWidth()) {
                //打开了content
                if (mlistener != null) {
                    mlistener.open(SlippingLayout.this);
                }
            }

        }

        //手指抬起的释放
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (content.getLeft() > -deleteWidth / 2) {
                //关闭
                onClose();
            } else {
                onOpen();
            }
        }

    };

    public void onOpen() {
        dragHelper.smoothSlideViewTo(content, -deleteWidth, 0);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public void onClose() {
        dragHelper.smoothSlideViewTo(content, 0, 0);
        ViewCompat.postInvalidateOnAnimation(this);
    }


    @Override
    public void computeScroll() {
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }

    }

    private int getLeft(int left) {
        if (left > 0) {
            left = 0;
        } else if (left < -delete.getMeasuredWidth()) {
            left = -delete.getMeasuredWidth();
        }
        return left;
    }

    private OnSwipeDeleteListener mlistener;

    public void setOnSwipeDeleteListener(OnSwipeDeleteListener listener) {
        this.mlistener = listener;
    }

    public interface OnSwipeDeleteListener {
        void open(SlippingLayout swipe);

        void close(SlippingLayout swipe);

        void onTouchDown(SlippingLayout swipe);

        void isClose(boolean b);

    }
}

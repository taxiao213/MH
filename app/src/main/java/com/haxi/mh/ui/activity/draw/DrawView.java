package com.haxi.mh.ui.activity.draw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.haxi.mh.R;

import java.util.ArrayList;

import butterknife.OnTouch;

/**
 * Create by Han on 2018/11/17
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class DrawView extends View {
    private boolean isClear = false;
    private Paint paint;
    private ArrayList<Path> paths;
    private Canvas canvs;
    private float startx;
    private float starty;
    private Path path;

    public DrawView(@NonNull Context context) {
        this(context, null);
    }

    public DrawView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(getResources().getColor(R.color.colorAccent));
        paint.setStrokeWidth(2);
        paths = new ArrayList<>();
        canvs = new Canvas();
        path = new Path();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       /* if (isClear) {
            canvas.save();
            Path path1 = new Path();
            path1.lineTo(100, 100);
            path1.lineTo(200, 200);
            canvas.drawPath(path1, paint);
            canvas.drawCircle(100, 100, 10, paint);
            canvas.drawLine(100, 100, 200, 500, paint);
            canvas.drawLine(200, 500, 500, 500, paint);
            canvas.restore();
            if (paths != null) {
                paths.clear();
            }
        } else {
            canvas.save();
            if (paths == null) paths = new ArrayList<>();
            Path path1 = new Path();
            path1.lineTo(100, 100);
            paths.add(path1);
            Path path2 = new Path();
            path2.lineTo(200, 200);
            paths.add(path2);
            for (int i = 0; i < paths.size(); i++) {
                canvas.drawPath(paths.get(i), paint);
            }
            canvas.drawCircle(100, 100, 100, paint);
            canvas.restore();
        }*/
        canvas.drawPath(path, paint);
    }

    public void setClear(boolean isClear) {
        this.isClear = isClear;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startx = event.getX();
                starty = event.getY();
                path.moveTo(startx, starty);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                path.quadTo(startx, starty, x, y);
                startx = x;
                starty = y;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                path.lineTo(startx, starty);
                canvs.drawPath(path, paint);
                invalidate();
                break;
        }
        return true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    private void view(float x, float y) {
//        canvs.drawLine(startx,starty,x,y,paint);
        canvs.drawPoint(x, y, paint);
        invalidate();
//        canvs.restore();
    }
}

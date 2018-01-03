package com.haxi.mh.utils.ui.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.haxi.mh.R;


/**
 * 带有搜索文字和图片
 * Created by Han on 2018/01/03
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class SearchEdittext extends android.support.v7.widget.AppCompatEditText {

    private float imageWidth;//图片宽度
    private float textSize;//字体大小
    private int textColor;//字体颜色
    private Paint paint;
    private Drawable mDrawable;

    public SearchEdittext(Context context) {
        this(context, null);
    }

    public SearchEdittext(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchEdittext(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initResource(context, attrs);
        initPaint();
    }

    /**
     * 初始化资源
     *
     * @param context
     * @param attrs
     */
    private void initResource(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.searchEdittext);
        float density = context.getResources().getDisplayMetrics().density;
        imageWidth = array.getDimension(R.styleable.searchEdittext_imageWidth, 15 * density + 0.5f);
        textSize = array.getDimension(R.styleable.searchEdittext_searchTextSize, 14 * density + 0.5f);
        textColor = array.getColor(R.styleable.searchEdittext_searchTextColor, 0x333333);
        array.recycle();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSearchIcon(canvas);
    }

    /**
     * 绘制搜索图片
     */
    private void drawSearchIcon(Canvas canvas) {
        if (this.getText().toString().length() == 0) {
            float textWidth = paint.measureText("搜索");
            float textHeight = getFont(paint);
            float dx = (getWidth() - imageWidth - textWidth - 15) / 2;
            float dy = (getHeight() - textHeight) / 2;
            canvas.save();
            canvas.translate(getScrollX() + dx, getScrollY() + dy);
            if (mDrawable != null) {
                mDrawable.draw(canvas);
            }
            canvas.drawText("搜索", getScrollX() + imageWidth + 15,
                    getScrollY() + (getHeight() - (getHeight() - textHeight) / 2 - paint.getFontMetrics().bottom - dy), paint);

            canvas.restore();

        }
    }

    private float getFont(Paint paint) {
        Paint.FontMetrics metrics = paint.getFontMetrics();
        return metrics.bottom - metrics.top;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mDrawable == null) {
            try {
                mDrawable = getContext().getResources().getDrawable(R.drawable.edittext_search);
                mDrawable.setBounds(0, 0, (int) imageWidth, (int) imageWidth);
            } catch (Exception e) {

            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mDrawable != null) {
            mDrawable.setCallback(null);
            mDrawable = null;
        }
    }

}

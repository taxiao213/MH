package com.haxi.mh.utils.ui.view.gesture;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haxi.mh.R;

/**
 * Created by Han on 2018/6/10
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class CustomLayout extends LinearLayout {

    private String leftText;
    private String centerText;

    public CustomLayout(Context context) {
        this(context, null);
    }

    public CustomLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.customlayout);
        leftText = typedArray.getString(R.styleable.customlayout_left_text);
        centerText = typedArray.getString(R.styleable.customlayout_center_text);
        int visible = typedArray.getInteger(R.styleable.customlayout_isvisible, View.VISIBLE);
        typedArray.recycle();

        View view = LayoutInflater.from(context).inflate(R.layout.activity_customlayout_item, this);
        TextView tvLeft = view.findViewById(R.id.tv_left);
        TextView tvRight = view.findViewById(R.id.tv_right);
        ImageView back = view.findViewById(R.id.iv_back);
        tvLeft.setText(leftText);
        tvRight.setText(centerText);
        back.setVisibility(visible);
    }
}

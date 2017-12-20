/*
 *    Copyright 2015 Kaopiz Software Co., Ltd.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.haxi.mh.utils.ui.pross;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.haxi.mh.R;
import com.haxi.mh.utils.ui.UIUtil;

/**
 * 进度圈基类
 * Created by Han on 2017/12/18
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public abstract class BaseProgressDialog extends Dialog {

    private static final float DIM_AMOUNT = 0;//黑暗度 0 - 1.0
    private static final int BACKGROUND_WIDTH = 0;
    private static final int BACKGROUND_HEIGHT = 0;

    private View mRootVew;
    private TextView mLabelText;
    private TextView mDetailsText;
    private FrameLayout mCustomViewContainer;
    private BackgroundLayout mBackgroundLayout;

    public BaseProgressDialog(Context context) {
        super(context);
        mRootVew = getLayoutInflater().inflate(R.layout.kprogresshud_hud, null, false);
        setWindowDimAmount(DIM_AMOUNT);
        initViews();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(mRootVew);
    }

    private void setWindowDimAmount(float dimAmount) {
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0));
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.dimAmount = dimAmount;//设置黑暗度
        window.setAttributes(layoutParams);
    }

    private void initViews() {
        mBackgroundLayout = (BackgroundLayout) mRootVew.findViewById(R.id.background);
        mCustomViewContainer = (FrameLayout) mRootVew.findViewById(R.id.container);
        mLabelText = (TextView) mRootVew.findViewById(R.id.label);
        mDetailsText = (TextView) mRootVew.findViewById(R.id.details_label);

        updateBackgroundSize(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);

        addViewToFrame(setContentView());

    }

    private void addViewToFrame(View view) {
        if (view == null)
            return;
        mCustomViewContainer.addView(view);
    }

    protected abstract View setContentView();

    private void updateBackgroundSize(int width, int height) {
        if (width > 0 && height > 0) {
            ViewGroup.LayoutParams params = mBackgroundLayout.getLayoutParams();
            params.width = UIUtil.dip2px(width);
            params.height = UIUtil.dip2px(height);
            mBackgroundLayout.setLayoutParams(params);
        }
    }

    /**
     * 指定Window的透明度
     *
     * @param dimAmount 透明度从 0 到 1.
     */
    public BaseProgressDialog setDimAmount(float dimAmount) {
        if (dimAmount >= 0 && dimAmount <= 1) {
            setWindowDimAmount(dimAmount);
        }
        return this;
    }


    /**
     * dialog的背景颜色
     *
     * @param color ARGB color
     */
    public BaseProgressDialog setWindowColor(int color) {
        mBackgroundLayout.setBaseColor(Color.WHITE);
        return this;
    }

    /**
     * 设置dialog背景的圆角 (默认是 10)
     *
     * @param radius 圆角的半径
     */
    public BaseProgressDialog setCornerRadius(int radius) {
        mBackgroundLayout.setCornerRadius(radius);
        return this;
    }

    /**
     * 提供一个方法,可现实自定的view.
     *
     * @param view 必须不能为null
     */
    public BaseProgressDialog setCustomView(View view) {
        if (view != null) {
            setView(view);
        } else {
            throw new RuntimeException("Custom view must not be null!");
        }
        return this;
    }

    public void setView(View view) {
        if (view != null) {
            if (isShowing()) {
                mCustomViewContainer.removeAllViews();
                addViewToFrame(view);
            }
        }
    }

    public BaseProgressDialog setLabel(String label) {
        if (label != null) {
            mLabelText.setText(label);
            mLabelText.setVisibility(View.VISIBLE);
        } else {
            mLabelText.setVisibility(View.GONE);
        }
        return this;
    }

    public BaseProgressDialog setDetailsLabel(String detailsLabel) {
        if (detailsLabel != null) {
            mDetailsText.setText(detailsLabel);
            mDetailsText.setVisibility(View.VISIBLE);
        } else {
            mDetailsText.setVisibility(View.GONE);
        }
        return this;
    }

    public void setSize(int width, int height) {
        if (mBackgroundLayout != null) {
            updateBackgroundSize(width, height);
        }
    }

}

package com.haxi.mh.utils.ui.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 跑马灯
 * Created by Han on 2018/01/05
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class TextScrollView extends android.support.v7.widget.AppCompatTextView {

    public TextScrollView(Context context) {
        super(context);
    }

    public TextScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 返回textview是否处在选中的状态
     * 而只有选中的textview才能够实现跑马灯效果
     * @return
     */
    @Override
    public boolean isFocused() {
        return true;
    }
}

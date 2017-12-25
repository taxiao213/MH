package com.haxi.mh.utils.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import java.util.ArrayList;
import java.util.List;


/**
 * 将DatePicker TimePicker 显示在同一屏幕 时间选择器工具类
 * Created by Han on 2017/12/25
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class DatePickerUtils {
    /**
     * 调整FrameLayout大小
     *
     * @param tp
     */
    public static void resizePikcer(FrameLayout tp) {
        float[] size = null;
        //npList size==3 代表 datepicker 年月日宽度对应为 0.25f 0.2f 0.2f
        //npList size==2 代表 timepicker 时分宽度对应为 0.175f 0.175f
        List<NumberPicker> npList = findNumberPicker(tp);
        if (npList.size() == 3) {
            size = new float[]{0.2f, 0.15f, 0.15f};
        } else if (npList.size() == 2) {
            size = new float[]{0.15f, 0.15f};

        }
        for (int i = 0; i < npList.size(); i++) {
            NumberPicker np = npList.get(i);
            resizeNumberPicker(np, size[i]);
        }
    }

    /**
     * 得到viewGroup里面的numberpicker组件
     *
     * @param viewGroup
     * @return
     */
    private static List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {

        List<NumberPicker> npList = new ArrayList<NumberPicker>();
        View child = null;
        if (null != viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                child = viewGroup.getChildAt(i);
                if (child instanceof NumberPicker) {
                    npList.add((NumberPicker) child);
                } else if (child instanceof LinearLayout) {
                    List<NumberPicker> result = findNumberPicker((ViewGroup) child);
                    if (result.size() > 0) {
                        return result;
                    }
                }
            }
        }
        return npList;
    }

    /**
     * 调整numberpicker大小
     *
     * @param np
     * @param size 每个numberPicker对应分得屏幕宽度
     */
    private static void resizeNumberPicker(NumberPicker np, float size) {
        int dp5 = UIUtil.dip2px(5);
        //timepicker 时 分 左右各自有8dp空白
        int dp32 = UIUtil.dip2px(32);
        //屏幕宽度 - timepicker左右空白 -自设周边5dp空白
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) ((UIUtil.getScreenWidth() - dp32 - dp5 * 10) * size), ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(dp5, 0, dp5, 0);
        np.setLayoutParams(params);
    }
}

package com.haxi.mh.utils.animation;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Han on 2018/9/12
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class AnimationUtils {

    public static void startAnimation(View view, int height, boolean isExpand) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        ValueAnimator animator = new ValueAnimator();
        if (isExpand) {
            animator.setIntValues(0, height);
        } else {
            animator.setIntValues(height, 0);
        }
        animator.setDuration(400);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                params.height = value;
                view.requestLayout();
            }
        });
        animator.start();
    }
}

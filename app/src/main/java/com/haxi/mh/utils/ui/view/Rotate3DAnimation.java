package com.haxi.mh.utils.ui.view;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * 3D 动画
 * Created by Han on 2018/7/30
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class Rotate3DAnimation extends Animation {
    private float fromDegree;
    private float toDegree;
    private float centerX;
    private float centerY;
    private float DepthZ;
    private boolean reverse;
    private Camera camera;

    public Rotate3DAnimation(float fromDegree, float toDegree, float centerX, float centerY, float DepthZ, boolean reverse) {
        this.fromDegree = fromDegree;
        this.toDegree = toDegree;
        this.centerX = centerX;
        this.centerY = centerY;
        this.DepthZ = DepthZ;
        this.reverse = reverse;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        camera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float degree = fromDegree + (toDegree - fromDegree) * interpolatedTime;
        Matrix matrix = t.getMatrix();
        camera.save();
        if (reverse) {
            camera.translate(0f, 0f, DepthZ * interpolatedTime);
        } else {
            camera.translate(0f, 0f, DepthZ * (1f - interpolatedTime));
        }
        camera.rotateY(degree);
        camera.getMatrix(matrix);
        camera.restore();
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }
}

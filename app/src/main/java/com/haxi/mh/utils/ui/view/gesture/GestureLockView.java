package com.haxi.mh.utils.ui.view.gesture;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haxi.mh.R;
import com.haxi.mh.utils.ui.UIUtil;

import java.util.ArrayList;
import java.util.List;

/*
    用法：
   FrameLayout mFrameLayout = (FrameLayout) findViewById(R.id.framelayout);
   GuestureLockView mGuestureLockView = new GuestureLockView(mActivity, new Drawl.GestureCallBack() {
            @Override
            public void checkedSuccess(String password) {
                //password是用户输入的密码，是数字
            }

            @Override
            public void checkedFail() {

            }
        });

  mGuestureLockView.setParentView(mFrameLayout);
 */

/**
 * 手势View
 * Created by Han on 2018/01/08
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class GestureLockView extends ViewGroup {

    private int baseNum = 6;

    private int[] screenDispaly;

    private int d;
    /**
     * 声明一个集合用来封装坐标集合
     */
    private List<PointGesture> list;
    private Context context;
    private DrawGesture drawgesture;

    /**
     * 包含9个ImageView的容器，初始化
     *
     * @param context
     * @param callBack 手势绘制完毕的回调
     */
    public GestureLockView(Context context, DrawGesture.GestureCallBack callBack) {
        super(context);
        screenDispaly = UIUtil.getScreenDispaly(context);
        d = (screenDispaly[0] - 100) / 3;
        this.list = new ArrayList<>();
        this.context = context;
        // 添加9个图标
        addChild();
        // 初始化一个可以画线的view
        drawgesture = new DrawGesture(context, list, callBack);
    }

    private void addChild() {
        for (int i = 0; i < 9; i++) {
            ImageView image = new ImageView(context);
            image.setBackgroundResource(R.drawable.gesture_node_normal);
            this.addView(image);

            // 第几行
            int row = i / 3;
            // 第几列
            int col = i % 3;

            // 定义点的每个属性
            int leftX = col * d + d / baseNum;
            int topY = row * d + d / baseNum;
            int rightX = col * d + d - d / baseNum;
            int bottomY = row * d + d - d / baseNum;

            PointGesture p = new PointGesture(leftX, rightX, topY, bottomY, image, i + 1);

            this.list.add(p);
        }
    }


    public void setParentView(ViewGroup parent) {
        // 得到屏幕的宽度
        int width = screenDispaly[0];
        LayoutParams layoutParams = new LayoutParams(width - 100, width - 100);

        this.setLayoutParams(layoutParams);
        drawgesture.setLayoutParams(layoutParams);

        parent.addView(drawgesture);
        parent.addView(this);

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            //第几行
            int row = i / 3;
            //第几列
            int col = i % 3;
            View v = getChildAt(i);
            v.layout(col * d + d / baseNum, row * d + d / baseNum, col * d + d - d / baseNum, row * d + d - d / baseNum);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }


}

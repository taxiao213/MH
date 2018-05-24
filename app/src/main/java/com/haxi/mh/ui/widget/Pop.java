package com.haxi.mh.ui.widget;

import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haxi.mh.R;
import com.haxi.mh.base.BaseActivity;


/**
 * 如果登录界面图片要填充满设置此项可以使状态栏透明 getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
 * popupwindow 设置在某个View下时要注意
 * Created by Han on 2018/5/25
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class Pop {
    private BaseActivity mContext;
    private PopupWindow window;

    public Pop(BaseActivity context) {
        this.mContext = context;
    }

    public void initPop() {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.activity_pop, null);
        window = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        window.setBackgroundDrawable(new ColorDrawable(0x80000000));
        window.setOutsideTouchable(true);
        //居中显示
        window.showAtLocation(view, Gravity.CENTER, 0, 0);
       /* //在某个View底部显示
        if (Build.VERSION.SDK_INT < 24) {
            window.showAsDropDown(view);
        } else {
            Rect visibleFrame = new Rect();
            view.getGlobalVisibleRect(visibleFrame);
            int height = view.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            window.setHeight(height);
            window.showAsDropDown(view, 0, view.getHeight());
        }*/

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                LinearLayout ll = (LinearLayout) view.findViewById(R.id.risk_pop_root_ll);
                int bottom = ll.getBottom();
                int height = ll.getTop();
                int left = ll.getLeft();
                int right = ll.getRight();
                int y = (int) event.getY();
                int x = (int) event.getX();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y > bottom || y < height || x < left || x > right) {
                        window.dismiss();
                    }
                }
                return true;
            }
        });
        initdata(view);
    }

    private void initdata(View view) {
        TextView risk_pop_tv_title = (TextView) view.findViewById(R.id.risk_pop_tv_title);
        RelativeLayout risk_pop_rl = (RelativeLayout) view.findViewById(R.id.risk_pop_rl);
        final EditText risk_pop_et = (EditText) view.findViewById(R.id.risk_pop_et);
        LinearLayout risk_pop_ll = (LinearLayout) view.findViewById(R.id.risk_pop_ll);
        TextView risk_pop_tv_cancel = (TextView) view.findViewById(R.id.risk_pop_tv_cancel);
        TextView risk_pop_tv_commit = (TextView) view.findViewById(R.id.risk_pop_tv_commit);


        risk_pop_tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (window != null) {
                    window.dismiss();
                }
            }
        });

        risk_pop_tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}

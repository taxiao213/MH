package com.haxi.mh.ui.activity;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.haxi.mh.R;
import com.haxi.mh.base.BaseActivity;
import com.haxi.mh.utils.ui.UIUtil;
import com.haxi.mh.utils.ui.view.HorizontalScrollView;

import java.util.ArrayList;

/**
 * Created by Han on 2018/7/18
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class ViewInterceptActivity extends BaseActivity {

    private HorizontalScrollView horizontalScrollView;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_view_intercept;
    }

    @Override
    protected void getData() {
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = getLayoutInflater();
        horizontalScrollView = findViewById(R.id.horizontal_view);
        int screenWidth = UIUtil.getScreenWidth();
        int screenHeight = UIUtil.getScreenHeight();
        for (int i = 0; i < 3; i++) {
            ViewGroup inflate = (ViewGroup) layoutInflater.inflate(R.layout.activity_view_content, horizontalScrollView, false);
            inflate.getLayoutParams().width = screenWidth;
            TextView textview = inflate.findViewById(R.id.title);
            textview.setText("page" + (i + 1));
            inflate.setBackgroundColor(Color.rgb(255 / (i + 1), 255 / (i + 1), 0));
            createList(inflate);
            horizontalScrollView.addView(inflate);
        }
    }

    private void createList(ViewGroup inflate) {
        ListView listView = inflate.findViewById(R.id.list);
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            strings.add("list" + i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.activity_view_content_list_item, R.id.title, strings);
        listView.setAdapter(adapter);
    }
}

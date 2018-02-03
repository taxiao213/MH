package com.haxi.mh.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.haxi.mh.R;
import com.haxi.mh.base.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 过渡页
 * Created by Han on 2018/2/3
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class SplashActivity extends BaseActivity {
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.dot_1)
    View dot1;
    @BindView(R.id.dot_2)
    View dot2;
    @BindView(R.id.dot_3)
    View dot3;
    private ArrayList<View> list;
    private int oldPosition = 0;
    private ArrayList<Drawable> imageViews;

    @Override
    protected int getLayoutRes() {
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        return R.layout.activity_splash;
    }

    @Override
    protected void getData() {
        list = new ArrayList<>();
        list.add(dot1);
        list.add(dot2);
        list.add(dot3);
        list.get(0).setSelected(true);

        imageViews = new ArrayList<>();
        imageViews.add(getResources().getDrawable(R.drawable.splash_dot1));
        imageViews.add(getResources().getDrawable(R.drawable.splash_dot2));
        imageViews.add(getResources().getDrawable(R.drawable.splash_dot3));

        viewpager.setOnPageChangeListener(new android.support.v4.view.ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == list.size() - 1) {
                    startActivity(new Intent(mActivity, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                }
            }

            @Override
            public void onPageSelected(int position) {
                list.get(oldPosition).setSelected(false);
                list.get(position).setSelected(true);
                oldPosition = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ViewPagerAdapter adapter = new ViewPagerAdapter(mActivity, imageViews);
        viewpager.setAdapter(adapter);

    }

    private class ViewPagerAdapter extends PagerAdapter {
        private Context mContext;
        private ArrayList<Drawable> mImageViews;

        public ViewPagerAdapter(Context context, ArrayList<Drawable> imageViews) {
            this.mContext = context;
            this.mImageViews = imageViews;
        }

        @Override
        public int getCount() {
            return mImageViews.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageDrawable(mImageViews.get(position));
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            //移除条目
            container.removeView((ImageView) object);
        }
    }
}

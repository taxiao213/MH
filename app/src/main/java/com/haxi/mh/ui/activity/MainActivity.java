package com.haxi.mh.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.haxi.mh.R;
import com.haxi.mh.base.BaseActivity;
import com.haxi.mh.service.PlayMusicService;
import com.haxi.mh.ui.fragment.HomeCreateTaskFragment;
import com.haxi.mh.ui.fragment.HomeManageFragment;
import com.haxi.mh.ui.fragment.HomePageFragment;
import com.haxi.mh.ui.fragment.HomePeopleFragment;
import com.haxi.mh.utils.model.LogUtils;
import com.haxi.mh.utils.ui.UIUtil;

import butterknife.BindView;

import static com.haxi.mh.constant.Constant.TAG_FRAGMENT1;
import static com.haxi.mh.constant.Constant.TAG_FRAGMENT2;
import static com.haxi.mh.constant.Constant.TAG_FRAGMENT3;
import static com.haxi.mh.constant.Constant.TAG_FRAGMENT4;

/**
 * 首页
 * Created by Han on 2017/12/13
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {


    @BindView(R.id.fl)
    FrameLayout fl;
    @BindView(R.id.rb_homepage)
    RadioButton rbHomepage;
    @BindView(R.id.rb_homecreatetask)
    RadioButton rbHomecreatetask;
    @BindView(R.id.rb_homemanage)
    RadioButton rbHomemanage;
    @BindView(R.id.rb_homepeople)
    RadioButton rbHomepeople;
    @BindView(R.id.rg)
    RadioGroup rg;
    private String current_tag = null;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void getData() {
        rg.setOnCheckedChangeListener(this);
        switchFragment(TAG_FRAGMENT1);
        startService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        startService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        startService();
    }

    /**
     * 开启服务
     */
    private void startService() {
        LogUtils.e("mh-->>MainActivity--->>startService()");
        if (!UIUtil.isServiceRunning(mActivity, PlayMusicService.class.getName())) {
            mActivity.startService(new Intent(mActivity, PlayMusicService.class));
            LogUtils.e("mh-->>MainActivity--->>startService()开启");
        }
    }



    /**
     * 切换fragment
     *
     * @param tab
     */
    private void switchFragment(String tab) {
        switch (tab) {
            case TAG_FRAGMENT1:
                rg.check(R.id.rb_homepage);
                break;
            case TAG_FRAGMENT2:
                rg.check(R.id.rb_homecreatetask);
                break;
            case TAG_FRAGMENT3:
                rg.check(R.id.rb_homemanage);
                break;
            case TAG_FRAGMENT4:
                rg.check(R.id.rb_homepeople);
                break;
        }

    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment1 = fragmentManager.findFragmentByTag(TAG_FRAGMENT1);
        Fragment fragment2 = fragmentManager.findFragmentByTag(TAG_FRAGMENT2);
        Fragment fragment3 = fragmentManager.findFragmentByTag(TAG_FRAGMENT3);
        Fragment fragment4 = fragmentManager.findFragmentByTag(TAG_FRAGMENT4);
        if (fragment1 != null) {
            transaction.hide(fragment1);
        }
        if (fragment2 != null) {
            transaction.hide(fragment2);
        }
        if (fragment3 != null) {
            transaction.hide(fragment3);
        }
        if (fragment4 != null) {
            transaction.hide(fragment4);
        }

        switch (i) {
            case R.id.rb_homepage:
                if (fragment1 == null) {
                    fragment1 = new HomePageFragment();
                    transaction.add(R.id.fl, fragment1, TAG_FRAGMENT1);
                } else {
                    transaction.show(fragment1);
                }
                current_tag = TAG_FRAGMENT1;
                break;
            case R.id.rb_homecreatetask:
                if (fragment2 == null) {
                    fragment2 = new HomeCreateTaskFragment();
                    transaction.add(R.id.fl, fragment2, TAG_FRAGMENT2);
                } else {
                    transaction.show(fragment2);
                }
                current_tag = TAG_FRAGMENT2;
                break;
            case R.id.rb_homemanage:
                if (fragment3 == null) {
                    fragment3 = new HomeManageFragment();
                    transaction.add(R.id.fl, fragment3, TAG_FRAGMENT3);
                } else {
                    transaction.show(fragment3);
                }
                current_tag = TAG_FRAGMENT3;
                break;
            case R.id.rb_homepeople:
                if (fragment4 == null) {
                    fragment4 = new HomePeopleFragment();
                    transaction.add(R.id.fl, fragment4, TAG_FRAGMENT4);
                } else {
                    transaction.show(fragment4);
                }
                current_tag = TAG_FRAGMENT4;
                break;
        }
        transaction.commitNow();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (current_tag != null) {
            outState.putString("current_tag", current_tag);
        }
        LogUtils.e("onSaveInstanceState", current_tag);
        startService();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            String tag = savedInstanceState.getString("current_tag");
            if (tag != null) {
                switchFragment(tag);
            }
        }
        LogUtils.e("onRestoreInstanceState", current_tag);
        startService();
    }
}

package com.haxi.mh.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.haxi.mh.R;
import com.haxi.mh.base.BaseActivity;
import com.haxi.mh.ui.fragment.ProcessedBusinessFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 业务审批
 * Created by Han on 2018/5/2
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class BusinessApprovalActivity extends BaseActivity {

    @BindView(R.id.menu_tab)
    TabLayout mTab;
    @BindView(R.id.pager)
    ViewPager mPager;
    private List<Fragment> mPagerFragmentList;
    private String[] title = {"待处理业务", "已处理业务", "待处理业务", "已处理业务"};
    private int select;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_budiness_approval;
    }

    @Override
    protected void getData() {
        select = getIntent().getIntExtra("select", 0);
        creatPager();
    }


    @OnClick({R.id.back, R.id.search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.search:
                break;
        }
    }

    private void creatPager() {
        mPagerFragmentList = new ArrayList<>();
        mPagerFragmentList.add(new ProcessedBusinessFragment());
        mPagerFragmentList.add(new ProcessedBusinessFragment());
        mPagerFragmentList.add(new ProcessedBusinessFragment());
        mPagerFragmentList.add(new ProcessedBusinessFragment());
        PagerAdapter mPagerAdapter = new PagerAdapter(mActivity.getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mTab.setupWithViewPager(mPager);
        mPager.setCurrentItem(select);

    }

    private class PagerAdapter extends FragmentPagerAdapter {

        PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mPagerFragmentList.get(position);
        }


        @Override
        public int getCount() {
            return title.length;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //解决viewpager重复加载数据
        }
        //
        //        @Override
        //        public int getItemPosition(Object object) {
        //            //实现重新加载需返回此值
        //            return BusinessApprovalActivity.PagerAdapter.POSITION_NONE;
        //        }
    }

}

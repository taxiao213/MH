package com.haxi.mh.ui.fragment;

import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.haxi.mh.R;
import com.haxi.mh.base.BaseFragment;
import com.haxi.mh.model.db.Person;
import com.haxi.mh.utils.db.PersonUtils;
import com.haxi.mh.utils.net.UploadUtil;
import com.haxi.mh.utils.ui.UIUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 首页
 * Created by Han on 2017/12/26
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class HomePageFragment extends BaseFragment {


    @BindView(R.id.title_back)
    ImageView titleBack;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.bt_01)
    Button bt01;
    @BindView(R.id.bt_02)
    Button bt02;


    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_homepage;
    }

    @Override
    protected void initView() {
        titleBack.setVisibility(View.GONE);
        titleTv.setText(UIUtil.getString(R.string.homepager_name));
    }

    @Override
    protected void initData() {

    }


    @OnClick({R.id.bt_01, R.id.bt_02})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_01:
                PersonUtils.getInstance().save(new Person(12l, "小花", "sss"));
                break;
            case R.id.bt_02:
                String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                File file1 = new File(rootPath + "/hrchat/chatfile");
                File file = null;
                if (file1.exists()) {
                    file = new File(file1, "iv_2.png");
                }
                if (file != null) {
                    UploadUtil.getInstance().upload(file);
                }
                break;
        }
    }
}

package com.haxi.mh.ui.activity.pay.dense;

import android.view.View;

import com.haxi.mh.R;
import com.haxi.mh.base.BaseActivity;
import com.haxi.mh.utils.dense.TestHttpEncrypt;
import com.haxi.mh.utils.model.LogUtils;

import butterknife.OnClick;

/**
 * 加密解密
 * Created by Han on 2018/9/26
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class DenseActivity extends BaseActivity {

    private TestHttpEncrypt testHttpEncrypt;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_dense;
    }

    @Override
    protected void getData() {
        testHttpEncrypt = new TestHttpEncrypt();
    }


    @OnClick({R.id.tv_dense_test1, R.id.tv_dense_test2, R.id.tv_dense_test3, R.id.tv_dense_test4, R.id.tv_dense_test5})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_dense_test1:
                if (testHttpEncrypt != null) {
                    try {
                        testHttpEncrypt.testGenerateKeyPair();
                    } catch (Exception e) {
                        LogUtils.e("-----e---" + e.getMessage());
                    }
                }

                break;
            case R.id.tv_dense_test2:
                if (testHttpEncrypt != null) {
                    try {
                        testHttpEncrypt.testAppEncrypt1();
                    } catch (Exception e) {
                        LogUtils.e("-----e---" + e.getMessage());
                    }
                }
                break;
            case R.id.tv_dense_test3:
                if (testHttpEncrypt != null) {
                    try {
                        testHttpEncrypt.testServerDecrypt1();
                    } catch (Exception e) {
                        LogUtils.e("-----e---" + e.getMessage());
                    }
                }
                break;
            case R.id.tv_dense_test4:
                if (testHttpEncrypt != null) {
                    try {
                        testHttpEncrypt.testserverEncrypt1();
                    } catch (Exception e) {
                        LogUtils.e("-----e---" + e.getMessage());
                    }
                }
                break;
            case R.id.tv_dense_test5:
                if (testHttpEncrypt != null) {
                    try {
                        testHttpEncrypt.testAppDecrypt1();
                    } catch (Exception e) {
                        LogUtils.e("-----e---" + e.getMessage());
                    }
                }
                break;
        }
    }
}

package com.haxi.mh.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.haxi.mh.R;
import com.haxi.mh.base.BaseActivity;
import com.haxi.mh.utils.ui.toast.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.haxi.mh.constant.Constant.REGISTER_CODE;

/**
 * 登录界面
 * Created by Han on 2018/5/8
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.titlebar_back_iv)
    ImageView titlebarBackIv;
    @BindView(R.id.titlebar_title_tv)
    TextView titlebarTitleTv;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    protected void getData() {
        titlebarBackIv.setVisibility(View.GONE);
        titlebarTitleTv.setText(mActivity.getText(R.string.login_login));

    }


    @OnClick({R.id.bt_login, R.id.bt_registered})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                login();
                break;
            case R.id.bt_registered:
                register();
                break;
        }
    }

    /**
     * 注册
     */
    private void register() {
        startActivityForResult(new Intent(mActivity, RegisterActivity.class), REGISTER_CODE);
    }

    /**
     * 登录
     */
    private void login() {

        String account = etAccount.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            ToastUtils.showShortToast("账号不能为空！");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            ToastUtils.showShortToast("密码不能为空！");
            return;
        }

        //通过接口请求
        ToastUtils.showShortToast("登录成功");
        startActivity(new Intent(mActivity,MainActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REGISTER_CODE) {
                if (data != null) {
                    String account = data.getStringExtra("account");
                    etAccount.setText(account);
                }
            }
        }
    }
}

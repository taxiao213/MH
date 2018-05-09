package com.haxi.mh.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.haxi.mh.R;
import com.haxi.mh.base.BaseActivity;
import com.haxi.mh.utils.model.RegularUtil;
import com.haxi.mh.utils.ui.toast.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 注册界面
 * Created by Han on 2018/5/8
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.titlebar_back_iv)
    ImageView titlebarBackIv;
    @BindView(R.id.titlebar_title_tv)
    TextView titlebarTitleTv;
    @BindView(R.id.et_register_tel)
    EditText etRegisterTel;
    @BindView(R.id.et_register_mail)
    EditText etRegisterMail;
    @BindView(R.id.et_register_name)
    EditText etRegisterName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_register_password)
    EditText etRegisterPassword;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_register;
    }

    @Override
    protected void getData() {
        titlebarBackIv.setVisibility(View.VISIBLE);
        titlebarTitleTv.setText(mActivity.getText(R.string.login_registered));

    }


    @OnClick({R.id.titlebar_back_iv, R.id.bt_rigster})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.titlebar_back_iv:
                finish();
                break;
            case R.id.bt_rigster:
                //注册
                String registerTel = etRegisterTel.getText().toString().trim();
                String registerMail = etRegisterMail.getText().toString().trim();
                String registerName = etRegisterName.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etRegisterPassword.getText().toString().trim();
                if (TextUtils.isEmpty(registerTel)) {
                    ToastUtils.showShortToast("电话号码不能为空！");
                    return;
                }
                if (!RegularUtil.isMobile(registerTel)){
                    ToastUtils.showShortToast("电话号码格式错误！");
                    return;
                }
                if (TextUtils.isEmpty(registerMail)) {
                    ToastUtils.showShortToast("邮箱不能为空！");
                    return;
                }
                if (!RegularUtil.isEmail(registerMail)){
                    ToastUtils.showShortToast("邮箱格式错误！");
                    return;
                }
                if (TextUtils.isEmpty(registerName)) {
                    ToastUtils.showShortToast("昵称不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    ToastUtils.showShortToast("密码不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(confirmPassword)) {
                    ToastUtils.showShortToast("确认密码不能为空！");
                    return;
                }
                if (!TextUtils.equals(password, confirmPassword)) {
                    ToastUtils.showShortToast("两次输入的密码不一致，请重新输入");
                    return;
                }

                //请求网络，注册成功返回
                Intent intent = new Intent();
                intent.putExtra("account",registerName);
                setResult(Activity.RESULT_OK,intent);
                finish();
                break;
        }
    }
}

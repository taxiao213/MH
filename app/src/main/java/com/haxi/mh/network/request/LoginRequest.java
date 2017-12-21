package com.haxi.mh.network.request;

import android.text.TextUtils;

import com.haxi.mh.base.BaseHttpRequest;
import com.haxi.mh.network.HttpRequestApi;
import com.haxi.mh.network.exception.ApiException;
import com.haxi.mh.utils.model.LogUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.json.JSONObject;

/**
 * 登录请求接口
 * Created by Han on 2017/12/20
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class LoginRequest extends BaseHttpRequest<String> {

    private String account;
    private String passWord;

    public LoginRequest(RxAppCompatActivity rxAppCompatActivity) {
        super(rxAppCompatActivity);
        this.account = "100124";
        this.passWord = "1";
    }


    @Override
    public void onNext(String resulte, String method) {
        if (!TextUtils.isEmpty(resulte)) {
            try {
                JSONObject jsonObject = new JSONObject(resulte);
                LogUtils.e(jsonObject.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(ApiException e) {
        LogUtils.e(e.toString());
    }

    @Override
    protected HttpRequestApi reuqest() {
        return new HttpRequestApi(HttpRequestApi.LOGIN).login(account, passWord);
    }

}

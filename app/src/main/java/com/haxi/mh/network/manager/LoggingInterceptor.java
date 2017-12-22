package com.haxi.mh.network.manager;


import com.haxi.mh.utils.model.LogUtils;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * http请求拦截，添加公共参数
 * Created by Han on 2017/12/16
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class LoggingInterceptor implements Interceptor {
    private String user_token;
    private String user_only_account;

    public LoggingInterceptor(String user_token, String user_only_account) {
        this.user_token = user_token;
        this.user_only_account = user_only_account;
    }

    public LoggingInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder newBuilder = request.newBuilder();//用来追加参数
        FormBody formBody = new FormBody.Builder()
                //                .add("user_token", user_token)
                //                .add("user_only_account", user_only_account)
                .build();
        String bodyToString = bodyToString(request.body());
        bodyToString += ((bodyToString.length() > 0) ? "&" : "") + bodyToString(formBody);
        Request build = newBuilder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"), bodyToString))
                .build();

        if (RxRetrofitApp.isDebug()) {
            LogUtils.e("接口--->>>" + " 请求方式 method== " + build.method() + " , url== " + build.url() + "?" + bodyToString(build.body()));
        }

        return chain.proceed(build);
    }

    /**
     * 将公共参数拼装
     *
     * @param request
     * @return
     */
    private String bodyToString(RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (Exception e) {
            return "参数拼接错误";
        }
    }

}

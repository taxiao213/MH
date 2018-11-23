package com.haxi.mh.network.request;

import android.text.TextUtils;

import com.haxi.mh.MyApplication;
import com.haxi.mh.utils.StringUtils;
import com.haxi.mh.utils.dense.HttpEncryptUtil;
import com.haxi.mh.utils.model.LogUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Create by Han on 2018/11/23
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class NetInterceptor implements Interceptor {
    /*http需要加密的接口*/
    private static final String[] HTTP_ENCRYPT = new String[]{};

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder newBuilder = request.newBuilder();
        RequestBody requestBody = request.body();
        String path = request.url().url().getPath();
        if (isEncrypt(path)) {
            //接口加密处理
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("curLoginAcc", "");
                FormBody body = (FormBody) requestBody;
                if (body != null && body.size() > 0) {
                    for (int i = 0; i < body.size(); i++) {
                        jsonObject.put(body.encodedName(i), body.encodedValue(i));
                    }
                }
                Map<String, String> map = HttpEncryptUtil.appEncrypt1(jsonObject.toString());
                FormBody.Builder builder = new FormBody.Builder();
                if (map != null && !map.isEmpty()) {
                    Set<Map.Entry<String, String>> entrySet = map.entrySet();
                    if (entrySet != null && entrySet.size() > 0) {
                        Iterator<Map.Entry<String, String>> iterator = entrySet.iterator();
                        while (iterator.hasNext()) {
                            Map.Entry<String, String> entry = iterator.next();
                            if (entry != null) {
                                String key = entry.getKey();
                                String value = entry.getValue();
                                builder.addEncoded(URLEncoder.encode(key, "UTF-8"), URLEncoder.encode(value, "UTF-8"));
                            }
                        }

                    }
                }
                newBuilder.method(request.method(), builder.build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (requestBody instanceof FormBody) {
                FormBody.Builder builder = new FormBody.Builder();
                FormBody body = (FormBody) requestBody;
                if (body != null && body.size() > 0) {
                    for (int i = 0; i < body.size(); i++) {
                        builder.addEncoded(body.encodedName(i), body.encodedValue(i));
                    }
                }
                //加公共参数
                builder.add("curLoginAcc", "");
                newBuilder.method(request.method(), builder.build());
            }
        }
        Request build = newBuilder.build();
        LogUtils.e("------请求数据接口--->>>", " 请求方式 method== " + build.method() + " , url== " + build.url() + "?" + bodyToString(build.body()));
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
        } catch (final Exception e) {
            return "";
        }
    }

    /**
     * 需要加密的接口
     *
     * @param path url路径
     * @return 是否需要加密处理
     */
    private boolean isEncrypt(String path) {
        if (!TextUtils.isEmpty(path))
            if (HTTP_ENCRYPT != null && HTTP_ENCRYPT.length > 0)
                for (String st : HTTP_ENCRYPT) {
                    if (!TextUtils.isEmpty(st))
                        if (path.contains(st))
                            return true;
                }
        return false;
    }
}

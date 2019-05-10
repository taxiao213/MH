package com.haxi.mh.utils.net;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haxi.mh.constant.Constant;
import com.haxi.mh.model.Test;
import com.haxi.mh.network.HttpService;
import com.haxi.mh.network.manager.RxRetrofitApp;
import com.haxi.mh.utils.model.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ConnectionPool;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * 上传图片工具类
 * Created by Han on 2017/12/29
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class UploadUtil {

    private static UploadUtil uploadUtil = null;

    private UploadUtil() {

    }

    public static UploadUtil getInstance() {
        if (uploadUtil == null) {
            synchronized (UploadUtil.class) {
                if (uploadUtil == null) {
                    uploadUtil = new UploadUtil();
                }
            }
        }
        return uploadUtil;
    }

    /**
     * 获取Retrofit对象
     *
     * @return
     */
    private Retrofit getRetrofit() {
        /**
         * 创建OKHttpClient对象 上传文件拼装公共参数会报错
         */
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                //  .addInterceptor(new AddParameterInterceptor())//http请求拦截器 上传文件拼装公共参数会报错
                .connectionPool(new ConnectionPool(100, 10, TimeUnit.MINUTES))//连接池
                .connectTimeout(6, TimeUnit.SECONDS)//请求超时时间
                .readTimeout(60, TimeUnit.SECONDS)//读取超时时间
                .writeTimeout(60, TimeUnit.SECONDS);//写入超时时间

        //请求接口返回数据拦截器
        if (RxRetrofitApp.isDebug()) {
            builder.addInterceptor(getHttpLoggingInterceptor());
        }

        /**
         * 创建Retrofit对象
         *   //增加返回值为Gson的支持(以实体类返回)
         *   //增加返回值为String的支持
         *   //增加返回值为Oservable<T>的支持
         *   addConverterFactory只能选用一个
         */
        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                //增加返回值为Oservable<T>的支持
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(rx.schedulers.Schedulers.io()))
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(Constant.BASE_URL)
                .build();

        return retrofit;
    }


    /**
     * 创建服务请求
     *
     * @return
     */
    private HttpService getUploadService() {
        return getRetrofit().create(HttpService.class);
    }

    /**
     * 传递请求参数
     *
     * @param partList
     * @return
     */
    private Observable<String> uploadPic(List<MultipartBody.Part> partList) {
        return getUploadService().uploadPic(partList);
    }


    /**
     * 上传图片 multipart/form-data application/octet-stream
     */
    public void upload(File file) {
        MultipartBody builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)//表单类型
                .addFormDataPart("api_key", Constant.FACE_API_KEY)
                .addFormDataPart("api_secret", Constant.FACE_API_SECRET)
                .addFormDataPart("image_file", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file))
                .build();
        List<MultipartBody.Part> parts = builder.parts();
        try {
            uploadPic(parts).subscribeOn(Schedulers.io()).subscribe(new Observer<String>() {
                Disposable disposable = null;

                @Override
                public void onSubscribe(Disposable d) {
                    disposable = d;
                }

                @Override
                public void onNext(String s) {
                    LogUtils.e("onNext--->>" + s);
                    try {
                        JSONObject object = new JSONObject(s);
                        if (object.has("image_id")) {
                            String image_id = object.optString("image_id");
                            LogUtils.e(image_id);
                        }
                        Test json = new Gson().fromJson(s, new TypeToken<Test>() {
                        }.getType());
                        LogUtils.e(json.toString());


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    LogUtils.e("onError--->>" + e.getMessage());
                }

                @Override
                public void onComplete() {
                    LogUtils.e("onComplete--->>");
                    disposable.dispose();
                    disposable = null;
                }
            });
        } catch (Exception e) {
            LogUtils.e("Exception--->>" + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * OkHttpClient 上传文件的接口
     *
     * @param file 文件
     * @return
     */
    public String uploadFile(File file) {
        String url = "https://api-cn.faceplusplus.com/cardpp/v1/ocridcard";
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(getHttpLoggingInterceptor())
                .build();
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image_file", file.getName(), fileBody)
                .addFormDataPart("api_key", Constant.FACE_API_KEY)
                .addFormDataPart("api_secret", Constant.FACE_API_SECRET);

        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();

        Response response = null;
        try {
            response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                LogUtils.e("response--->>>" + response.body().string());
                return response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null && response.body() != null) {
                response.body().close();
            }
        }
        return null;
    }

    /**
     * 请求接口返回数据拦截器
     *
     * @return
     */
    public HttpLoggingInterceptor getHttpLoggingInterceptor() {
        //日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                if (TextUtils.isEmpty(message)) {
                    return;
                }
                String s = message.substring(0, 1);
                if ("{".equals(s) || "[".equals(s)) {
                    //输出日志
                    if (RxRetrofitApp.isDebug()) {
                        LogUtils.e("接口返回数据--->>> " + message);
                    }
                }
            }
        });
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }
}

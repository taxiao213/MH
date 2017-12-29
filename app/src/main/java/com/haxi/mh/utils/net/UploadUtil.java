package com.haxi.mh.utils.net;

import com.haxi.mh.constant.Constant;
import com.haxi.mh.network.HttpService;
import com.haxi.mh.network.manager.AddParameterInterceptor;
import com.haxi.mh.network.manager.RequestLoggingInterceptor;
import com.haxi.mh.network.manager.RxRetrofitApp;
import com.haxi.mh.utils.model.LogUtils;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import okhttp3.ConnectionPool;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
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

    /**
     * 获取Retrofit对象
     *
     * @return
     */
    private Retrofit getRetrofit() {
        /**
         * 创建OKHttpClient对象
         */
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new AddParameterInterceptor())//http请求拦截器 用来拼装公共参数
                .connectionPool(new ConnectionPool(100, 10, TimeUnit.MINUTES))//连接池
                .connectTimeout(6, TimeUnit.SECONDS)//请求超时时间
                .readTimeout(60, TimeUnit.SECONDS)//读取超时时间
                .writeTimeout(60, TimeUnit.SECONDS);//写入超时时间

        //请求接口返回数据拦截器
        if (RxRetrofitApp.isDebug()) {
            builder.addInterceptor(RequestLoggingInterceptor.getInstace().getHttpLoggingInterceptor());
        }

        /**
         * 创建Retrofit对象
         *   //增加返回值为Gson的支持(以实体类返回)
         *   //增加返回值为String的支持
         *   //增加返回值为Oservable<T>的支持
         *   addConverterFactory只能选用一个
         */
        //增加返回值为Oservable<T>的支持
        //增加返回值为String的支持
        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                //增加返回值为Oservable<T>的支持
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(Constant.BASE_URL)
                .build();

        return retrofit;
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
     * 创建服务请求
     *
     * @return
     */
    private HttpService getUploadService() {
        return getRetrofit().create(HttpService.class);
    }

    private Call<Result<String>> uploadPic(List<MultipartBody.Part> partList) {
        return getUploadService().uploadPic(partList);
    }

    /**
     * 上传图片
     */
    public void upload(File file) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)//表单类型
                .addFormDataPart("api_key", Constant.FACE_API_KEY)
                .addFormDataPart("api_secret", Constant.FACE_API_SECRET);
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("image_file", file.getName(), body);//image_file 后台接收图片流的参数名
        List<MultipartBody.Part> parts = builder.build().parts();
        try {
            uploadPic(parts).enqueue(new Callback<Result<String>>() {
                @Override
                public void onResponse(Call<Result<String>> call, Response<Result<String>> response) {
                    LogUtils.e("onResponse--->>" + response.body().toString());
                }

                @Override
                public void onFailure(Call<Result<String>> call, Throwable t) {
                    LogUtils.e("onFailure--->>" + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

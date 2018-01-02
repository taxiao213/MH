package com.haxi.mh.network;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * 用来创建Retrofit 的接口
 * Created by Han on 2017/12/18
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public interface HttpService {

    //登录接口
    @FormUrlEncoded
    @POST("userLogin/login.action")
    Observable<String> login(@Field("account") String account, @Field("passwd") String passWord);

    //获取菜单
    @FormUrlEncoded
    @POST("jisuapi/search")
    Observable<String> getMenu(@FieldMap Map<String, Object> map);

    //上传图片 OCR ID Card API Call<Result<String>>
    @Multipart
    @POST("ocridcard")
    Observable<String> uploadPic(@Query("api_key") String api_key, @Query("api_secret") String api_secret, @Part List<MultipartBody.Part> partList);

    @Multipart
    @POST("userLogin/login.action")
    Observable<String> login(@Part List<MultipartBody.Part> partList);


}

package com.haxi.mh.network;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

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
    Observable<String> login(@Field("account") String account, @Field("passwd") String passWord); //登录接口

    //获取菜单
    @FormUrlEncoded
    @POST("jisuapi/search")
    Observable<String> getMenu(@FieldMap Map<String, Object> map);


}

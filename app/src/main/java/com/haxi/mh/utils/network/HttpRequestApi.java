package com.haxi.mh.utils.network;

import io.reactivex.Observable;
import retrofit2.Retrofit;

/**
 * http请求接口参数设置
 * Created by Han on 2017/12/18
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 */

public class HttpRequestApi extends BaseApi {

    private boolean progress;

    /**
     * 创建HttpRequestApi
     *
     * @param apiType
     */
    public HttpRequestApi(int apiType) {
        this.apiType = apiType;
    }

    /**
     * 接口编号
     */
    private int apiType;

    //登录账号
    private String account;
    //登录密码
    private String passWord;

    //------------------------------接口常量---------------------------//

    /**
     * 登录
     */
    public static final int LOGIN = 1001;


    /**
     * 登录
     *
     * @param account  账号
     * @param passWord 密码
     * @return
     */
    public HttpRequestApi login(String account, String passWord) {
        setShowProgress(true);//设置是否显示加载圈
        setCancle(false);//设置是否能取消加载圈
        setCache(false);//设置是否有缓存
        this.account = account;
        this.passWord = passWord;
        return this;
    }

    @Override
    protected Observable getObservable(Retrofit retrofit) {
        HttpService service = retrofit.create(HttpService.class);
        switch (apiType) {
            case LOGIN:
                return service.login(account, passWord);
            default:
                return null;
        }
    }


}

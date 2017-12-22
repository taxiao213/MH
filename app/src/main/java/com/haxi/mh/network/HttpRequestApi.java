package com.haxi.mh.network;

import com.haxi.mh.network.manager.BaseApi;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Retrofit;

/**
 * http请求接口参数设置
 * Created by Han on 2017/12/18
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class HttpRequestApi extends BaseApi {


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

    /**
     * 接口为Map
     */
    private Map<String, Object> mParams;

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
     * 获取菜谱
     */
    public static final int GET_MENU = 1002;


    /**
     * 登录
     *
     * @param account  账号
     * @param passWord 密码
     * @return
     */
    public HttpRequestApi login(String account, String passWord) {
        setShowProgress(true);//设置是否显示加载框
        setCancle(false);//设置是否能取消加载框
        setCache(true);//设置是否有缓存
        setBaseUrl("http://www.zkhonry.com:9000/zkhonry-mobile-interface/");
        this.account = account;
        this.passWord = passWord;
        return this;
    }

    /**
     * 获取菜单
     *
     * @param map
     * @return
     */
    public HttpRequestApi getMenu(Map<String, Object> map) {
        setShowProgress(true);
        setCache(false);
        setCancle(false);
        setBaseUrl("https://way.jd.com/");
        this.mParams = map;
        return this;
    }

    @Override
    protected Observable getObservable(Retrofit retrofit) {
        HttpService service = retrofit.create(HttpService.class);
        switch (apiType) {
            case LOGIN:
                return service.login(account, passWord);
            case GET_MENU:
                return service.getMenu(mParams);
            default:
                return null;
        }
    }


}

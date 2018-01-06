package com.haxi.mh.ui.activity;

import android.webkit.JavascriptInterface;

/**
 * 与js交互
 * Created by Han on 2018/01/05
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class AndroidtoJs extends Object {

    private WebActivity web;

    public AndroidtoJs(WebActivity web) {
        this.web = web;
    }

    // 定义JS需要调用的方法
    // 被JS调用的方法必须加入@JavascriptInterface注解
    @JavascriptInterface
    public void click(String msg) {

    }

    /**
     * H5控制后退按键
     */
    @JavascriptInterface
    public void newBack(String back) {
        web.setKeyBack(back);
    }


}

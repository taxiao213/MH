package com.haxi.mh.mvp.entity;

/**
 * Created by Han on 2018/8/30
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class WxInfoDao {

    private String name;
    private int account;

    public WxInfoDao(String name, int account) {
        this.name = name;
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public int getAccount() {
        return account;
    }
}

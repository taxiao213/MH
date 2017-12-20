package com.haxi.mh.network.cookie;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * post请求缓存数据
 * Created by Han on 2017/12/18
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
@Entity
public class CookieResulte {
    @Id
    private Long id;
    /*url*/
    private String url;
    /*返回结果*/
    private String resulte;
    /*时间*/
    private Long time;

    public CookieResulte(String url, String resulte, Long time) {
        this.url = url;
        this.resulte = resulte;
        this.time = time;
    }

    @Generated(hash = 1991833239)
    public CookieResulte(Long id, String url, String resulte, Long time) {
        this.id = id;
        this.url = url;
        this.resulte = resulte;
        this.time = time;
    }

    @Generated(hash = 2104390000)
    public CookieResulte() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getResulte() {
        return this.resulte;
    }

    public void setResulte(String resulte) {
        this.resulte = resulte;
    }

    public Long getTime() {
        return this.time;
    }

    public void setTime(Long time) {
        this.time = time;
    }


}

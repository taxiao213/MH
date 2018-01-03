package com.haxi.mh.model;

import java.util.List;

/**
 * 测试用
 * Created by Han on 2018/1/3
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class Test {

    /**
     * image_id : lfT7kkH1CKLUVU0L9DWMNw==
     * request_id : 1514979887,8fe27db1-bf4d-47f3-b0ad-b0e7143920b8
     * cards : []
     * time_used : 256
     */

    private String image_id;
    private String request_id;
    private int time_used;
    private List<?> cards;

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public int getTime_used() {
        return time_used;
    }

    public void setTime_used(int time_used) {
        this.time_used = time_used;
    }

    public List<?> getCards() {
        return cards;
    }

    public void setCards(List<?> cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        return "Test{" +
                "image_id='" + image_id + '\'' +
                ", request_id='" + request_id + '\'' +
                ", time_used=" + time_used +
                ", cards=" + cards +
                '}';
    }
}

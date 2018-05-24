package com.haxi.mh.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 测试用 实现Pracelable 序列化
 * Serializable方式简单，会把整个对象序列化，效率比Pracelable效率低
 * Created by Han on 2018/1/3
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class Test implements Parcelable {

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

    protected Test() {
    }

    protected Test(Parcel in) {
        image_id = in.readString();
        request_id = in.readString();
        time_used = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image_id);
        dest.writeString(request_id);
        dest.writeInt(time_used);
    }

    public static final Creator<Test> CREATOR = new Creator<Test>() {
        @Override
        public Test createFromParcel(Parcel in) {
            Test test = new Test();
            test.image_id = in.readString();
            test.request_id = in.readString();
            test.time_used = in.readInt();
            return test;
        }

        @Override
        public Test[] newArray(int size) {
            return new Test[size];
        }
    };


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

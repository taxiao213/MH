package com.haxi.mh.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 测试数据库greenDao
 * Created by Han on 2017/12/11.
 */
@Entity
public class Person {

    @Id(autoincrement = true)
    private long id;

    private String name;

    private String value;

    @Generated(hash = 1024547259)
    public Person() {
    }

    @Generated(hash = 1922299209)
    public Person(long id, String name, String value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

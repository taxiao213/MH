package com.haxi.mh.utils.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.haxi.mh.MyApplication;

import java.io.File;

/**
 * SharedPreferences 存储数据
 * Created by Han on 2017/12/11
 * Email:yin13753884368@163.com
 */
public class SharedPreHelper {

    private static SharedPreferences sp;
    private static SharedPreHelper helper;
    /**
     * 程序中可以同时存在多个SharedPreferences数据， 根据SharedPreferences的名称就可以拿到对象
     **/
    private final String SP_NAME = "sharedPre_name";

    /**
     * SharedPreferences中储存数据的路径
     **/
    public final static String DATA_URL = "/data/data/";

    public final static String SHARED_MAIN_XML = "sharedPre_name.xml";

    public static SharedPreHelper getInstance() {
        if (helper == null) {
            synchronized (SharedPreHelper.class) {
                if (helper == null) {
                    helper = new SharedPreHelper();
                }
            }
        }
        return helper;
    }

    private SharedPreHelper() {
        sp = MyApplication.mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public void setData(String key, Boolean value) {
        sp.edit().putBoolean(key, value).commit();

    }

    public Boolean getBooleanData(String key) {
        return sp.getBoolean(key, false);
    }

    public void setData(String key, Float value) {
        sp.edit().putFloat(key, value).commit();

    }

    public Float getFloatData(String key) {
        return sp.getFloat(key, 0);
    }

    public void setData(String key, Integer value) {
        sp.edit().putInt(key, value).commit();

    }

    public int getIntegerData(String key) {
        return sp.getInt(key, 0);
    }

    public void setData(String key, String value) {
        sp.edit().putString(key, value).commit();

    }

    public String getStringData(String key) {
        return sp.getString(key, "");
    }

    public void setData(String key, Long value) {
        sp.edit().putLong(key, value).commit();

    }

    public Long getLongData(String key) {
        return sp.getLong(key, 0);
    }

    /**
     * SharedPreferences中保存的内容
     **/
    public void delData(String key) {
        sp.edit().remove(key).commit();
    }


    /**
     * 总结一下：要想及时并安全清除SharedPreferences一定要使用Editor去clear并commit，
     * 不要直接暴力地删除其xml文件。
     *
     * @param packageName
     */
    public void delFile(String packageName) {
        /** 删除SharedPreferences文件 **/
        File file = new File(DATA_URL + packageName + "/shared_prefs", SHARED_MAIN_XML);
        if (file.exists()) {
            file.delete();
            sp.edit().clear().commit();
        }
    }
}

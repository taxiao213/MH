package com.haxi.mh.utils.model;

/**
 * SharedPreHelper工具类
 * Created by Han on 2018/5/4
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class UserUtils {

    public static void setHuaWeiNotifyId(Integer huaWeiNotifyId) {
        SharedPreHelper.getInstance().setData("huaWeiNotifyId", huaWeiNotifyId);
    }

    public static int getHuaWeiNotifyId() {
        return SharedPreHelper.getInstance().getIntegerData("huaWeiNotifyId");
    }
}

package com.haxi.mh.utils.im;


import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.haxi.mh.utils.model.LogUtils;
import com.huawei.hms.support.api.push.PushReceiver;

/**
 * 华为推送
 * Created by Han on 2018/10/22
 * Email:yin13753884368@163.com
 */

public class IMHuaWeiReceiver extends PushReceiver {


    /**
     * 连接上华为服务时会调用,可以获取token值
     *
     * @param context
     * @param token
     * @param extras
     */
    @Override
    public void onToken(Context context, String token, Bundle extras) {
        String belongId = extras.getString("belongId");
       /* String brand = UserPreferences.getInstance(context).getPhoneBrand();
        if (brand.equals(IMConstants.BRAND_HUAWEI)) {
            UserPreferences.getInstance(context).saveTokenHuawei(token);
            context.sendBroadcast(new Intent(IMConstants.ACTION_TOKEN_REFRESH));
        }*/
        LogUtils.e("--push-- 华为 token= " + token + " belongId = " + belongId);
    }

    /**
     * 透传消息的回调方法
     *
     * @param context
     * @param msg
     * @param bundle
     * @return
     */
    @Override
    public boolean onPushMsg(Context context, byte[] msg, Bundle bundle) {
        try {
            LogUtils.e("--push-- 华为 msg== " + new String(msg, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 自定义的消息的回调方法
     *
     * @param context
     * @param event
     * @param extras
     */
    @Override
    public void onEvent(Context context, PushReceiver.Event event, Bundle extras) {
        if (Event.NOTIFICATION_OPENED.equals(event) || Event.NOTIFICATION_CLICK_BTN.equals(event)) {
            int notifyId = extras.getInt(BOUND_KEY.pushNotifyId, 0);
            if (0 != notifyId) {
                NotificationManager manager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(notifyId);
            }
            LogUtils.e("--push-- 华为 onEvent== ", extras.getString(BOUND_KEY.pushMsgKey));
        }
        super.onEvent(context, event, extras);
    }

    /**
     * 连接状态的回调方法
     *
     * @param context
     * @param pushState
     */
    @Override
    public void onPushState(Context context, boolean pushState) {
        try {
            LogUtils.e("--push-- 华为 onPushState== ", (pushState ? "Connected" : "Disconnected"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

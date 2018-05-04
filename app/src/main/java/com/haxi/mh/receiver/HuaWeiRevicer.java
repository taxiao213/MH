package com.haxi.mh.receiver;


import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.huawei.hms.support.api.push.PushReceiver;

/**
 * 华为推送
 * Created by Han on 2017/12/27
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class HuaWeiRevicer extends PushReceiver {


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
        Log.e("------华为onToken", "token = " + token + ",belongId = " + belongId);
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
            int notifyId = bundle.getInt(BOUND_KEY.pushNotifyId, 0);
            Log.e("------华为 onPushMsg", new String(msg, "UTF-8") + "notifyId== " + notifyId);

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
        Log.e("------华为 onEvent", extras.getInt(BOUND_KEY.pushNotifyId, 0) + "");

        if (Event.NOTIFICATION_OPENED.equals(event) || Event.NOTIFICATION_CLICK_BTN.equals(event)) {
            int notifyId = extras.getInt(BOUND_KEY.pushNotifyId, 0);
            if (0 != notifyId) {
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (manager != null) {
                    manager.cancel(notifyId);
                }
            }
            Log.e("------华为 onEvent", extras.getString(BOUND_KEY.pushMsgKey));
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
            Log.e("------华为 onPushState", (pushState ? "Connected" : "Disconnected"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

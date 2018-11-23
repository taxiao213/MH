package com.haxi.mh.utils.im;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.haxi.mh.ui.activity.MainActivity;


/**
 * 消息提醒的广播
 * Created by Han on 2018/10/18
 * Email:yin13753884368@163.com
 */

public class IMNoticeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case IMConstants.MSG_BROADCAST_TYPE_UPDATE_APP:
                        //APP升级通知
                        break;
                    case IMConstants.MSG_BROADCAST_TYPE_KNOWLEDGE_NOTIFICATION:
                        //新闻通知
                        String title = intent.getStringExtra(IMConstants.INTENT_TITLE);
                        String url = intent.getStringExtra(IMConstants.INTENT_URL);
                        startWebActivity(context, title, IMConstants.WEB_URL_KNOWLEDGE + url);
                        break;
                    case IMConstants.MSG_BROADCAST_TYPE_RECHARGE_REMINDER:
                        //充值提醒
                        break;
                    case IMConstants.MSG_BROADCAST_TYPE_MEDICAL_REMINDER:
                        //就诊提醒
                        break;
                    case IMConstants.MSG_BROADCAST_TYPE_CALL_NOTIFICATION:
                        //就诊提醒
                        break;
                    case IMConstants.MSG_BROADCAST_TYPE_REGISTRATION_APPOINTMENT_REMINDER:
                        //挂号预约提醒
                        break;
                    case IMConstants.MSG_BROADCAST_TYPE_RETREAT_REMINDER:
                        //退号成功通知
                        break;
                    case IMConstants.MSG_BROADCAST_TYPE_CANCEL_REGISTRATION_APPOINTMENT_REMINDER:
                        //取消预约挂号通知
                        break;
                    case IMConstants.MSG_BROADCAST_TYPE_WAITING_FOR_CALL_REMINDER:
                        //候诊叫号提醒
                        break;
                    case IMConstants.MSG_BROADCAST_TYPE_ADVICE_REMINDER:
                        //意见反馈通知
                        String title1 = intent.getStringExtra(IMConstants.INTENT_TITLE);
                        String url1 = intent.getStringExtra(IMConstants.INTENT_URL);
                        startWebActivity(context, title1, IMConstants.baseUrl + url1);
                        break;
                }
            }
        }
    }

    /**
     * 跳转候诊叫号界面
     *
     * @param context
     * @param patientId
     */
    private void startHWaitingCallActivity(Context context, String patientId) {
    }

    /**
     * 跳转H5
     *
     * @param context 上下文
     * @param title   标题
     * @param url     h5路径
     */
    private void startWebActivity(Context context, String title, String url) {
        if (context != null) {
        }
    }

    /**
     * 跳转到首页
     *
     * @param context      上下文
     * @param tag_fragment 相应的fragment
     * @param type         2升级APP通知,1其他
     */
    private void startMainActivity(Context context, String tag_fragment, int type) {
        if (context != null) {
            Intent updateIntent = new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(updateIntent);
            Intent intent = new Intent(IMConstants.ACTION_APP_MAIN);
            intent.putExtra("tag_fragment", tag_fragment);
            context.sendBroadcast(intent);
            IMUtils.getInstance().getMessage(IMUtils.IM_MESSAGE_TYPE1);
        }
    }
}

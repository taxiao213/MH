package com.haxi.mh.utils.im;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.haxi.mh.MyApplication;
import com.haxi.mh.R;
import com.haxi.mh.model.db.IMMessageInfo;
import com.haxi.mh.utils.StringUtils;


/**
 * 消息通知栏提醒
 * Created by Han on 2018/10/15
 * Email:yin13753884368@163.com
 */
public class IMNotification {
    private static IMNotification myNotification;
    private NotificationManager mNotification;
    private static final String NOTIFICATION_CHANNEL_ID = "20188";
    private static final String NOTIFICATION_CHANNEL_NAME = "com.honry.hrpatient";
    private static final String NOTIFICATION_CHANNEL_DESCRIPTION = "hrpatient";

    public IMNotification() {

    }

    public void CancelNotification(int mNotificationSign) {
        if (mNotification != null) {
            mNotification.cancel(mNotificationSign);
        }
    }

    public static IMNotification getInstance() {
        if (myNotification == null) {
            synchronized (IMNotification.class) {
                if (myNotification == null) {
                    myNotification = new IMNotification();
                }
            }
        }
        return myNotification;
    }

    /**
     * 通知栏提醒
     *
     * @param info  IMMessageInfo
     * @param mSign 标签 用于取消
     * @param type  类型(分类用)
     *              APP升级通知 {@link IMConstants#MSG_BROADCAST_TYPE_UPDATE_APP}
     *              新闻通知  msgtip_type 0 {@link IMConstants#MSG_BROADCAST_TYPE_KNOWLEDGE_NOTIFICATION} 新闻通知比较特殊在不登陆时也可以看到，特殊处理
     *              充值提醒  msgtip_type 1 {@link IMConstants#MSG_BROADCAST_TYPE_RECHARGE_REMINDER}
     *              就诊提醒  msgtip_type 2 {@link IMConstants#MSG_BROADCAST_TYPE_MEDICAL_REMINDER}
     *              取号成功通知  msgtip_type 3 {@link IMConstants#MSG_BROADCAST_TYPE_CALL_NOTIFICATION}
     *              挂号预约提醒  msgtip_type 4 {@link IMConstants#MSG_BROADCAST_TYPE_REGISTRATION_APPOINTMENT_REMINDER}
     *              退号成功通知  msgtip_type 5 {@link IMConstants#MSG_BROADCAST_TYPE_RETREAT_REMINDER}
     *              取消预约挂号通知  msgtip_type 6 {@link IMConstants#MSG_BROADCAST_TYPE_CANCEL_REGISTRATION_APPOINTMENT_REMINDER}
     *              候诊叫号提醒  msgtip_type 7 {@link IMConstants#MSG_BROADCAST_TYPE_WAITING_FOR_CALL_REMINDER}
     *              意见反馈通知  msgtip_type 8 {@link IMConstants#MSG_BROADCAST_TYPE_ADVICE_REMINDER}
     */
    public void showNotice(IMMessageInfo info, int mSign, String type) {
        String title = StringUtils.null2Length0(info.getMsg_menu_name());//标题
        String content = StringUtils.null2Length0(info.getMain_title());//标题下内容
        IMUtils.getInstance().playVideo();
        Intent mainIntent = new Intent();
        mNotification = (NotificationManager) MyApplication.getMyApplication().getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
        switch (type) {
            case IMConstants.MSG_BROADCAST_TYPE_UPDATE_APP:
                mainIntent.setAction(IMConstants.MSG_BROADCAST_TYPE_UPDATE_APP);
                break;
            case IMConstants.MSG_BROADCAST_TYPE_KNOWLEDGE_NOTIFICATION:
                mainIntent.setAction(IMConstants.MSG_BROADCAST_TYPE_KNOWLEDGE_NOTIFICATION);
                mainIntent.putExtra(IMConstants.INTENT_TITLE, title);
                mainIntent.putExtra(IMConstants.INTENT_URL, StringUtils.null2Length0(info.getStr2()));
                break;
            case IMConstants.MSG_BROADCAST_TYPE_RECHARGE_REMINDER:
                mainIntent.setAction(IMConstants.MSG_BROADCAST_TYPE_RECHARGE_REMINDER);
                break;
            case IMConstants.MSG_BROADCAST_TYPE_MEDICAL_REMINDER:
                mainIntent.setAction(IMConstants.MSG_BROADCAST_TYPE_MEDICAL_REMINDER);
                break;
            case IMConstants.MSG_BROADCAST_TYPE_CALL_NOTIFICATION:
                mainIntent.setAction(IMConstants.MSG_BROADCAST_TYPE_CALL_NOTIFICATION);
                break;
            case IMConstants.MSG_BROADCAST_TYPE_REGISTRATION_APPOINTMENT_REMINDER:
                mainIntent.setAction(IMConstants.MSG_BROADCAST_TYPE_REGISTRATION_APPOINTMENT_REMINDER);
                break;
            case IMConstants.MSG_BROADCAST_TYPE_RETREAT_REMINDER:
                mainIntent.setAction(IMConstants.MSG_BROADCAST_TYPE_RETREAT_REMINDER);
                break;
            case IMConstants.MSG_BROADCAST_TYPE_CANCEL_REGISTRATION_APPOINTMENT_REMINDER:
                mainIntent.setAction(IMConstants.MSG_BROADCAST_TYPE_CANCEL_REGISTRATION_APPOINTMENT_REMINDER);
                break;
            case IMConstants.MSG_BROADCAST_TYPE_WAITING_FOR_CALL_REMINDER:
                mainIntent.setAction(IMConstants.MSG_BROADCAST_TYPE_WAITING_FOR_CALL_REMINDER);
                break;
            case IMConstants.MSG_BROADCAST_TYPE_ADVICE_REMINDER:
                mainIntent.setAction(IMConstants.MSG_BROADCAST_TYPE_ADVICE_REMINDER);
                mainIntent.putExtra(IMConstants.INTENT_TITLE, title);
                break;
            default:
                break;
        }


        /* (Context context, int requestCode, Intent intent, int flags  :
         * requestCode唯一：则不通广播传递内容唯一，点击每条广播获取相应的信息，需要设置不通的值*/
        PendingIntent mainPendingIntent = PendingIntent.getBroadcast(MyApplication.getMyApplication(), mSign, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyApplication.getMyApplication(), NOTIFICATION_CHANNEL_ID)
                //设置小图标
                .setSmallIcon(R.mipmap.mh_icon)
                //设置通知标题
                .setContentTitle(title)
                //设置通知内容 这块用title+时间
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(null)//禁止使用系统通知声音
                .setContentIntent(mainPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        mNotification.notify(mSign, builder.build());
    }

    /**
     * The notification priority, set by setPriority(). The priority determines how intrusive the notification should be on Android 7.1 and lower. (For Android 8.0 and higher, you must instead set the channel importance—shown in the next section.)
     * NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
     * .setPriority(NotificationCompat.PRIORITY_DEFAULT);
     * Notice that the NotificationCompat.Builder constructor requires that you provide a channel ID. This is required for compatibility with Android 8.0 (API level 26) and higher, but is ignored by older versions.
     */
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(NOTIFICATION_CHANNEL_DESCRIPTION);
            channel.setSound(null, null);//禁止使用系统通知声音
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager) MyApplication.getMyApplication().getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null)
                notificationManager.createNotificationChannel(channel);
        }
    }

}

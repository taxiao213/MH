package com.haxi.mh.ui.widget;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.haxi.mh.MyApplication;
import com.haxi.mh.R;

/**
 * 标题栏通知
 * Created by Han on 2018/01/15
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class Notification {

    private static Notification myNotification;
    private NotificationManager mNotification;
    private static final String NOTIFICATION_CHANNEL_ID = "2018";
    private static final String NOTIFICATION_CHANNEL_NAME = "com.honry.hrpatient";
    private static final String NOTIFICATION_CHANNEL_DESCRIPTION = "hrpatient";

    public Notification() {

    }

    public void CancelNotification(int mNotificationSign) {
        if (mNotification != null) {
            mNotification.cancel(mNotificationSign);
        }
    }

    public static Notification getInstance() {
        if (myNotification == null) {
            synchronized (Notification.class) {
                if (myNotification == null) {
                    myNotification = new Notification();
                }
            }
        }
        return myNotification;
    }

    /**
     * 通知栏提醒
     *
     * @param title   标题
     * @param content 需要显示的内容
     * @param mSign   标签 用于取消
     * @param type    类型(分类用)
     */
    public void showNotice(String title, String content, int mSign, String type) {
        Intent mainIntent = new Intent();
        mNotification = (NotificationManager) MyApplication.getMyApplication().getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();


        /* (Context context, int requestCode, Intent intent, int flags  :
         * requestCode唯一：则不通广播传递内容唯一，点击每条广播获取相应的信息，需要设置不通的值*/
        PendingIntent mainPendingIntent = PendingIntent.getBroadcast(MyApplication.getMyApplication(), mSign, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyApplication.getMyApplication(), NOTIFICATION_CHANNEL_ID)
                //设置小图标
                .setSmallIcon(R.mipmap.ic_launcher)
                //设置通知标题
                .setContentTitle(title)
                //设置通知内容 这块用title+时间
                .setContentText(content)
                .setAutoCancel(true)
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
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager) MyApplication.getMyApplication().getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null)
                notificationManager.createNotificationChannel(channel);
        }
    }
}

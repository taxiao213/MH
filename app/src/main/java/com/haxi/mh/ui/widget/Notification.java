package com.haxi.mh.ui.widget;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.haxi.mh.R;
import com.haxi.mh.ui.activity.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 标题栏通知
 * Created by Han on 2018/01/15
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class Notification {

    public static Notification notification;
    private NotificationManager mNotification;
    private Context context;

    public static Notification getInstances(Context context) {
        if (notification == null) {
            synchronized (Notification.class) {
                if (notification == null) {
                    notification = new Notification(context);
                }
            }
        }
        return notification;
    }

    public Notification(Context context) {
        this.context = context;
    }

    /**
     *
     * @param title 显示的标题
     * @param createtime 显示的时间
     * @param mNotificationSign 取消通知栏的标志
     */
    public void showNotice(String title, String createtime, int mNotificationSign) {

        Intent mainIntent = new Intent();
        mNotification = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mainIntent.setClass(context, MainActivity.class);

        /**
         * (Context context, int requestCode, Intent intent, int flags  :
         * requestCode唯一：则不通广播传递内容唯一，点击每条广播获取相应的信息，需要设置不通的值
         */
        PendingIntent mainPendingIntent = PendingIntent.getBroadcast(context, mNotificationSign, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                //设置小图标
                .setSmallIcon(R.drawable.mh_icon)
                //设置通知标题 标题写死
                .setContentTitle(title)
                //设置通知内容 这块用 title +时间
                .setContentText(getTime(createtime))
                .setAutoCancel(true)
                .setContentIntent(mainPendingIntent);
        mNotification.notify(mNotificationSign, builder.build());
    }

    /**
     * 取消提醒
     *
     * @param mNotificationSign
     */
    public void CancelNotification(int mNotificationSign) {
        if (mNotification != null) {
            mNotification.cancel(mNotificationSign);
        }
    }

    private String getTime(String s) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
            long time = format.parse(s).getTime();
            long dayTime = (long) 86400000;//1天的时间 *6
            long nowTime = System.currentTimeMillis(); //系统时间
            SimpleDateFormat formatNow = new SimpleDateFormat("HH:mm");
            SimpleDateFormat formatToday = new SimpleDateFormat("MM月dd日");
            SimpleDateFormat formatWeek = new SimpleDateFormat("EEEE");
            SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
            if (formatYear.parse(formatYear.format(time)).getTime() == formatYear.parse(formatYear.format(nowTime)).getTime()) {
                if (nowTime - time <= dayTime) {
                    return formatNow.format(time);
                } else if (nowTime - time > dayTime && nowTime - time <= dayTime * 2) {
                    return formatNow.format("昨天");
                } else if (nowTime - time > dayTime * 2 && nowTime - time <= dayTime * 6) {
                    //星期几
                    return (formatWeek.format(time));
                } else {
                    return formatToday.format(time);
                }
            } else {
                return format.format(time);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}

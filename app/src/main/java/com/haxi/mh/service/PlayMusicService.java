package com.haxi.mh.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.haxi.mh.utils.model.LogUtils;
import com.haxi.mh.utils.ui.UIUtil;

/**
 * 前台Service(多进程)
 * Created by Han on 2018/1/19
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 *   startForeground(0, getNotification()); 为0时不会显示通知栏
 */
public class PlayMusicService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(0, getNotification());
//        ConverServiceConnection connection = new ConverServiceConnection(this);
//        this.bindService(new Intent(this, HelpService.class), connection, BIND_AUTO_CREATE);
        LogUtils.e("mh-->>PlayMusicService：onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    public Notification getNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.mh_icon)
//                .setContentTitle("前台service")
//                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.mh_icon))
                .setWhen(System.currentTimeMillis());
        return builder.build();
    }

    @Override
    public void onDestroy() {
        new Thread(){
            @Override
            public void run() {
                startService();
                super.run();
            }
        }.start();
        LogUtils.e("mh-->>PlayMusicService：onDestroy()");
        super.onDestroy();

    }

    /**
     * 开启服务
     */
    private void startService() {
        LogUtils.e("mh-->>PlayMusicService--->>startService()");
        if (!UIUtil.isServiceRunning(this, PlayMusicService.class.getName())) {
            startService(new Intent(this, PlayMusicService.class));
            LogUtils.e("mh-->>PlayMusicService--->>startService()开启");
        }
    }
}

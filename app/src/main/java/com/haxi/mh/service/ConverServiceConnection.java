package com.haxi.mh.service;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.haxi.mh.utils.model.LogUtils;

/**
 * 前台服务辅助，用来消除通知栏(多进程)
 * Created by Han on 2018/1/19
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class ConverServiceConnection implements ServiceConnection {
    private PlayMusicService musicService;

    public ConverServiceConnection(PlayMusicService playMusicService) {
        this.musicService = playMusicService;
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        LogUtils.e("ConverServiceConnection：onServiceConnected()");
        HelpService service = ((HelpService.LocalBinder) iBinder).getService();
        service.startForeground(100, getNotification(service));
        service.stopForeground(true);
        if (musicService != null) {
            musicService.unbindService(this);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        LogUtils.e("ConverServiceConnection：onServiceDisconnected()");
    }

    public Notification getNotification(Service service) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(service)
//                .setSmallIcon(R.mipmap.mh_icon)
//                .setContentTitle("前台service")
//                .setLargeIcon(BitmapFactory.decodeResource(service.getResources(), R.mipmap.mh_icon))
                .setWhen(System.currentTimeMillis());
        return builder.build();
    }
}

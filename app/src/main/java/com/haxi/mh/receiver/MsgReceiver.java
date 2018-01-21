package com.haxi.mh.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.haxi.mh.service.PlayMusicService;
import com.haxi.mh.utils.ui.UIUtil;

/**
 * 收取广播
 * Created by Han on 2018/1/19
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class MsgReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            Log.e("mh-->>MsgReceiver: ", action);
            if (!UIUtil.isServiceRunning(context, PlayMusicService.class.getName())) {
                context.startService(new Intent(context, PlayMusicService.class));
            }
        }
    }
}

package com.haxi.mh.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.facebook.stetho.common.LogUtil;
import com.haxi.mh.constant.Constant;
import com.haxi.mh.service.PlayMusicService;
import com.haxi.mh.utils.ui.UIUtil;

/**
 * 收取广播 动态注册去监听物理按键 短按Home键 长按Home键 或者 activity切换键  // samsung 长按Home键
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
            switch (action) {
                case Intent.ACTION_CLOSE_SYSTEM_DIALOGS:
                    String reason = intent.getStringExtra(Constant.SYSTEM_DIALOG_REASON_KEY);
                    LogUtil.e("mh-->>MsgReceiver-->>reason:" + reason);
                    if (Constant.SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
                        // 短按Home键
                    } else if (Constant.SYSTEM_DIALOG_REASON_RECENT_APPS.equals(reason)) {
                        // 长按Home键 或者 activity切换键
                    } else if (Constant.SYSTEM_DIALOG_REASON_LOCK.equals(reason)) {
                        // 锁屏
                    } else if (Constant.SYSTEM_DIALOG_REASON_ASSIST.equals(reason)) {
                        // samsung 长按Home键
                    }
                    break;
                case Intent.ACTION_SCREEN_ON: //屏幕变亮
                    break;
                case Intent.ACTION_SCREEN_OFF: //屏幕变暗
                    break;
                case Intent.ACTION_USER_PRESENT: //屏幕解锁
                    break;
            }
        }
    }
}

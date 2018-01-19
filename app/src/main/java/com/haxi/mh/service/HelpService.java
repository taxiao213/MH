package com.haxi.mh.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.haxi.mh.utils.model.LogUtils;

/**
 * 台前Service辅助(多进程)
 * Created by Han on 2018/1/19
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class HelpService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.e("HelpService: onBind()");
        return new LocalBinder();
    }

    public class LocalBinder extends Binder {
        public HelpService getService() {
            LogUtils.e("HelpService: getService()");
            return HelpService.this;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.e("HelpService: onDestroy()");
    }
}

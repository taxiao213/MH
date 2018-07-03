package com.haxi.mh.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Binder线程池
 * Created by Han on 2018/6/27
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class BinderPoolService extends Service {
    private Binder binderPoolIml = new BinderPool.BinderPoolIml();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binderPoolIml;
    }


}

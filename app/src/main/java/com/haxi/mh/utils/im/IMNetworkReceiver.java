package com.haxi.mh.utils.im;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PowerManager;


import com.haxi.mh.utils.model.LogUtils;

import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;

/**
 * 获取网络状态
 * Created by Han on 2018/1/24
 */

public class IMNetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                switch (networkInfo.getType()) {
                    case TYPE_MOBILE:
                    case TYPE_WIFI:
                        // 无线 连接成功 1
                        if (IMUtils.getInstance().isLogin()) {
                            if (IMConstants.recording.get("record") == null || IMConstants.recording.get("record")) {
                                if (IMConstants.recording.get("resume") != null && IMConstants.recording.get("resume")) {
                                    PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                                    if (pm != null && pm.isScreenOn()) {
                                        LogUtils.e("mh-->>mqService--->网络变动");
                                        new Thread() {
                                            @Override
                                            public void run() {
                                                super.run();
                                                try {
                                                    Thread.sleep(1000 * 5);
                                                    IMUtils.getInstance().startService();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }.start();
                                    }
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
            } else {
                // 手机没有任何的网络
                LogUtils.e("mh-->>mqService--->手机没有任何的网络");
                context.sendBroadcast(new Intent(IMConstants.ACTION_LOGIN_OPENFIRE_FAILURE_STATE));
            }
        }

    }

}

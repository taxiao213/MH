package com.haxi.mh.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.haxi.mh.utils.model.LogUtils;

/**
 * Created by Han on 2018/6/20
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class MessengerService extends Service {

    public static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    LogUtils.e("----server--" + msg.getData().getString("msg"));
                    Messenger replyTo = msg.replyTo;
                    Message obtain = Message.obtain(null, 0);
                    Bundle bundle = new Bundle();
                    bundle.putString("reply","服务端收到信息");
                    obtain.setData(bundle);
                    try {
                        replyTo.send(obtain);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;

            }
            super.handleMessage(msg);
        }
    }

    private final Messenger messenger = new Messenger(new MessengerHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }


}

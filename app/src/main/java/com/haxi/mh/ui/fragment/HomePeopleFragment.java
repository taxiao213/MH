package com.haxi.mh.ui.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.autofill.AutofillManager;

import com.haxi.mh.R;
import com.haxi.mh.aidl.MessengerService;
import com.haxi.mh.base.BaseFragment;
import com.haxi.mh.ui.activity.draw.DrawActivity;
import com.haxi.mh.ui.activity.pay.PayActivity;
import com.haxi.mh.utils.model.LogUtils;

import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 我的
 * Created by Han on 2017/12/27
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class HomePeopleFragment extends BaseFragment {

    Unbinder unbinder;
    private Messenger grtReply = new Messenger(new MessengerHandler());

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    LogUtils.e("----client--" + msg.getData().getString("reply"));
            }
        }
    }

    private Messenger messenger;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messenger = new Messenger(service);
            Message obtain = Message.obtain(null, 0);
            Bundle bundle = new Bundle();
            bundle.putString("msg", "hello this is client");
            obtain.setData(bundle);
            obtain.replyTo = grtReply;
            try {
                messenger.send(obtain);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_homepeople;
    }

    @Override
    protected void initView() {
        Intent intent = new Intent(mActivity, MessengerService.class);
        mActivity.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void initData() {

    }


    @OnClick({R.id.pay, R.id.draw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pay:
                startActivity(new Intent(mActivity, PayActivity.class));
                break;
            case R.id.draw:
                startActivity(new Intent(mActivity, DrawActivity.class));
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.unbindService(serviceConnection);
    }

    public @StringRes
    int getTitleResID() {
        return 0;
    }

}

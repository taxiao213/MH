package com.haxi.mh.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;

import com.haxi.mh.R;
import com.haxi.mh.aidl.Book;
import com.haxi.mh.aidl.BookManagerService;
import com.haxi.mh.aidl.IBookManagerInterface;
import com.haxi.mh.aidl.IOnNewBookArrivedListener;
import com.haxi.mh.base.BaseActivity;
import com.haxi.mh.utils.model.LogUtils;

import java.util.List;

/**
 * 客户端
 * Created by Han on 2018/6/20
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class AidlActivity extends BaseActivity {

    private IBookManagerInterface managerInterface;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    LogUtils.e("----client---- receive new book " + msg.obj);
                    break;
            }
        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.e("----client---- onServiceConnected ");
            managerInterface = IBookManagerInterface.Stub.asInterface(service);
            try {
                //客户端连接后设置死亡代理
                managerInterface.asBinder().linkToDeath(deathRecipient,0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            try {
                List<Book> bookList = managerInterface.getBookList();
                LogUtils.e("----client---- onServiceConnected " + bookList.toString());
                managerInterface.registerListener(iOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            managerInterface = null;
            LogUtils.e("----client---- onServiceDisconnected ");
            //当Binder意外死亡时，1.在onServiceDisconnected设置重连 此线程在UI 2.在binderDied 回调方法中
        }
    };

    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            //死亡代理的回调
            LogUtils.e("----- binderDied -----");
            if (managerInterface == null) return;
            managerInterface.asBinder().unlinkToDeath(deathRecipient, 0);
            managerInterface = null;
            //当Binder意外死亡时，1.在onServiceDisconnected设置重连 此线程在UI 2.在binderDied 回调方法中

            //再次重新绑定，连接 此线程在Binder线程池中，不能访问UI
            Intent intent = new Intent(mActivity, BookManagerService.class);
            bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        }
    };

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_aidl;
    }

    @Override
    protected void getData() {
        Intent intent = new Intent(mActivity, BookManagerService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        if (managerInterface != null && managerInterface.asBinder().isBinderAlive()) {
            try {
                managerInterface.unregisterListener(iOnNewBookArrivedListener);
                LogUtils.e("----onDestroy---- unregisterListener ");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private IOnNewBookArrivedListener iOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrives(Book book) throws RemoteException {
            //aidl接口
            mHandler.obtainMessage(0, book).sendToTarget();
            LogUtils.e("----onNewBookArrives  ");
        }
    };
}

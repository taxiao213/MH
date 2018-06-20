package com.haxi.mh.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.haxi.mh.R;
import com.haxi.mh.aidl.Book;
import com.haxi.mh.aidl.BookManagerService;
import com.haxi.mh.aidl.IBookManagerInterface;
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


    private ServiceConnection serviceConnection =new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBookManagerInterface managerInterface = IBookManagerInterface.Stub.asInterface(service);
            try {
                List<Book> bookList = managerInterface.getBookList();
                LogUtils.e("----client----"+bookList.toString());

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
        return R.layout.activity_aidl;
    }

    @Override
    protected void getData() {
        Intent intent = new Intent(mActivity, BookManagerService.class);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}

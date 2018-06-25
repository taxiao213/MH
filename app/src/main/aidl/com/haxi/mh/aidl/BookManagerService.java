package com.haxi.mh.aidl;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.haxi.mh.utils.model.LogUtils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Han on 2018/6/20
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class BookManagerService extends Service {

    private AtomicBoolean atomicBoolean = new AtomicBoolean(false);
    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();//支持 并发读 / 写
//    private CopyOnWriteArrayList<IOnNewBookArrivedListener> listeners = new CopyOnWriteArrayList<>(); //多线程中在注销时会找不到listener

    private RemoteCallbackList<IOnNewBookArrivedListener> mlisteners = new RemoteCallbackList<>();
    private Binder binder;


    @Override
    public void onCreate() {
        super.onCreate();
//            if (!listeners.contains(onNewBookArrivedListener)) {
//                listeners.add(onNewBookArrivedListener);
//            } else {
//                LogUtils.e("---BookManagerService  registerListener-----" + "已经注册");
//            }
//            if (listeners.contains(onNewBookArrivedListener)) {
//                listeners.remove(onNewBookArrivedListener);
//            } else {
//                LogUtils.e("---BookManagerService  unregisterListener-----" + "已经移除注册");
//            }
        binder = new IBookManagerInterface.Stub() {
            @Override
            public List<Book> getBookList() throws RemoteException {
                return mBookList;
            }

            @Override
            public void addBook(Book book) throws RemoteException {
                mBookList.add(book);
            }

            @Override
            public void registerListener(IOnNewBookArrivedListener onNewBookArrivedListener) throws RemoteException {
//            if (!listeners.contains(onNewBookArrivedListener)) {
//                listeners.add(onNewBookArrivedListener);
//            } else {
//                LogUtils.e("---BookManagerService  registerListener-----" + "已经注册");
//            }
                mlisteners.register(onNewBookArrivedListener);
                LogUtils.e("---BookManagerService  registerListener-----listeners ==");
            }

            @Override
            public void unregisterListener(IOnNewBookArrivedListener onNewBookArrivedListener) throws RemoteException {
//            if (listeners.contains(onNewBookArrivedListener)) {
//                listeners.remove(onNewBookArrivedListener);
//            } else {
//                LogUtils.e("---BookManagerService  unregisterListener-----" + "已经移除注册");
//            }
                mlisteners.unregister(onNewBookArrivedListener);
                LogUtils.e("---BookManagerService  unregisterListener-----listeners ==");
            }
        };

        mBookList.add(new Book(1, "第一"));
        mBookList.add(new Book(2, "第二"));
        new Thread(new ServiceWork()).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        atomicBoolean.set(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //aidl自定义权限 进行验证 PackageManager.PERMISSION_DENIED=-1
        int i = checkCallingOrSelfPermission("com.haxi.mh.aidl.ACCESS_BOOK_SERVICE");
        if (i == PackageManager.PERMISSION_DENIED) {
            return null;
        }
        return binder;
    }

    public class ServiceWork implements Runnable {
        @Override
        public void run() {
            while (!atomicBoolean.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = mBookList.size() + 1;
                Book book = new Book(bookId, "new Book" + bookId);
                try {
                    onNewBookArrived(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void onNewBookArrived(Book book) throws RemoteException {
        mBookList.add(book);
        LogUtils.e("---BookManagerService  onNewBookArrived-----notify  mBookList ==" + mBookList.size());
//        for (int i = 0; i < listeners.size(); i++) {
//            IOnNewBookArrivedListener bookArrivedListener = listeners.get(i);
//            bookArrivedListener.onNewBookArrives(book);
//            LogUtils.e("---BookManagerService  onNewBookArrived-----notify  IOnNewBookArrivedListener ==" + listeners);
//        }

        final int N = mlisteners.beginBroadcast();
        for (int i = 0; i < N; i++) {
            IOnNewBookArrivedListener broadcastItem = mlisteners.getBroadcastItem(i);
            if (broadcastItem != null) {
                broadcastItem.onNewBookArrives(book);
            }
        }
        mlisteners.finishBroadcast();
    }
}

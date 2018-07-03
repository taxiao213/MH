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

    //AtomicBoolean是java.util.concurrent.atomic包下的原子变量，这个包里面提供了一组原子类。其基本的特性就是在多线程环境下
    // 当有多个线程同时执行这些类的实例包含的方法时，具有排他性
    // 即当某个线程进入方法，执行其中的指令时，不会被其他线程打断，而别的线程就像自旋锁一样，一直等到该方法执行完成
    private AtomicBoolean atomicBoolean = new AtomicBoolean(false);
    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();//支持 并发读 / 写

    //用这个类，可以简单的创建一个实例，通过其register(E)和unregister(E)函数注册或注销clients
    //通过beginBroadcast(), getBroadcastItem(int), finishBroadcast().回调给client
    //如果一个注册了callback的进程消失了，这个类会自动将其从list中清除
    //可以通过onCallbackDied(E)对这个注册的callback进行额外处理
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

package com.haxi.mh.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Han on 2018/6/27
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class BinderPool {
    public static final int BINDER_NONE = -1;
    public static final int BINDER_COMPUTE = 0;
    public static final int BINDER_SECURITY = 1;
    private final Context mContext;
    private static volatile BinderPool binderPool;
    private CountDownLatch countDownLatch;
    //CounyDownLatch是一个同步类，他允许一个或多个线程一直等待，直到其他线层的操作执行完成后再去执行
    private IBinderPool mBinderPool;

    private BinderPool(Context context) {
        mContext = context.getApplicationContext();
        connectionBinderPoolService();
    }

    public static BinderPool getInstance(Context context) {
        if (binderPool == null) {
            synchronized (BinderPool.class) {
                if (binderPool == null) {
                    binderPool = new BinderPool(context);
                }
            }
        }
        return binderPool;
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinderPool = IBinderPool.Stub.asInterface(service);
            try {
                mBinderPool.asBinder().linkToDeath(deathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            mBinderPool.asBinder().unlinkToDeath(deathRecipient, 0);
            mBinderPool = null;
            connectionBinderPoolService();
        }
    };

    private synchronized void connectionBinderPoolService() {
        countDownLatch = new CountDownLatch(1);
        Intent intent = new Intent(mContext, BinderPoolService.class);
        mContext.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public IBinder queryBinder(int binderCode) {
        IBinder iBinder = null;
        try {
            iBinder = mBinderPool.queryBinder(binderCode);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return iBinder;
    }

    public static class BinderPoolIml extends IBinderPool.Stub {
        public BinderPoolIml() {
            super();
        }

        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder binder = null;
            switch (binderCode) {
                case BINDER_NONE:
                    break;
                case BINDER_COMPUTE:
                    binder = new ComputeImpl();
                    break;
                case BINDER_SECURITY:
                    binder = new SecurityCenterImpl();
                    break;
            }
            return binder;
        }
    }
}

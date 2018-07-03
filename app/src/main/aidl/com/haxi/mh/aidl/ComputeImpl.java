package com.haxi.mh.aidl;

import android.os.RemoteException;

/**
 * Binder 线程池
 * Created by Han on 2018/6/27
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class ComputeImpl extends ICompute.Stub{
    @Override
    public int add(int a, int b) throws RemoteException {
        return a+b;
    }
}

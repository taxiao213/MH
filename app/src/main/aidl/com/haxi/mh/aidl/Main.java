package com.haxi.mh.aidl;

import android.os.IBinder;
import android.os.RemoteException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created by Han on 2018/6/20
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class Main {

    private static IBookManagerInterface.Stub managerInterface;
    private static IBinder.DeathRecipient deathRecipient;

    public static void main(String args) {

        managerInterface = new IBookManagerInterface.Stub() {
            @Override
            public List<Book> getBookList() throws RemoteException {
                return null;
            }

            @Override
            public void addBook(Book book) throws RemoteException {

            }

            @Override
            public void registerListener(IOnNewBookArrivedListener onNewBookArrivedListener) throws RemoteException {

            }

            @Override
            public void unregisterListener(IOnNewBookArrivedListener onNewBookArrivedListener) throws RemoteException {

            }
        };

        deathRecipient = new IBinder.DeathRecipient() {
            @Override
            public void binderDied() {
                if (managerInterface ==null)
                    return;
                    managerInterface.asBinder().unlinkToDeath(deathRecipient,0);
                    managerInterface=null;
                    //todo 重新绑定远程service

            }
        };
        File file = new File("");
        try {
            Book 黄继光 = new Book(11, "黄继光");

            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(黄继光);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

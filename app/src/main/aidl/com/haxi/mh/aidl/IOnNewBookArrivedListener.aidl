// IMyAidlInterface.aidl
package com.haxi.mh.aidl;

// Declare any non-default types here with import statements
import com.haxi.mh.aidl.Book;
interface IOnNewBookArrivedListener {
    void onNewBookArrives(in Book book);
}

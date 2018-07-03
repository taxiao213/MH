// BookInterface.aidl
package com.haxi.mh.aidl;

// Declare any non-default types here with import statements
import com.haxi.mh.aidl.Book;
import com.haxi.mh.aidl.IOnNewBookArrivedListener;

// Binder 线程池
interface IBinderPool {
  IBinder queryBinder(int binderCode);
}

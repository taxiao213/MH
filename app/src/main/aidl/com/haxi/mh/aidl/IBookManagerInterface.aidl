// BookInterface.aidl
package com.haxi.mh.aidl;

// Declare any non-default types here with import statements
import com.haxi.mh.aidl.Book;
import com.haxi.mh.aidl.IOnNewBookArrivedListener;

interface IBookManagerInterface {
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IOnNewBookArrivedListener onNewBookArrivedListener);
    void unregisterListener(IOnNewBookArrivedListener onNewBookArrivedListener);
}

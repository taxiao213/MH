// BookInterface.aidl
package com.haxi.mh.aidl;

// Declare any non-default types here with import statements
import com.haxi.mh.aidl.Book;
import com.haxi.mh.aidl.IOnNewBookArrivedListener;

interface ISecurityCenter {
   String encrypt(String content);
   String decrypt(String password);
}

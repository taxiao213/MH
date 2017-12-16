package com.haxi.mh.utils.model;


import com.orhanobut.logger.Logger;

/**
 * 日志输出Logger
 * Created by Han on 2017/12/16
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 */

public class LogUtils {

    public static void v(String arg) {
        Logger.v(arg);
    }

    public static void v(String arg, String st) {
        Logger.v(arg, st);
    }

    public static void d(String arg) {
        Logger.d(arg);
    }

    public static void d(String arg, String st) {
        Logger.d(arg, st);
    }

    public static void i(String arg) {
        Logger.i(arg);
    }

    public static void i(String arg, String st) {
        Logger.i(arg, st);
    }

    public static void w(String arg) {
        Logger.w(arg);
    }

    public static void w(String arg, String st) {
        Logger.w(arg, st);
    }

    public static void e(String arg) {
        Logger.e(arg);
    }

    public static void e(String arg, String st) {
        Logger.e(arg, st);
    }

}

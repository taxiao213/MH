package com.haxi.mh.utils.model;


import com.haxi.mh.constant.Constant;
import com.orhanobut.logger.Logger;

/**
 * 日志输出Logger
 * Created by Han on 2017/12/16
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class LogUtils {

    public static void v(String arg) {
        if (Constant.IS_OPEN_LOG) {
            Logger.v(arg);
        }
    }

    public static void v(String arg, String st) {
        if (Constant.IS_OPEN_LOG) {
            Logger.t(arg).v(st);
        }
    }

    public static void d(String arg) {
        if (Constant.IS_OPEN_LOG) {
            Logger.d(arg);
        }
    }

    public static void d(String arg, String st) {
        if (Constant.IS_OPEN_LOG) {
            Logger.t(arg).d(st);
        }
    }

    public static void i(String arg) {
        if (Constant.IS_OPEN_LOG) {
            Logger.i(arg);
        }
    }

    public static void i(String arg, String st) {
        if (Constant.IS_OPEN_LOG) {
            Logger.t(arg).i(st);
        }
    }

    public static void w(String arg) {
        if (Constant.IS_OPEN_LOG) {
            Logger.w(arg);
        }
    }

    public static void w(String arg, String st) {
        if (Constant.IS_OPEN_LOG) {
            Logger.t(arg).w(st);
        }
    }

    public static void e(String arg) {
        if (Constant.IS_OPEN_LOG) {
            Logger.e(arg);
        }
    }

    public static void e(String arg, String st) {
        if (Constant.IS_OPEN_LOG) {
            Logger.t(arg).e(st);
        }
    }

}

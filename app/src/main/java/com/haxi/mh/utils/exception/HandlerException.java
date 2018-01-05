package com.haxi.mh.utils.exception;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.haxi.mh.utils.model.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * 处理全局未捕获异常
 * Created by zhangjutao on 2016/8/25.
 */
public class HandlerException implements Thread.UncaughtExceptionHandler {

    public static final String TAG = "HandlerException--->>  ";

    // HandlerException 实例
    private static HandlerException instance = null;

    // Context 对象
    private Context mContext;

    // 系统默认的 UncaughtException 处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    // 用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<>();

    // 用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA);


    /**
     * 保证只有一个 CrashHandler 实例
     */
    private HandlerException() {
    }

    /**
     * 获取 CrashHandler 实例 ,单例模式
     */
    public static HandlerException getInstance() {
        if (instance == null) {
            synchronized (HandlerException.class) {
                if (instance == null) {
                    instance = new HandlerException();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化
     *
     * @param context context
     */
    public void init(Context context) {
        this.mContext = context;
        // 获取系统默认的 UncaughtException 处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该 CrashHandler 为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当 UncaughtException 发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
            return;
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            LogUtils.e(TAG + "error : " + e);
        }
        // 退出程序,注释下面的重启启动程序代码
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    /**
     * 自定义错误处理，收集错误信息，发送错误报告等操作均在此完成
     *
     * @param ex ex
     * @return true：如果处理了该异常信息；否则返回 false
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出...", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
        // 收集设备参数信息
        collectDeviceInfo(mContext);
        // 保存日志文件
        saveCrashInfo2File(ex);
        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param context context
     */
    public void collectDeviceInfo(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e(TAG + "an error occured when collect package info" + e);
        }

        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                LogUtils.e(TAG + field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                LogUtils.e(TAG + "an error occured when collect crash info" + e);
            }
        }
    }

    /**
     * 保存错误信息到文件中
     * *
     *
     * @param ex ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private void saveCrashInfo2File(Throwable ex) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append("=").append(value).append("\n");
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                String path = rootPath + "/crash/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }

        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }
    }
}

package com.haxi.mh.utils.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.haxi.mh.MyApplication;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * UI工具类,对一些界面数据的获取与操作
 * Created by Han on 2017/12/11
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class UIUtil {

    //屏幕宽度
    public static int mScreenWidth;
    //屏幕高度
    public static int mScreenHeight;


    public static Context getContext() {
        return MyApplication.getMyApplication();
    }


    //判断当前的线程是不是在主线程
    public static boolean isRunInMainThread() {
        //android.os.Process.myTid()表示调用线程的id
        return android.os.Process.myTid() == getMainThreadId();
    }

    /**
     * 在主线程执行runnable
     */
    public static boolean post(Runnable runnable) {
        return getHandler().post(runnable);
    }

    /**
     * dp转换px
     */
    public static int dip2px(int dip) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }


    /**
     * px转换dp
     */
    public static int px2dip(int px) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 获取手机屏幕宽和高
     *
     * @param context
     * @return
     */
    public static int[] getScreenDispaly(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();//手机屏幕的宽度
        int height = wm.getDefaultDisplay().getHeight();//手机屏幕的高度
        int result[] = {width, height};
        return result;
    }

    /**
     * 获取资源
     */
    public static Resources getResources() {
        return getContext().getResources();
    }


    /**
     * 获取文字
     */
    public static String getString(@StringRes int resId) {
        return getResources().getString(resId);
    }

    public static String getString(@StringRes int resId, Object... formatArgus) {
        return getResources().getString(resId, formatArgus);
    }


    /**
     * 获取dimen
     */
    public static int getDimens(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    /**
     * 获取drawable
     */
    public static Drawable getDrawable(int resId) {
        return ActivityCompat.getDrawable(getContext(), resId);
    }

    /**
     * 获取颜色
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 获取屏幕高度
     *
     * @return 屏幕高度
     */
    public static int getScreenHeight() {
        if (mScreenHeight == 0) {
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            if (wm != null) {
                mScreenHeight = wm.getDefaultDisplay().getHeight();
            }
        }
        return mScreenHeight;
    }

    /**
     * 获取屏幕宽度
     *
     * @return 屏幕宽度
     */
    public static int getScreenWidth() {
        if (mScreenWidth == 0) {
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            if (wm != null) {
                mScreenWidth = wm.getDefaultDisplay().getWidth();
            }
        }
        return mScreenWidth;
    }


    /**
     * 设置指定textView的字体大小，单位sp
     *
     * @param textView textView
     * @param textSize textSize
     */
    public static void setTextSize(TextView textView, int textSize) {
        if (textView != null)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    }


    /**
     * 把自身从父View中移除
     */
    public static void removeSelfFromParent(View view) {
        if (view != null) {
            ViewParent parent = view.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(view);
            }
        }
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public int getStateBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        } else {
            result = (int) Math.ceil(20 * context.getResources().getDisplayMetrics().density);
        }
        return result;
    }

    /**
     * 延时在主线程执行runnable
     */
    public static boolean postDelayed(Runnable runnable, long delayMillis) {
        return getHandler().postDelayed(runnable, delayMillis);
    }


    /**
     * 获取主线程的handler
     */
    public static Handler getHandler() {
        return MyApplication.getMainThreadHandler();
    }

    /**
     * 获取主线程id
     *
     * @return
     */
    public static long getMainThreadId() {
        return MyApplication.getMainThreadId();
    }


    /**
     * 显示确定Dialog
     */
    public static void showAlertDialog(final Context context, final String content) {
        runInMainThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(context)
                        .setTitle("提示")
                        .setMessage(content)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    /**
     * 运行在主线程
     *
     * @param runnable
     */
    public static void runInMainThread(Runnable runnable) {
        if (isRunInMainThread()) {
            runnable.run();
        } else {
            post(runnable);
        }
    }


    /**
     * 显示ProgressDialog
     */
    public static ProgressDialog showProgressDialog(Context context, String tips) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(tips);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
        // 10秒后如果没有被关闭，则自动关闭
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }, 10000);
        return dialog;
    }

    /**
     * 判断SDCard是否存在,并可写
     *
     * @return
     */
    public static boolean checkSDCard() {
        String flag = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(flag);
    }

    /**
     * 是否横屏
     *
     * @param context
     * @return true为横屏，false为竖屏
     */
    public static boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 判断是否是平板
     * 这个方法是从 Google I/O App for Android 的源码里找来的，非常准确。
     *
     * @param context
     * @return
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;
    }

    /**
     * 沉浸式工具类，判断是底部有虚拟键进行透明设置
     *
     * @param context
     */
    public static void transPortStatus(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (!UIUtil.checkDeviceHasNavigationBar(context)) {
                //透明导航栏
                context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
    }

    /**
     * 沉浸式工具类，判断是底部有虚拟键进行透明设置
     */
    public static void transPortStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Activity context1 = (Activity) getContext();
            //透明状态栏
            context1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            context1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 沉浸式工具类
     *
     * @param context
     * @param view
     */

    public static void transPortStatusWithView(Activity context, View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (view != null) {
                view.setVisibility(View.VISIBLE);
            }
            if (!UIUtil.checkDeviceHasNavigationBar(context)) {
                context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
    }


    /**
     * 开启键盘
     *
     * @param view
     */
    private void openBroad(View view) {
        InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, 0);

    }

    /**
     * 关闭键盘
     *
     * @param view
     */
    private void closeBroad(View view) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 根据图片的名称获取对应的资源id
     *
     * @param resourceName
     * @return
     */
    public static int getDrawResourceID(String resourceName) {
        Resources res = getResources();
        int picid = res.getIdentifier(resourceName, "drawable", getContext().getPackageName());
        return picid;
    }


    /**
     * 获得当前的版本信息
     *
     * @return
     */
    public static String[] getVersionInfo() {
        String[] version = new String[2];
        Context context = getContext();
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version[0] = String.valueOf(packageInfo.versionCode);
            version[1] = packageInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 是否开启 GPS
     *
     * @return
     */
    public static boolean isOpenGPS() {
        Context context = getContext();
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNPEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return isGPSEnable || isNPEnable;
    }

    /**
     * 开启GPS
     *
     * @param context
     */
    public static void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(getContext(), 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    /**
     * 短信分享
     *
     * @param mContext
     * @param smstext  短信分享内容
     * @return
     */
    public static Boolean sendSms(Context mContext, String smstext) {
        Uri smsToUri = Uri.parse("smsto:");
        Intent mIntent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        mIntent.putExtra("sms_body", smstext);
        mContext.startActivity(mIntent);
        return null;
    }

    /**
     * 邮件分享
     *
     * @param mContext
     * @param title    邮件的标题
     * @param text     邮件的内容
     * @return
     */
    public static void sendMail(Context mContext, String title, String text) {
        // 调用系统发邮件
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        // 设置文本格式
        emailIntent.setType("text/plain");
        // 设置对方邮件地址
        emailIntent.putExtra(Intent.EXTRA_EMAIL, "");
        // 设置标题内容
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        // 设置邮件文本内容
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);
        mContext.startActivity(Intent.createChooser(emailIntent, "Choose Email Client"));
    }

    /**
     * 根据key获取config.properties里面的值
     *
     * @param context
     * @param key
     * @return
     */
    public static String getProperty(Context context, String key) {
        try {
            Properties props = new Properties();
            InputStream input = context.getAssets().open("config.properties");
            if (input != null) {
                props.load(input);
                return props.getProperty(key);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }

    /**
     * 唤起其他APP  "com.tencent.mm"
     *
     * @param packagename 包名
     * @param context     上下文
     */
    private void doStartApplicationWithPackageName(String packagename, Context context) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = context.getPackageManager().queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            context.startActivity(intent);
        }
    }

    /**
     * 判断服务是否开启
     *
     * @return
     */
    public static boolean isServiceRunning(Context context, String ServiceName) {
        if (TextUtils.isEmpty(ServiceName)) {
            return false;
        }

        android.app.ActivityManager myManager = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<android.app.ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(100);
        if (runningService == null || runningService.size() == 0) {
            return false;
        }
        for (android.app.ActivityManager.RunningServiceInfo list : runningService) {
            if (list.service.getClassName().toString().equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }
}



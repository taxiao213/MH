package com.haxi.mh.utils.background;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

/**
 * 监听窗口焦点,并且获取焦点窗口的包名
 * Created by Han on 2018/3/8
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class DetectService extends AccessibilityService {

    private static String mForegroundPackageName;
    private static DetectService mInstance = null;

    public DetectService() {
    }

    public static DetectService getInstance() {
        if (mInstance == null) {
            synchronized (DetectService.class) {
                if (mInstance == null) {
                    mInstance = new DetectService();
                }
            }
        }
        return mInstance;
    }

    /**
     * 监听窗口焦点,并且获取焦点窗口的包名
     *
     * @param event
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            mForegroundPackageName = event.getPackageName().toString();
        }
    }

    @Override
    public void onInterrupt() {
    }

    public String getForegroundPackage() {
        return mForegroundPackageName;
    }


    /**
     * 此方法用来判断当前应用的辅助功能服务是否开启
     *
     * @param context
     * @return
     */
    public static boolean isAccessibilitySettingsOn(Context context) {
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            Log.e("wenming", e.getMessage());
        }

        if (accessibilityEnabled == 1) {
            String services = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (services != null) {
                return services.toLowerCase().contains(context.getPackageName().toLowerCase());
            }
        }
        return false;
    }
}
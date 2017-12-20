package com.haxi.mh.utils.ui;

import android.app.Activity;

import java.util.ListIterator;
import java.util.Stack;

/**
 * 所有栈中的Activity 管理
 * Created by Han on 2017/12/14
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class ActivityManager {

    public static ActivityManager manager;
    private static Stack<Activity> activityStack = null;

    public static ActivityManager getInstances() {
        if (manager == null) {
            synchronized (ActivityManager.class) {
                if (manager == null) {
                    manager = new ActivityManager();
                }
            }
        }
        return manager;
    }

    public ActivityManager() {

    }

    /**
     * 将Activity加入集合
     *
     * @param activity
     */
    public void add(Activity activity) {
        if (activityStack == null) {
            synchronized (ActivityManager.class) {
                if (activityStack == null) {
                    activityStack = new Stack<>();
                }
            }
        }
        activityStack.add(activity);
    }

    /**
     * 将Activity从集合移除
     *
     * @param activity
     */
    public void remove(Activity activity) {
        if (!activityStack.empty()) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 将所有Activity从集合移除
     */
    public void removeAllActivity() {
        if (!activityStack.empty()) {
            ListIterator<Activity> iterator = activityStack.listIterator();
            while (iterator.hasNext()) {
                Activity activity = iterator.next();
                if (activity != null && !activity.isFinishing()) {
                    iterator.remove();
                    activity.finish();
                }
            }
        }
    }

    /**
     * 除此Activity外,将其他Activity从集合移除
     */
    public void removeExceptActivity(Class clas) {
        if (!activityStack.empty()) {
            ListIterator<Activity> iterator = activityStack.listIterator();
            while (iterator.hasNext()) {
                Activity activity = iterator.next();
                if (activity != null && !activity.isFinishing()) {
                    if (!activity.getClass().equals(clas)) {
                        iterator.remove();
                        activity.finish();
                    }
                }
            }
        }
    }

    /**
     * 获得当前的Activity(即最上层)
     *
     * @return
     */
    public Activity getCurrentActivity() {
        Activity activity = null;
        if (!activityStack.empty()) {
            activity = activityStack.lastElement();
        }
        return activity;
    }

    /**
     * 获取当前栈Activity数量
     *
     * @return
     */
    public int getActivityCount() {
        int count = 0;
        if (!activityStack.empty()) {
            count = activityStack.size();
        }
        return count;
    }
}

package com.haxi.mh.utils;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

/**
 * 弹出吐司
 * Created by Han on 2017/12/16
 * Email:yin13753884368@163.com
 */

public class ToastUtils {

    /**
     * 弹出吐司 短时间
     *
     * @param st
     */
    public static void showShortToast(String st) {
        showToast(st, Toast.LENGTH_SHORT);
    }

    /**
     * 弹出吐司 长时间
     *
     * @param st
     */
    public static void showLongToast(String st) {
        showToast(st, Toast.LENGTH_LONG);
    }

    /**
     * 弹出吐司
     *
     * @param st
     * @param length
     */
    private static void showToast(final String st, final int length) {
        if (UIUtil.isRunInMainThread()) {
            Toast.makeText(UIUtil.getContext(), st, length).show();
        } else {
            UIUtil.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(UIUtil.getContext(), st, length).show();
                }
            });
        }
    }

    /**
     * SnackBar 短时间
     *
     * @param view
     * @param st
     * @param length
     */
    private static void showShortSnackBar(View view, String st, int length) {
        showSnackBar(view, st, Snackbar.LENGTH_SHORT);
    }

    /**
     * SnackBar 长时间
     *
     * @param view
     * @param st
     * @param length
     */
    private static void showLongSnackBar(View view, String st, int length) {
        showSnackBar(view, st, Snackbar.LENGTH_LONG);
    }


    /**
     * SnackBar 并弹出吐司 短时间
     *
     * @param view
     * @param st
     */
    private static void showSnackBarShortToast(View view, String st) {
        showSnackBarToast(view, st, Snackbar.LENGTH_SHORT);
    }


    /**
     * SnackBar 并弹出吐司 长时间
     *
     * @param view
     * @param st
     */
    private static void showSnackBarLongToast(View view, String st) {
        showSnackBarToast(view, st, Snackbar.LENGTH_LONG);
    }

    /**
     * SnackBar 消失 并弹出吐司
     *
     * @param view
     * @param st
     * @param length
     */
    private static void showSnackBarToast(View view, final String st, final int length) {
        Snackbar.make(view, st, length).setAction("右侧信息", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast(st, length);
            }
        }).show();

    }

    /**
     * SnackBar
     *
     * @param view
     * @param st
     * @param length
     */
    private static void showSnackBar(View view, String st, int length) {
        Snackbar.make(view, st, length).show();
    }

}

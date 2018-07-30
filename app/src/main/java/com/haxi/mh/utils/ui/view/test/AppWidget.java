package com.haxi.mh.utils.ui.view.test;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.SystemClock;
import android.widget.RemoteViews;

import com.haxi.mh.R;
import com.haxi.mh.constant.Constant;
import com.haxi.mh.utils.model.LogUtils;
import com.haxi.mh.utils.ui.toast.ToastUtils;

/**
 * 桌面小插件
 * Created by Han on 2018/7/25
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class AppWidget extends AppWidgetProvider {

    public AppWidget() {
        super();
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        LogUtils.e("--app--onEnabled");
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        LogUtils.e("--app--onDisabled");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(Constant.RECEIVER_WIDGET)) {
            ToastUtils.showShortToast("我是小插件");
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.add);
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                    for (int i = 0; i < 37; i++) {
                        float degree = (i * 100) / 360;
                        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
                        remoteViews.setImageViewBitmap(R.id.iv, rotateBitmap(context, bitmap, degree));
                        Intent intent1 = new Intent(Constant.RECEIVER_WIDGET);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent1, 0);
                        remoteViews.setOnClickPendingIntent(R.id.iv, pendingIntent);
                        appWidgetManager.updateAppWidget(new ComponentName(context, AppWidget.class), remoteViews);
                        SystemClock.sleep(30);
                    }

                }
            }.start();
        }
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        int length = appWidgetIds.length;
        for (int i = 0; i < length; i++) {
            int appWidgetId = appWidgetIds[i];
            onWidgetUpdate(context, appWidgetManager, appWidgetId);
        }
    }

    private void onWidgetUpdate(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Intent intent1 = new Intent(Constant.RECEIVER_WIDGET);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent1, 0);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        remoteViews.setOnClickPendingIntent(R.id.iv, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    /**
     * 旋转bitmap
     *
     * @param context
     * @param bitmap
     * @param degree
     * @return
     */
    public Bitmap rotateBitmap(Context context, Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degree);
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bitmap1;
    }
}

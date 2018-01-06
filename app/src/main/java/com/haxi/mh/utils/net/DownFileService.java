package com.haxi.mh.utils.net;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;

/**
 * 安卓系统下载类
 * Created by Han on 2018/1/5
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 * 用法：
 * Intent updataService = new Intent(this, DownFileService.class);
 * updataService.putExtra("downloadurl", downUrl);
 * updataService.putExtra("fileName", fileName);
 * startService(updataService);
 */
public class DownFileService extends Service {
    /* 安卓系统下载类 **/
    private DownloadManager manager;
    /* 接收下载完的广播 **/
    private DownloadCompleteReceiver receiver;
    private String url;
    private String fileName;
    private String DOWNLOADPATH = "/DownLoadFile/";//下载路径，如果不定义自己的路径，6.0的手机不自动安装


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        url = intent.getStringExtra("downloadurl");
        fileName = intent.getStringExtra("fileName");
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + DOWNLOADPATH + fileName;
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        try {
            initDownManager();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (receiver != null) {
            // 注销下载广播
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }

    /**
     * 初始化下载器
     **/
    private void initDownManager() {
        manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        receiver = new DownloadCompleteReceiver();
        //设置下载地址
        DownloadManager.Request down = new DownloadManager.Request(Uri.parse(url));
        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        down.setAllowedOverRoaming(false);
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
        down.setMimeType(mimeString);
        // 下载时，通知栏显示途中
        down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        // 显示下载界面
        down.setVisibleInDownloadsUi(true);
        // 设置下载后文件存放的位置
        down.setDestinationInExternalPublicDir(DOWNLOADPATH, fileName);
        down.setTitle(fileName);
        // 将下载请求放入队列
        manager.enqueue(down);
        //注册下载广播
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }


    // 接受下载完成后的intent
    public class DownloadCompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //判断是否下载完成的广播
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                //获取下载的文件id
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (manager.getUriForDownloadedFile(downId) != null) {
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + DOWNLOADPATH + fileName);
                    if (file.exists()) {
                        openFile(file, context);
                    }
                } else {
                    Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
                }
                //停止服务并关闭广播
                DownFileService.this.stopSelf();
            }
        }
    }


    public void openFile(File file, Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.VIEW");
        String type = getMIMEType(file);
        intent.setDataAndType(Uri.fromFile(file), type);
        try {
            context.startActivity(intent);
        } catch (Exception var5) {
            var5.printStackTrace();
            Toast.makeText(context, "没有找到打开此类文件的程序", Toast.LENGTH_SHORT).show();
        }

    }

    public String getMIMEType(File var0) {
        String var1 = "";
        String var2 = var0.getName();
        String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return var1;
    }

}
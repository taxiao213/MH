package com.haxi.mh.utils.im;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;

/**
 * 打印本地log
 * Created by Han on 2018/2/27
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class IMLogHelper {

    public static IMLogHelper logHelper;
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    public static IMLogHelper getInstances() {
        if (logHelper == null) {
            synchronized (IMLogHelper.class) {
                if (logHelper == null) {
                    logHelper = new IMLogHelper();
                }
            }
        }
        return logHelper;
    }

    public void write(final String st) {
        if (IMConstants.LOG_SWITCH) {
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    StringBuffer sb = new StringBuffer();
                    String time = formatter.format(new Date());
                    sb.append(time).append("\n");
                    sb.append(st).append("\n");
                    String fileName = "log.txt";
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        File file = Environment.getExternalStorageDirectory().getAbsoluteFile();
                        String files = "log";
                        File dir = new File(file, files);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        File logFile = new File(dir, fileName);
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(logFile, true);
                            fos.write(sb.toString().getBytes());
                            fos.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            });
        }
    }
}

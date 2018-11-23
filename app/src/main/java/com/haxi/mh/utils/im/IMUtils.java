package com.haxi.mh.utils.im;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.view.View;


import com.haxi.mh.MyApplication;
import com.haxi.mh.R;
import com.haxi.mh.ServiceUtils;
import com.haxi.mh.model.db.DBMsgTipDoInfo;
import com.haxi.mh.utils.StringUtils;
import com.haxi.mh.utils.dbutil.DBMsgTipUtil;
import com.haxi.mh.utils.im.utils.BackgroundUtils;
import com.haxi.mh.utils.net.NetUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * 聊天相关工具类
 * Created by Han on 2018/10/15
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class IMUtils {

    private static IMUtils mIMUtils;
    private long initTime = 0L;
    public static final int IM_MESSAGE_TYPE1 = 1;//1不清除消息列表
    public static final int IM_MESSAGE_TYPE2 = 2;//2清除消息列表

    private IMUtils() {

    }

    /**
     * 初始化管理单例
     *
     * @return
     */
    public static IMUtils getInstance() {
        if (mIMUtils == null) {
            synchronized (IMUtils.class) {
                if (mIMUtils == null)
                    mIMUtils = new IMUtils();
            }
        }
        return mIMUtils;
    }

    //<editor-fold desc="Service相关">

    /**
     * 开启服务 登陆后开启服务
     */
    public void startServiceLogin() {
        MyApplication context = getMyApplication();
        if (context == null) return;
        if (isServiceRunning(context)) {
            stopService(context);
        }
        startService(context);
    }


    /**
     * 开启服务 在前台时开启服务
     */
    public void startServiceMain() {
        MyApplication context = getMyApplication();
        if (context == null) return;
        if (isForegroundProcess())
            startService(context);
    }

    /**
     * 开启服务 在切换网络时开启服务
     */
    public void startService() {
        MyApplication context = getMyApplication();
        if (context == null) return;
        startService(context);
    }

    /**
     * 开启服务
     *
     * @param context
     */
    private void startService(MyApplication context) {
        context.startService(new Intent(context, IMService.class));
    }

    /**
     * 关闭服务
     *
     * @param context
     */
    public void stopService(MyApplication context) {
        context.stopService(new Intent(context, IMService.class));
    }


    @Nullable
    private MyApplication getMyApplication() {
        if (!isLogin()) return null;
        MyApplication context = (MyApplication) MyApplication.getMyApplication();
        if (context == null) return null;
        return context;
    }

    /**
     * 网络是否连接
     *
     * @return
     */
    public boolean isNetConnected() {
        return NetUtils.isNetworkConnected(MyApplication.getMyApplication());
    }

    /**
     * 是否已经登陆
     * ture 已经登陆
     * false 未登陆
     *
     * @return
     */
    public boolean isLogin() {
        return false;
    }

    /**
     * 是否在前台 需要判断是否已经登陆
     * ture 在前台
     * false 不在前台
     *
     * @return
     */
    public boolean isForegroundProcess() {
        MyApplication context = getMyApplication();
        if (context == null) return false;
        return BackgroundUtils.getApplicationValue(context);
    }

    /**
     * 服务是否在运行
     * ture 在
     * false 不在
     *
     * @return
     */
    public boolean isServiceRunning(MyApplication context) {
        return ServiceUtils.isServiceRunning(context, IMService.class.getName());
    }

    /**
     * 获取当前时间 定时用
     *
     * @return Long
     */
    public Long getPresentTime() {
        return 0L;
    }

    /**
     * 保存当前时间 定时用
     */
    public void savePresentTime(Long time) {

    }

    //</editor-fold>

    /**
     * 获取当前账号
     *
     * @return Account
     */
    public String getAccount() {
        return "";
    }

    /**
     * 获取当前账号昵称
     *
     * @return Account
     */
    public String getAccountName() {
        return "";
    }

    /**
     * 返回的时间为String类型, 用于首页消息列表
     *
     * @param time long类型
     * @return String类型
     */
    public String getTime(long time) {
        return IMTimeUtils.getMainTime(time);
    }

    /**
     * 消息通知栏时间提醒
     * 返回String 类型时间
     *
     * @param time 传入String
     * @return String
     */
    public String getTime(String time) {
        return IMTimeUtils.getMainTime(time);
    }


    /**
     * 返回Long 类型时间
     *
     * @param times 传入String
     * @return String
     */
    public Long getTimeResultLong(String times) {
        return IMTimeUtils.getTimeResultLong(times);
    }


    /**
     * 是否有声音或者振动提醒
     */
    public void playVideo() {
        if (setIntervals()) return;
        Boolean settingSound = false;
        Boolean settingVibration = false;
        if (settingSound) {
            isPlayVideo(true);
        }
        if (settingVibration) {
            //设置振动
            Vibrator vibe = (Vibrator) MyApplication.getMyApplication().getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(500);
        }
    }


    /**
     * 声音提醒
     *
     * @param isSound true有声音  false静音
     */
    private void isPlayVideo(Boolean isSound) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer player) {
                player.seekTo(0);
            }
        });
        AssetFileDescriptor file = MyApplication.getMyApplication().getResources().openRawResourceFd(R.raw.ring);
        try {
            mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
            file.close();
            if (isSound) {
                mediaPlayer.setVolume(0.5f, 0.5f);
                mediaPlayer.prepare();
            } else {
                mediaPlayer.setVolume(0, 0);
                mediaPlayer.prepare();
            }

        } catch (Exception ioe) {
            mediaPlayer = null;
        }
        mediaPlayer.start();
    }

    /**
     * 设置间隔时间 {@link IMConstants#RECEIVE_MESSAGE_TIME}
     *
     * @return
     */
    private boolean setIntervals() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - initTime <= IMConstants.RECEIVE_MESSAGE_TIME) {
            initTime = currentTimeMillis;
            return true;
        } else {
            initTime = currentTimeMillis;
        }
        return false;
    }

    /**
     * 控制是否显示时间提示框
     *
     * @param position 位置
     * @param mlist    ArrayList<DBMsgTipDoInfo>
     * @param tvImTime V
     */
    public void timeIsvisible(int position, ArrayList<DBMsgTipDoInfo> mlist, View tvImTime) {
        try {
            if (position == 0) {
                tvImTime.setVisibility(View.VISIBLE);
            } else if (mlist.get(position) != null && mlist.get(position - 1) != null) {
                if (Math.abs(mlist.get(position).getMsgtip_create_time() - mlist.get(position - 1).getMsgtip_create_time()) < 3 * 60 * 1000) {
                    tvImTime.setVisibility(View.GONE);
                } else {
                    tvImTime.setVisibility(View.VISIBLE);
                }
            } else {
                tvImTime.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            tvImTime.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取消息
     *
     * @param type 1不清除列表 2清除列表
     */
    public void getMessage(int type) {
        MyApplication application = MyApplication.getMyApplication();
        if (application == null) return;
        Intent intent = new Intent(IMConstants.ACTION_TEXT_MESSAGE_REFRESH);
        if (type == IM_MESSAGE_TYPE2) {
            intent.putExtra("type", IM_MESSAGE_TYPE2);
        }
        application.sendBroadcast(intent);
    }

    /**
     * 数据库中是否有数据
     * true 有 显示缓存大小
     * false 无 不显示缓存大小
     * 可拓展
     *
     * @return 单位字节 需要转换
     */
    public Boolean hasDBData() {
        Long count = DBMsgTipUtil.getInstance().DBCount();
        return count > 0L;
    }

    /**
     * 获取消息列表文件大小
     *
     * @return 单位字节 需要转换
     */
    public Long getDBSize() {
        if (hasDBData()) {
            MyApplication application = MyApplication.getMyApplication();
            if (application != null) {
                File file = application.getDatabasePath(IMConstants.DB_NAME).getAbsoluteFile();
                if (file.exists()) {
                    return file.length();
                }
            }
        }
        return 0L;
    }

    /**
     * 删除数据库中所有信息
     */
    public void deleteDBData() {
        DBMsgTipUtil.getInstance().deleteAllData();
        getMessage(IMUtils.IM_MESSAGE_TYPE2);
    }

    /**
     * String 转 html
     *
     * @param string 字符串
     * @return String
     */
    public String htmlFormat(Integer colorValue, String string) {
        String html = "<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <p>\n" +
                "<span style=" +
                "\"color: " +
                colorValue +
                "; font-size: 14px;\">" +
                string +
                "</span>\n" +
                "</p>\n" +
                " </body>\n" +
                "</html>";
        return html;
    }

    /**
     * 替换、过滤特殊字符 //替换中文标号
     *
     * @param str
     * @return
     */
    public static String StringFilter(String str) {
        try {
            return StringUtils.null2Length0(str).replaceAll("【", "[").replaceAll("】", "]").replaceAll("！", "!").replaceAll("，", ",");
        } catch (Exception e) {
            return "";
        }

    }
}

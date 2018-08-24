package com.haxi.mh.constant;

/**
 * 常量
 * Created by Han on 2017/12/27
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class Constant {
    /* fragment切换的tag */
    public static final String TAG_FRAGMENT1 = "fragment1";
    public static final String TAG_FRAGMENT2 = "fragment2";
    public static final String TAG_FRAGMENT3 = "fragment3";
    public static final String TAG_FRAGMENT4 = "fragment4";

    /* face++网络请求base */
    public static final String BASE_URL = "https://api-cn.faceplusplus.com/cardpp/v1/";
    public static final String BASE_BJ_URL = "http://www.zkhonry.com:9000/zkhonry-mobile-interface/";
    public static final String FACE_API_KEY = "fMOByyGzrehIRTb6BRRFJCjWE0oMCsVB";
    public static final String FACE_API_SECRET = "-OQcEHIYrSjP_jkbCymFxTuw9pRIQRYP";

    /* 文件后缀 */
    public static String WORD = "doc,docx,dot,dotx,docm,dotm,rtf,wps,wpt,xml";
    public static String EXCEL = "et,ett,excel,xls,xlsx,xlsm,xltx,xltm,xlsb,xlam,xlt";
    public static String PPT = "dps,dpt,ppt,pptx,pptm,ppsx,potx,potm,ppam,pot,pps";
    public static String TXT = "txt";
    public static String PDF = "pdf";
    public static String ZIP = "7z,rar,zip";
    public static String PIC = "jpg,jpeg,png,gif,bmp,webp";
    public static String VIDEO = "3gp,3gpp,3g2,3gpp2,avi,asx,flv,mpeg,mpe,mpg,mp1,mp2,mp4,m4v,mov,mkv,rm,rmvb,sf,ts,webm,wmv";
    public static String MUSIC = "asf,aac,ape,flac,m4a,mp3,mmf,mid,ra,tti,wma,wav";

    /* 监听物理按键 只能动态注册使用 */
    public static final String LOG_TAG = "HomeReceiver";
    public static final String SYSTEM_DIALOG_REASON_KEY = "reason";
    public static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
    public static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    public static final String SYSTEM_DIALOG_REASON_LOCK = "lock";
    public static final String SYSTEM_DIALOG_REASON_ASSIST = "assist";

    /* 数据库是否开启加密 */
    public static final Boolean DB_RELEASE = true;
    /* 数据库秘钥 */
    public static final String DB_KEY = "mhdb";
    /* 数据库名称 */
    public static final String DB_NAME = "mh";
    /* 华为推送Code */
    public static final int REQUEST_HMS_RESOLVE_ERROR = 101;
    public static final String REQUEST_HMS_EXTRA_RESULT = "intent.extra.RESULT";
    /* 是否打印log */
    public static final Boolean IS_OPEN_LOG = true;

    /* 界面跳转code */
    public static final int REGISTER_CODE = 1110;

    /* 桌面小插件广播 */
    public static final String RECEIVER_WIDGET = "com.mh.appwidget";

    /* 打log */
    public static final String LOG = "----mhlog----";
}

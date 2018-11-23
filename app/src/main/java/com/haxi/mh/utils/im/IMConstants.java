package com.haxi.mh.utils.im;


import java.util.HashMap;
import java.util.Timer;

/**
 * 消息类型常量类
 * Created by Han on 2018/10/15
 * Email:yin13753884368@163.com
 */

public class IMConstants {

    //<editor-fold desc="openfire 配置">
    /*openfire IP地址*/
    public static final String HOST = "";
    /*openfire 服务名*/
    public static final String SERVICE_NAME = "";
    /*openfire 端口*/
    public static final int PORT = 5222;
    /*baseurl*/
    public static String baseUrl = ""; //接口根路径
    /*关闭打开log false关闭  true打开*/
    public static final boolean LOG_SWITCH = true;
    /*调试数据库 false不加密  true加密*/
    public static final boolean DB_ENCRYPTION = true;
    /*数据库名称*/
    public static final String DB_NAME = "db_name";
    /*数据库秘钥*/
    public static final String DB_KEY = "hrpatientdb";
    /*离线信息的时间差*/
    public static final long RECEIVE_MESSAGE_TIME = 1000L;
    /*获取消息的条数*/
    public static final int MESSAGE_NUM = 10;
    /*华为厂商*/
    public static final String BRAND_HUAWEI = "huawei";
    /*小米厂商*/
    public static final String BRAND_XIAOMI = "xiaomi";
    /*新闻类通知默认账号*/
    public static final String KNOWLEDGE_ACCOUNT = "knowledge_honry";
    /*新闻类通知默认昵称*/
    public static final String KNOWLEDGE_ACCOUNT_NAME = "knowledge_honry_name";
    /*新闻类H5路径*/
    public static String WEB_URL_KNOWLEDGE = baseUrl + "information/viewInformationById.action?infoid=";
    /*跳转标题*/
    public static final String INTENT_TITLE = "intent_title";
    /*跳转url*/
    public static final String INTENT_URL = "intent_url";
    //</editor-fold >


    //<editor-fold desc="Openfire 消息类型 聊天">

    public static final String MSG_TYPE_ADD_FRIEND = "msg_type_add_friend";//添加好友

    public static final String MSG_TYPE_NO_ADD_FRIEND = "msg_type_no_add_friend";//拒绝添加好友

    public static final String MSG_TYPE_TRUE_ADD_FRIEND = "msg_type_true_add_friend";//同意添加好友

    public static final String MSG_TYPE_TEXTMESSAGE = "mag_type_text_message";//文本类型消息

    public static final String MSG_TYPE_SERVER_RECEIPT_MESSAGE = "server_receipt_message";//发到服务器回执的消息

    public static final String MSG_TYPE_RECEIPT_READ_MESSAGE = "msg_type_receipt_read_message";//已读消息的回执

    public static final String MSG_TYPE_WITHDRAW_MESSAGE = "msg_type_withdraw_message";//撤回消息

    public static final String MSG_PATIENT_TYPE_TEXTMESSAGE = "msg_patient_type_textmessage";//首页患者类型消息

    public static final String MSG_TYPE_PERMISSION = "msy_type_permission";//预留回复权限

    public static final String MSG_TYPE_IMG = "msg_type_img";//发送图片

    public static final String MSG_TYPE_VIDEO = "msg_type_video";//语音

    public static final String MSG_TYPE_TEXT = "msg_type_text";//文本

    public static final String MSG_TYPE_GROUP = "msg_type_group";//群组

    public static final String MSG_TYPE_FILE = "msg_type_file";//文件

    public static final String ACTION_ADDFRIEND = "com.honry.hrpatient.addfriend";//添加好友

    public static final String ACTION_NO_ADDFRIEND = "com.honry.hrpatient.add_no_friend";//拒绝添加好友

    public static final String ACTION_TRUE_ADDFRIEND = "com.honry.hrpatient.add_true_friend";//同意添加好友

    public static final String ACTION_TEXT_MESSAGE = "com.android.hr.textmessage";

    public static final String ACTION_BROADCAST_ISCHAT_NOTICE = "com.android.hr.broadcast_ischat_notice";//是否在聊天界面，看是否需要语音提示。

    public static final String ACTION_MSG_TYPE_IFREADON = "msg_type_ifReadOn";//已读和未读状态开关 0开 1未开

    //</editor-fold >


    //<editor-fold desc="Openfire 消息类型 广播">
    public static final String MSG_BROADCAST_TYPE_KNOWLEDGE_NOTIFICATION = "msg_type_lyt_news";//新闻通知

    public static final String MSG_BROADCAST_TYPE_RECHARGE_REMINDER = "msg_type_lyt_balanceCost";//充值提醒

    public static final String MSG_BROADCAST_TYPE_MEDICAL_REMINDER = "msg_type_lyt_visit";//就诊提醒

    public static final String MSG_BROADCAST_TYPE_CALL_NOTIFICATION = "msg_type_lyt_getNumber";//取号成功通知

    public static final String MSG_BROADCAST_TYPE_RETREAT_REMINDER = "msg_type_lyt_backNum";//退号成功通知

    public static final String MSG_BROADCAST_TYPE_REGISTRATION_APPOINTMENT_REMINDER = "msg_type_lyt_ghyy";//挂号预约提醒

    public static final String MSG_BROADCAST_TYPE_CANCEL_REGISTRATION_APPOINTMENT_REMINDER = "msg_type_lyt_cancelYYGH";//取消预约挂号通知

    public static final String MSG_BROADCAST_TYPE_WAITING_FOR_CALL_REMINDER = "msg_type_lyt_hzjh";//候诊叫号提醒

    public static final String MSG_BROADCAST_TYPE_ADVICE_REMINDER = "msg_type_lyt_advice_back";//意见反馈通知

    public static final String MSG_BROADCAST_TYPE_MEDICAL_REMINDER_REFRESH = "msg_type_lyt_visitRefresh";//候诊叫号

    public static final String MSG_BROADCAST_TYPE_UPDATE_APP = "msg_type_lyt_android_version";//APP升级通知


    //</editor-fold >


    //<editor-fold desc="Openfire 界面控制">
    public static final String ACTION_UPDATE_APP = "com.honry.hrpatient.action.update.app";//APP升级

    public static final String ACTION_LOGIN_SUCCESS = "com.honry.hrpatient.action.login.success";//登陆成功

    public static final String ACTION_TOKEN_REFRESH = "com.honry.hrpatient.action.token.refresh";//获取到token上传

    public static final String ACTION_LOGIN_OPENFIRE_SUCCESS_STATE = "com.honry.hrpatient.action.login.openfire.success.state";//openfire登录状态 成功

    public static final String ACTION_LOGIN_OPENFIRE_FAILURE_STATE = "com.honry.hrpatient.action.login.openfire.failure.state";//openfire登录状态 失败

    public static final String ACTION_TEXT_MESSAGE_REFRESH = "com.honry.hrpatient.action.text.message.refresh";//刷新界面

    public static final String ACTION_TEXT_MESSAGE_STATE_NO = "com.honry.hrpatient.action.text.message.state.no";//无新消息

    public static final String ACTION_TEXT_MESSAGE_STATE_YES = "com.honry.hrpatient.action.text.message.state.yes";//有新消息

    public static final String ACTION_APP_MAIN = "com.honry.hrpatient.action.app.main";//进入首页信息

    public static final String ACTION_APP_BACK = "com.honry.hrpatient.action.app.back";//最小化退出openfire

    public static final String ACTION_APP_ONSTART = "com.honry.hrpatient.action.app.onstart";//前台

    public static final String ACTION_APP_ONSTOP = "com.honry.hrpatient.action.app.onstop";//后台

    /*用来记录锁屏开屏状态*/
    public static final HashMap<Object, Boolean> recording = new HashMap<>();
    public static final HashMap<Object, Timer> timer = new HashMap<>();

    //</editor-fold >


}

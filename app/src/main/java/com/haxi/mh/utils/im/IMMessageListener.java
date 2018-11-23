package com.haxi.mh.utils.im;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.haxi.mh.model.db.DBMsgTipDoInfo;
import com.haxi.mh.model.db.IMMessageInfo;
import com.haxi.mh.utils.StringUtils;
import com.haxi.mh.utils.dbutil.DBMsgTipUtil;
import com.haxi.mh.utils.model.LogUtils;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;


/**
 * openfire监听
 * Created by Han on 2018/10/15
 * Email:yin13753884368@163.com
 */

public class IMMessageListener implements ChatMessageListener {


    private int mSign = 1;//通知栏标签
    private Context context;//

    public IMMessageListener(Context context) {
        this.context = context;
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        mSign++;
        String msgBody = message.getBody();
        if (StringUtils.isEmpty(msgBody)) return;
        try {
            LogUtils.e("-------IMMessageListener msgBody==" + msgBody);
            IMMessageInfo info = new Gson().fromJson(msgBody, IMMessageInfo.class);
            if (info == null) return;
            if (message.getType().equals(Message.Type.chat)) {
                //处理聊天
                String msgType = info.getType();
                if (!TextUtils.equals(msgType, IMConstants.MSG_TYPE_SERVER_RECEIPT_MESSAGE)) {
                    IMManager.getInstance().sendChatMessage("cd##" + message.getStanzaId() + "##cd", "20161209");
                }
                switch (msgType) {
                    case IMConstants.MSG_TYPE_ADD_FRIEND:
                        //添加好友申请

                        break;
                    case IMConstants.MSG_TYPE_NO_ADD_FRIEND:
                        //添加好友拒绝

                        break;
                    case IMConstants.MSG_TYPE_TRUE_ADD_FRIEND:
                        //添加好友同意

                        break;
                    case IMConstants.MSG_TYPE_TEXTMESSAGE:

                        break;
                    case IMConstants.MSG_TYPE_RECEIPT_READ_MESSAGE:

                        break;

                    case IMConstants.MSG_TYPE_WITHDRAW_MESSAGE:

                        break;
                }
            } else {
                //处理广播
                switch (info.getMsg_type()) {
                    case IMConstants.MSG_BROADCAST_TYPE_UPDATE_APP:
                        //APP升级通知
                        break;
                    case IMConstants.MSG_BROADCAST_TYPE_KNOWLEDGE_NOTIFICATION:
                        //新闻通知
                        addBroadMessage(info, IMConstants.MSG_BROADCAST_TYPE_KNOWLEDGE_NOTIFICATION);
                        break;
                    case IMConstants.MSG_BROADCAST_TYPE_RECHARGE_REMINDER:
                        //充值提醒
                        addBroadMessage(info, IMConstants.MSG_BROADCAST_TYPE_RECHARGE_REMINDER);
                        break;
                    case IMConstants.MSG_BROADCAST_TYPE_MEDICAL_REMINDER:
                        //就诊提醒
                        addBroadMessage(info, IMConstants.MSG_BROADCAST_TYPE_MEDICAL_REMINDER);
                        break;
                    case IMConstants.MSG_BROADCAST_TYPE_CALL_NOTIFICATION:
                        //取号成功通知
                        addBroadMessage(info, IMConstants.MSG_BROADCAST_TYPE_CALL_NOTIFICATION);
                        break;
                    case IMConstants.MSG_BROADCAST_TYPE_REGISTRATION_APPOINTMENT_REMINDER:
                        //挂号预约提醒
                        addBroadMessage(info, IMConstants.MSG_BROADCAST_TYPE_REGISTRATION_APPOINTMENT_REMINDER);
                        break;
                    case IMConstants.MSG_BROADCAST_TYPE_RETREAT_REMINDER:
                        //退号成功通知
                        addBroadMessage(info, IMConstants.MSG_BROADCAST_TYPE_RETREAT_REMINDER);
                        break;
                    case IMConstants.MSG_BROADCAST_TYPE_CANCEL_REGISTRATION_APPOINTMENT_REMINDER:
                        //取消预约挂号通知
                        addBroadMessage(info, IMConstants.MSG_BROADCAST_TYPE_CANCEL_REGISTRATION_APPOINTMENT_REMINDER);
                        break;
                    case IMConstants.MSG_BROADCAST_TYPE_WAITING_FOR_CALL_REMINDER:
                        //候诊叫号提醒
                        addBroadMessage(info, IMConstants.MSG_BROADCAST_TYPE_WAITING_FOR_CALL_REMINDER);
                        break;
                    case IMConstants.MSG_BROADCAST_TYPE_ADVICE_REMINDER:
                        //意见反馈通知
                        addBroadMessage(info, IMConstants.MSG_BROADCAST_TYPE_ADVICE_REMINDER);
                        break;
                    case IMConstants.MSG_BROADCAST_TYPE_MEDICAL_REMINDER_REFRESH:
                        //候诊叫号
                        updateUI(info, IMConstants.MSG_BROADCAST_TYPE_MEDICAL_REMINDER_REFRESH);
                        break;
                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * 刷新界面数据
     *
     * @param info IMMessageInfo
     * @param type 候诊叫号 {@link IMConstants#MSG_BROADCAST_TYPE_MEDICAL_REMINDER_REFRESH}
     */
    private void updateUI(IMMessageInfo info, String type) {
        Intent intent = null;
        switch (type) {
            case IMConstants.MSG_BROADCAST_TYPE_MEDICAL_REMINDER_REFRESH:
                //候诊叫号
                intent = new Intent(IMConstants.MSG_BROADCAST_TYPE_MEDICAL_REMINDER_REFRESH);
                String str1 = info.getStr1();//就诊人id
                intent.putExtra(IMConstants.INTENT_TITLE, str1);
                if (context != null)
                    context.sendBroadcast(intent);
                break;
        }
    }

    /**
     * 广播信息处理
     * 新闻通知  msgtip_type 0 {@link IMConstants#MSG_BROADCAST_TYPE_KNOWLEDGE_NOTIFICATION} 新闻通知比较特殊在不登陆时也可以看到，特殊处理
     * 充值提醒  msgtip_type 1 {@link IMConstants#MSG_BROADCAST_TYPE_RECHARGE_REMINDER}
     * 就诊提醒  msgtip_type 2 {@link IMConstants#MSG_BROADCAST_TYPE_MEDICAL_REMINDER}
     * 取号成功通知  msgtip_type 3 {@link IMConstants#MSG_BROADCAST_TYPE_CALL_NOTIFICATION}
     * 挂号预约提醒  msgtip_type 4 {@link IMConstants#MSG_BROADCAST_TYPE_REGISTRATION_APPOINTMENT_REMINDER}
     * 退号成功通知  msgtip_type 5 {@link IMConstants#MSG_BROADCAST_TYPE_RETREAT_REMINDER}
     * 取消预约挂号通知  msgtip_type 6 {@link IMConstants#MSG_BROADCAST_TYPE_CANCEL_REGISTRATION_APPOINTMENT_REMINDER}
     * 候诊叫号提醒  msgtip_type 7 {@link IMConstants#MSG_BROADCAST_TYPE_WAITING_FOR_CALL_REMINDER}
     * 意见反馈通知  msgtip_type 8 {@link IMConstants#MSG_BROADCAST_TYPE_ADVICE_REMINDER}
     *
     * @param info    IMMessageInfo
     * @param msgType 广播类型
     */
    private void addBroadMessage(IMMessageInfo info, String msgType) {
        IMNotification.getInstance().showNotice(info, mSign, msgType);

        DBMsgTipDoInfo msgTipDoInfo = new DBMsgTipDoInfo();
        msgTipDoInfo.setMsgtip_relation_account(IMUtils.getInstance().getAccount());//关联账号
        msgTipDoInfo.setMsgtip_relation_name(IMUtils.getInstance().getAccountName());//关联昵称
        msgTipDoInfo.setMsgtip_msg_id(StringUtils.null2Length0(info.getMsg_id()));//服务端主键id
        msgTipDoInfo.setMsgtip_msg_type(msgType);//广播类型
        msgTipDoInfo.setMsgtip_title(StringUtils.null2Length0(info.getMsg_menu_name()));//标题
        msgTipDoInfo.setMsgtip_create_time(IMUtils.getInstance().getTimeResultLong(StringUtils.null2Length0(info.getMsg_send_time())));//时间
        msgTipDoInfo.setMsgtip_content(StringUtils.null2Length0(info.getMain_title()));//标题下内容
        msgTipDoInfo.setMsgtip_is_jump(info.getJump_flg());//1可以跳转,2不可以跳转
        msgTipDoInfo.setMsgtip_is_login(true);//true登陆才能看到   false不登陆也能看到 默认是true
        msgTipDoInfo.setMsgtip_is_read(false);//新消息默认都是未读状态
        switch (msgType) {
            case IMConstants.MSG_BROADCAST_TYPE_KNOWLEDGE_NOTIFICATION:
                //新闻通知比较特殊在不登陆时也可以看到，特殊处理
                msgTipDoInfo.setMsgtip_type(0);
                msgTipDoInfo.setMsgtip_relation_account(IMConstants.KNOWLEDGE_ACCOUNT);//默认关联账号
                msgTipDoInfo.setMsgtip_relation_name(IMConstants.KNOWLEDGE_ACCOUNT_NAME);//默认关联昵称
                msgTipDoInfo.setMsgtip_is_login(false);//true登陆才能看到   false不登陆也能看到 特殊处理是false
                msgTipDoInfo.setMsgtip_content1(StringUtils.null2Length0(info.getStr1()));//大图片地址
                msgTipDoInfo.setMsgtip_content2(StringUtils.null2Length0(info.getStr2()));//h5路径
                msgTipDoInfo.setMsgtip_content3(StringUtils.null2Length0(info.getStr3()));//1大图 2小图 3无图
                break;
            case IMConstants.MSG_BROADCAST_TYPE_RECHARGE_REMINDER:
                msgTipDoInfo.setMsgtip_type(1);
                msgTipDoInfo.setMsgtip_content1(StringUtils.null2Length0(info.getPatient_name()));//姓名
                msgTipDoInfo.setMsgtip_content2(StringUtils.null2Length0(info.getCard_type_name()));//就诊卡类型
                msgTipDoInfo.setMsgtip_content3(StringUtils.null2Length0(info.getStr1()));//卡号
                msgTipDoInfo.setMsgtip_content4(StringUtils.null2Length0(info.getTrans_type_name()));//交易类型
                msgTipDoInfo.setMsgtip_content5(String.valueOf(info.getMoney1()));//交易金额
                msgTipDoInfo.setMsgtip_content6(String.valueOf(info.getMoney2()));//账户余额
                break;
            case IMConstants.MSG_BROADCAST_TYPE_MEDICAL_REMINDER:
                msgTipDoInfo.setMsgtip_type(2);
                msgTipDoInfo.setMsgtip_content1(StringUtils.null2Length0(info.getDoc_title_name()));//医生职称
                msgTipDoInfo.setMsgtip_content2(StringUtils.null2Length0(info.getStr1()));//候诊时间
                msgTipDoInfo.setMsgtip_content3(StringUtils.null2Length0(info.getHospital_name()));//就诊医院
                msgTipDoInfo.setMsgtip_content4(StringUtils.null2Length0(info.getDept_name()));//就诊科室
                msgTipDoInfo.setMsgtip_content5(StringUtils.null2Length0(info.getPatient_name()));//就诊人
                msgTipDoInfo.setMsgtip_content6(StringUtils.null2Length0(info.getStr2()));//跳转拼接参数  挂号预约流水号
                msgTipDoInfo.setMsgtip_content7(StringUtils.null2Length0(info.getStr3()));//跳转拼接参数  就诊人id
                msgTipDoInfo.setMsgtip_content8(StringUtils.null2Length0(info.getStr4()));//跳转拼接参数  挂号记录id
                break;
            case IMConstants.MSG_BROADCAST_TYPE_CALL_NOTIFICATION:
                msgTipDoInfo.setMsgtip_type(3);
                msgTipDoInfo.setMsgtip_content1(StringUtils.null2Length0(info.getHospital_name()));//就诊医院
                msgTipDoInfo.setMsgtip_content2(StringUtils.null2Length0(info.getDept_name()));//就诊科室
                msgTipDoInfo.setMsgtip_content3(StringUtils.null2Length0(info.getDoc_title_name()));//医生职称
                msgTipDoInfo.setMsgtip_content4(StringUtils.null2Length0(info.getStr1()));//候诊时间
                msgTipDoInfo.setMsgtip_content5(StringUtils.null2Length0(info.getPatient_name()));//就诊人
                break;
            case IMConstants.MSG_BROADCAST_TYPE_REGISTRATION_APPOINTMENT_REMINDER:
                msgTipDoInfo.setMsgtip_type(4);
                msgTipDoInfo.setMsgtip_content1(StringUtils.null2Length0(info.getDoc_title_name()));//医生职称
                msgTipDoInfo.setMsgtip_content2(StringUtils.null2Length0(info.getStr1()));//候诊时间
                msgTipDoInfo.setMsgtip_content3(StringUtils.null2Length0(info.getHospital_name()));//就诊医院
                msgTipDoInfo.setMsgtip_content4(StringUtils.null2Length0(info.getDept_name()));//就诊科室
                msgTipDoInfo.setMsgtip_content5(StringUtils.null2Length0(info.getPatient_name()));//就诊人
                msgTipDoInfo.setMsgtip_content6(StringUtils.null2Length0(info.getStr2()));//跳转拼接参数  挂号预约流水号
                msgTipDoInfo.setMsgtip_content7(StringUtils.null2Length0(info.getStr4()));//跳转拼接参数  就诊人id
                msgTipDoInfo.setMsgtip_content8(StringUtils.null2Length0(info.getStr5()));//跳转拼接参数  挂号记录id
                break;
            case IMConstants.MSG_BROADCAST_TYPE_RETREAT_REMINDER:
                msgTipDoInfo.setMsgtip_type(5);
                msgTipDoInfo.setMsgtip_content1(StringUtils.null2Length0(info.getHospital_name()));//就诊医院
                msgTipDoInfo.setMsgtip_content2(StringUtils.null2Length0(info.getDept_name()));//就诊科室
                msgTipDoInfo.setMsgtip_content3(StringUtils.null2Length0(info.getDoc_title_name()));//医生职称
                msgTipDoInfo.setMsgtip_content4(StringUtils.null2Length0(info.getStr3()));//候诊时间
                msgTipDoInfo.setMsgtip_content5(StringUtils.null2Length0(info.getPatient_name()));//就诊人
                msgTipDoInfo.setMsgtip_content6(StringUtils.null2Length0(info.getStr4()));//退费提示信息
                msgTipDoInfo.setMsgtip_content7(StringUtils.null2Length0(info.getStr5()));//跳转拼接参数  挂号记录id
                break;
            case IMConstants.MSG_BROADCAST_TYPE_CANCEL_REGISTRATION_APPOINTMENT_REMINDER:
                msgTipDoInfo.setMsgtip_type(6);
                msgTipDoInfo.setMsgtip_content1(StringUtils.null2Length0(info.getDoc_title_name()));//医生职称
                msgTipDoInfo.setMsgtip_content2(StringUtils.null2Length0(info.getStr2()));//候诊时间
                msgTipDoInfo.setMsgtip_content3(StringUtils.null2Length0(info.getHospital_name()));//就诊医院
                msgTipDoInfo.setMsgtip_content4(StringUtils.null2Length0(info.getDept_name()));//就诊科室
                msgTipDoInfo.setMsgtip_content5(StringUtils.null2Length0(info.getPatient_name()));//就诊人
                msgTipDoInfo.setMsgtip_content6(StringUtils.null2Length0(info.getStr3()));//跳转拼接参数  挂号记录id
                break;
            case IMConstants.MSG_BROADCAST_TYPE_WAITING_FOR_CALL_REMINDER:
                msgTipDoInfo.setMsgtip_type(7);
                msgTipDoInfo.setMsgtip_content1(StringUtils.null2Length0(info.getHospital_name()));//就诊医院
                msgTipDoInfo.setMsgtip_content2(StringUtils.null2Length0(info.getDept_name()));//就诊科室
                msgTipDoInfo.setMsgtip_content3(StringUtils.null2Length0(info.getStr3()));//就诊地点
                msgTipDoInfo.setMsgtip_content4(StringUtils.null2Length0(info.getDoc_title_name()));//医生职称
                msgTipDoInfo.setMsgtip_content5(StringUtils.null2Length0(info.getStr4()));//候诊时间
                msgTipDoInfo.setMsgtip_content6(StringUtils.null2Length0(info.getPatient_name()));//就诊人
                msgTipDoInfo.setMsgtip_content7(StringUtils.null2Length0(info.getStr5()));//跳转拼接参数  跳转候诊叫号 就诊人id
                break;
            case IMConstants.MSG_BROADCAST_TYPE_ADVICE_REMINDER:
                msgTipDoInfo.setMsgtip_type(8);
                msgTipDoInfo.setMsgtip_content1(StringUtils.null2Length0(info.getStr1()));//跳转拼接参数 H5界面
                break;
        }
        DBMsgTipUtil.getInstance().save(msgTipDoInfo);
        IMUtils.getInstance().getMessage(IMUtils.IM_MESSAGE_TYPE1);

    }

}

package com.haxi.mh.model.db;

import java.io.Serializable;
import java.util.List;

/**
 * 消息传递的model
 * Created by Han on 2018/10/15
 * Email:yin13753884368@163.com
 */

public class IMMessageInfo implements Serializable {

    private String from;
    private String to;
    private String fromname;
    private String type; //消息类型
    private String content;
    private long sendTime;
    private boolean isFile;
    private String filePath;//文件预览的路径
    private String fileType;
    private String rosterPhoto;//用户个人头像地址
    private String toName;//对方的昵称
    private String fileName;//文件名称
    private String fileSize;//文件大小
    private String fileCompressPath;//文件压缩的路径
    private boolean isPatient;//是否患者信息
    private String patientType;//出诊类型
    private String patientAge; //患者年龄
    private String patientSex; //患者性别
    private String patientDomainName;//域名
    private List<IMMessageInfo> isReadList;//未读消息集合


    //--------------------------------广播用------------------------------------------

    /**
     * msg_menu_name : 就诊提醒
     * msg_type : msg_type_lyt_balanceCost
     * msg_send_time : 2018-10-17 10:59:50
     * main_title : 您好，尾号为3827的就诊卡在2018-10-17 10:59:50成功进行了一笔交易。
     * patient_name : 张晨光
     * card_type_name : 就诊卡
     * card_num : 000763827
     * trans_type : 充值
     * trans_cost : 150
     * own_balance : 152
     * jump_flg : 1
     * jump_param1 : jump_param1
     *
     * hospital_name : 郑州大学第一附属医院
     * dept_name : 骨科1门诊（郑东）
     * visit_time : 2018-10-17 11:30~12:00
     * doc_title_name : 副主任医师
     * field1 : 000763827
     * trans_type_name : 充值
     * money1 : 150.0
     * money2 : 152.0
     */
    private String  msg_id;  //消息主键
    private String msg_menu_name;//广播标题
    private String msg_type;//广播类型
    private String msg_send_time;//发送时间
    private String main_title;//标题下内容
    private String patient_name;//患者姓名
    private String card_type_name;//卡类型名称
    private String hospital_name;//医院名称
    private String dept_name;//科室名称
    private String doc_title_name;//医生职称
    private String trans_type_name;//交易类型名称
    private String pay_type_name ;//支付方式名称
    private int jump_flg;//是否可以跳转 1可以跳转,2不可以跳转

    /*预留String类型字段*/
    private String str1;
    private String str2;
    private String str3;
    private String str4;
    private String str5;
    private String str6;
    /*预留Float金额字段*/
    private double money1;
    private double money2;
    private double money3;
    /*预留String类型字段*/
    private String time1;
    private String time2;
    private String time3;
    private double apkNewestVNum;//最新版本号

    public double getApkNewestVNum() {
        return apkNewestVNum;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getFromname() {
        return fromname;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public long getSendTime() {
        return sendTime;
    }

    public boolean isFile() {
        return isFile;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public String getRosterPhoto() {
        return rosterPhoto;
    }

    public String getToName() {
        return toName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public String getFileCompressPath() {
        return fileCompressPath;
    }

    public boolean isPatient() {
        return isPatient;
    }

    public String getPatientType() {
        return patientType;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public String getPatientSex() {
        return patientSex;
    }

    public String getPatientDomainName() {
        return patientDomainName;
    }

    public List<IMMessageInfo> getIsReadList() {
        return isReadList;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public String getMsg_menu_name() {
        return msg_menu_name;
    }

    public String getMsg_type() {
        return msg_type;
    }

    public String getMsg_send_time() {
        return msg_send_time;
    }

    public String getMain_title() {
        return main_title;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public String getCard_type_name() {
        return card_type_name;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public String getDept_name() {
        return dept_name;
    }

    public String getDoc_title_name() {
        return doc_title_name;
    }

    public String getTrans_type_name() {
        return trans_type_name;
    }

    public String getPay_type_name() {
        return pay_type_name;
    }

    public int getJump_flg() {
        return jump_flg;
    }

    public String getStr1() {
        return str1;
    }

    public String getStr2() {
        return str2;
    }

    public String getStr3() {
        return str3;
    }

    public String getStr4() {
        return str4;
    }

    public String getStr5() {
        return str5;
    }

    public String getStr6() {
        return str6;
    }

    public double getMoney1() {
        return money1;
    }

    public double getMoney2() {
        return money2;
    }

    public double getMoney3() {
        return money3;
    }

    public String getTime1() {
        return time1;
    }

    public String getTime2() {
        return time2;
    }

    public String getTime3() {
        return time3;
    }
}

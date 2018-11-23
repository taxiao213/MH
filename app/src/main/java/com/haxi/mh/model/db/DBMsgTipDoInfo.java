package com.haxi.mh.model.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 消息提示model
 * Created by Han on 2018/10/18
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

@Entity
public class DBMsgTipDoInfo {

    @Id(autoincrement = true)
    private Long id;

    private String msgtip_msg_id;//服务端传来的id(只保存，不用)

    private String msgtip_msg_type; //消息类型

    private Integer msgtip_type; //首页消息类型(分类用)

    private String msgtip_relation_account; //关联者账号

    private String msgtip_relation_name; //关联者名称

    private String msgtip_from_account; //发送者的账号

    private String msgtip_from_name; //发送者的名称

    private String msgtip_from_roster_photo;//发送者的头像地址

    private String msgtip_to_account; //接收者的账号

    private String msgtip_to_name; //接收者的名称

    private String msgtip_to_roster_photo;//接收者的头像地址

    private Long msgtip_create_time; //创建时间

    private String msgtip_title; //消息标题

    private String msgtip_content; //内容  [图片]、[语音]、[文件] 或者文本

    private String msgtip_content1; //子内容1
    private String msgtip_content2; //子内容2
    private String msgtip_content3; //子内容3
    private String msgtip_content4; //子内容4
    private String msgtip_content5; //子内容5
    private String msgtip_content6; //子内容6
    private String msgtip_content7; //子内容7
    private String msgtip_content8; //子内容8
    private String msgtip_content9; //子内容9
    private String msgtip_content10; //子内容10

    private Integer msgtip_is_jump;//1 可跳转 2不可跳转

    private Boolean msgtip_is_read; //已读 1  未读 0

    private Integer msgtip_is_top;//是否置顶 >1为置顶 0为否

    private Integer msgtip_save_top;//保存置顶 1为置顶 0为否

    private Integer msgtip_sort_type;//类别排序 2广播通知 1日程安排  0为单聊、群聊信息

    private Integer msgtip_visible;//1 可见 2不可见

    private Integer msgtip_notice_sign;//消除通知栏的标签

    private String msgtip_group_create_account;//房间创建者

    private String msgtip_is_succeed;//是否发送成功的标志  0为成功，1为失败

    private Boolean msgtip_is_delete;//删除的标志

    private Boolean msgtip_is_patient = false;//与患者聊天的标志

    private Boolean msgtip_is_login;//true登陆才能看到   false不登陆也能看到

    private String msgtip_domain_name; //域名


    @Generated(hash = 939807298)
    public DBMsgTipDoInfo() {
    }

    @Generated(hash = 1870413636)
    public DBMsgTipDoInfo(Long id, String msgtip_msg_id, String msgtip_msg_type, Integer msgtip_type,
                          String msgtip_relation_account, String msgtip_relation_name, String msgtip_from_account,
                          String msgtip_from_name, String msgtip_from_roster_photo, String msgtip_to_account, String msgtip_to_name,
                          String msgtip_to_roster_photo, Long msgtip_create_time, String msgtip_title, String msgtip_content,
                          String msgtip_content1, String msgtip_content2, String msgtip_content3, String msgtip_content4,
                          String msgtip_content5, String msgtip_content6, String msgtip_content7, String msgtip_content8,
                          String msgtip_content9, String msgtip_content10, Integer msgtip_is_jump, Boolean msgtip_is_read,
                          Integer msgtip_is_top, Integer msgtip_save_top, Integer msgtip_sort_type, Integer msgtip_visible,
                          Integer msgtip_notice_sign, String msgtip_group_create_account, String msgtip_is_succeed,
                          Boolean msgtip_is_delete, Boolean msgtip_is_patient, Boolean msgtip_is_login, String msgtip_domain_name) {
        this.id = id;
        this.msgtip_msg_id = msgtip_msg_id;
        this.msgtip_msg_type = msgtip_msg_type;
        this.msgtip_type = msgtip_type;
        this.msgtip_relation_account = msgtip_relation_account;
        this.msgtip_relation_name = msgtip_relation_name;
        this.msgtip_from_account = msgtip_from_account;
        this.msgtip_from_name = msgtip_from_name;
        this.msgtip_from_roster_photo = msgtip_from_roster_photo;
        this.msgtip_to_account = msgtip_to_account;
        this.msgtip_to_name = msgtip_to_name;
        this.msgtip_to_roster_photo = msgtip_to_roster_photo;
        this.msgtip_create_time = msgtip_create_time;
        this.msgtip_title = msgtip_title;
        this.msgtip_content = msgtip_content;
        this.msgtip_content1 = msgtip_content1;
        this.msgtip_content2 = msgtip_content2;
        this.msgtip_content3 = msgtip_content3;
        this.msgtip_content4 = msgtip_content4;
        this.msgtip_content5 = msgtip_content5;
        this.msgtip_content6 = msgtip_content6;
        this.msgtip_content7 = msgtip_content7;
        this.msgtip_content8 = msgtip_content8;
        this.msgtip_content9 = msgtip_content9;
        this.msgtip_content10 = msgtip_content10;
        this.msgtip_is_jump = msgtip_is_jump;
        this.msgtip_is_read = msgtip_is_read;
        this.msgtip_is_top = msgtip_is_top;
        this.msgtip_save_top = msgtip_save_top;
        this.msgtip_sort_type = msgtip_sort_type;
        this.msgtip_visible = msgtip_visible;
        this.msgtip_notice_sign = msgtip_notice_sign;
        this.msgtip_group_create_account = msgtip_group_create_account;
        this.msgtip_is_succeed = msgtip_is_succeed;
        this.msgtip_is_delete = msgtip_is_delete;
        this.msgtip_is_patient = msgtip_is_patient;
        this.msgtip_is_login = msgtip_is_login;
        this.msgtip_domain_name = msgtip_domain_name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsgtip_msg_type() {
        return msgtip_msg_type;
    }

    public void setMsgtip_msg_type(String msgtip_msg_type) {
        this.msgtip_msg_type = msgtip_msg_type;
    }

    public Integer getMsgtip_type() {
        return msgtip_type;
    }

    public void setMsgtip_type(Integer msgtip_type) {
        this.msgtip_type = msgtip_type;
    }

    public String getMsgtip_relation_account() {
        return msgtip_relation_account;
    }

    public void setMsgtip_relation_account(String msgtip_relation_account) {
        this.msgtip_relation_account = msgtip_relation_account;
    }

    public String getMsgtip_relation_name() {
        return msgtip_relation_name;
    }

    public void setMsgtip_relation_name(String msgtip_relation_name) {
        this.msgtip_relation_name = msgtip_relation_name;
    }

    public String getMsgtip_from_account() {
        return msgtip_from_account;
    }

    public void setMsgtip_from_account(String msgtip_from_account) {
        this.msgtip_from_account = msgtip_from_account;
    }

    public String getMsgtip_from_name() {
        return msgtip_from_name;
    }

    public void setMsgtip_from_name(String msgtip_from_name) {
        this.msgtip_from_name = msgtip_from_name;
    }

    public String getMsgtip_from_roster_photo() {
        return msgtip_from_roster_photo;
    }

    public void setMsgtip_from_roster_photo(String msgtip_from_roster_photo) {
        this.msgtip_from_roster_photo = msgtip_from_roster_photo;
    }

    public String getMsgtip_to_account() {
        return msgtip_to_account;
    }

    public void setMsgtip_to_account(String msgtip_to_account) {
        this.msgtip_to_account = msgtip_to_account;
    }

    public String getMsgtip_to_name() {
        return msgtip_to_name;
    }

    public void setMsgtip_to_name(String msgtip_to_name) {
        this.msgtip_to_name = msgtip_to_name;
    }

    public String getMsgtip_to_roster_photo() {
        return msgtip_to_roster_photo;
    }

    public void setMsgtip_to_roster_photo(String msgtip_to_roster_photo) {
        this.msgtip_to_roster_photo = msgtip_to_roster_photo;
    }

    public Long getMsgtip_create_time() {
        return msgtip_create_time;
    }

    public void setMsgtip_create_time(Long msgtip_create_time) {
        this.msgtip_create_time = msgtip_create_time;
    }

    public String getMsgtip_title() {
        return msgtip_title;
    }

    public void setMsgtip_title(String msgtip_title) {
        this.msgtip_title = msgtip_title;
    }

    public String getMsgtip_content() {
        return msgtip_content;
    }

    public void setMsgtip_content(String msgtip_content) {
        this.msgtip_content = msgtip_content;
    }

    public String getMsgtip_content1() {
        return msgtip_content1;
    }

    public void setMsgtip_content1(String msgtip_content1) {
        this.msgtip_content1 = msgtip_content1;
    }

    public String getMsgtip_content2() {
        return msgtip_content2;
    }

    public void setMsgtip_content2(String msgtip_content2) {
        this.msgtip_content2 = msgtip_content2;
    }

    public String getMsgtip_content3() {
        return msgtip_content3;
    }

    public void setMsgtip_content3(String msgtip_content3) {
        this.msgtip_content3 = msgtip_content3;
    }

    public String getMsgtip_content4() {
        return msgtip_content4;
    }

    public void setMsgtip_content4(String msgtip_content4) {
        this.msgtip_content4 = msgtip_content4;
    }

    public String getMsgtip_content5() {
        return msgtip_content5;
    }

    public void setMsgtip_content5(String msgtip_content5) {
        this.msgtip_content5 = msgtip_content5;
    }

    public String getMsgtip_content6() {
        return msgtip_content6;
    }

    public void setMsgtip_content6(String msgtip_content6) {
        this.msgtip_content6 = msgtip_content6;
    }

    public Integer getMsgtip_is_jump() {
        return msgtip_is_jump;
    }

    public void setMsgtip_is_jump(Integer msgtip_is_jump) {
        this.msgtip_is_jump = msgtip_is_jump;
    }

    public Boolean getMsgtip_is_read() {
        return msgtip_is_read;
    }

    public void setMsgtip_is_read(Boolean msgtip_is_read) {
        this.msgtip_is_read = msgtip_is_read;
    }

    public Integer getMsgtip_is_top() {
        return msgtip_is_top;
    }

    public void setMsgtip_is_top(Integer msgtip_is_top) {
        this.msgtip_is_top = msgtip_is_top;
    }

    public Integer getMsgtip_save_top() {
        return msgtip_save_top;
    }

    public void setMsgtip_save_top(Integer msgtip_save_top) {
        this.msgtip_save_top = msgtip_save_top;
    }

    public Integer getMsgtip_sort_type() {
        return msgtip_sort_type;
    }

    public void setMsgtip_sort_type(Integer msgtip_sort_type) {
        this.msgtip_sort_type = msgtip_sort_type;
    }

    public Integer getMsgtip_visible() {
        return msgtip_visible;
    }

    public void setMsgtip_visible(Integer msgtip_visible) {
        this.msgtip_visible = msgtip_visible;
    }

    public Integer getMsgtip_notice_sign() {
        return msgtip_notice_sign;
    }

    public void setMsgtip_notice_sign(Integer msgtip_notice_sign) {
        this.msgtip_notice_sign = msgtip_notice_sign;
    }

    public String getMsgtip_group_create_account() {
        return msgtip_group_create_account;
    }

    public void setMsgtip_group_create_account(String msgtip_group_create_account) {
        this.msgtip_group_create_account = msgtip_group_create_account;
    }

    public String getMsgtip_is_succeed() {
        return msgtip_is_succeed;
    }

    public void setMsgtip_is_succeed(String msgtip_is_succeed) {
        this.msgtip_is_succeed = msgtip_is_succeed;
    }

    public Boolean getMsgtip_is_delete() {
        return msgtip_is_delete;
    }

    public void setMsgtip_is_delete(Boolean msgtip_is_delete) {
        this.msgtip_is_delete = msgtip_is_delete;
    }

    public Boolean getMsgtip_is_patient() {
        return msgtip_is_patient;
    }

    public void setMsgtip_is_patient(Boolean msgtip_is_patient) {
        this.msgtip_is_patient = msgtip_is_patient;
    }

    public String getMsgtip_domain_name() {
        return msgtip_domain_name;
    }

    public void setMsgtip_domain_name(String msgtip_domain_name) {
        this.msgtip_domain_name = msgtip_domain_name;
    }

    public String getMsgtip_msg_id() {
        return this.msgtip_msg_id;
    }

    public void setMsgtip_msg_id(String msgtip_msg_id) {
        this.msgtip_msg_id = msgtip_msg_id;
    }

    public String getMsgtip_content7() {
        return this.msgtip_content7;
    }

    public void setMsgtip_content7(String msgtip_content7) {
        this.msgtip_content7 = msgtip_content7;
    }

    public Boolean getMsgtip_is_login() {
        return this.msgtip_is_login;
    }

    public void setMsgtip_is_login(Boolean msgtip_is_login) {
        this.msgtip_is_login = msgtip_is_login;
    }

    public String getMsgtip_content8() {
        return this.msgtip_content8;
    }

    public void setMsgtip_content8(String msgtip_content8) {
        this.msgtip_content8 = msgtip_content8;
    }

    public String getMsgtip_content9() {
        return this.msgtip_content9;
    }

    public void setMsgtip_content9(String msgtip_content9) {
        this.msgtip_content9 = msgtip_content9;
    }

    public String getMsgtip_content10() {
        return this.msgtip_content10;
    }

    public void setMsgtip_content10(String msgtip_content10) {
        this.msgtip_content10 = msgtip_content10;
    }
}

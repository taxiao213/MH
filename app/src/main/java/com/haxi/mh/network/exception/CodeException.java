package com.haxi.mh.network.exception;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * 自定义错误code类型:注解写法
 * 可自由扩展
 * Created by Han on 2017/12/16
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class CodeException {

    /**
     * 网络错误
     */
    public static final int NETWORD_ERROR = 0x101;

    /**
     * 运行时异常-包含自定义异常
     */
    public static final int RUNTIME_ERROR = 0x105;

    /**
     * 连接错误
     */
    public static final int HTTP_ERROR = 0x102;

    /**
     * json解析错误
     */
    public static final int JSON_ERROR = 0x103;

    /**
     * 无法解析该域名
     */
    public static final int UNKOWNHOST_ERROR = 0x104;

    /**
     * 未知错误
     */
    public static final int UNKNOWN_ERROR = 0x106;

    // 定义适用于参数的注解，限定取值范围为{NETWORD_ERROR, HTTP_ERROR, RUNTIME_ERROR, UNKNOWN_ERROR, JSON_ERROR, UNKOWNHOST_ERROR}
    @IntDef({NETWORD_ERROR, HTTP_ERROR, RUNTIME_ERROR, UNKNOWN_ERROR, JSON_ERROR, UNKOWNHOST_ERROR})
    @Retention(RetentionPolicy.SOURCE)


    public @interface CodeEp {
    }

}

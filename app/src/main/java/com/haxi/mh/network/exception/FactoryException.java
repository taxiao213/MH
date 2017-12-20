package com.haxi.mh.network.exception;


import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import retrofit2.adapter.rxjava2.HttpException;


/**
 * 异常处理工厂
 * 主要是解析异常，输出自定义ApiException
 * Created by Han on 2017/12/16
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class FactoryException {

    private static final String HTTPEXCEPTION_MSG = "网络错误";
    private static final String CONNECTEXCEPTION_MSG = "连接失败";
    private static final String JSONEXCEPTION_MSG = "json解析失败";
    private static final String UNKNOWNHOSTEXCEPTION_MSG = "无法解析该域名";

    /**
     * 解析异常
     *
     * @param e
     * @return
     */
    public static ApiException analysisExcetpion(Throwable e) {
        ApiException apiException = new ApiException(e);
        if (e instanceof HttpException) {
             /*网络异常*/
            apiException.setCode(CodeException.NETWORD_ERROR);
            apiException.setDisplayMessage(HTTPEXCEPTION_MSG);
        } else if (e instanceof CustomTimeException) {
             /*自定义运行时异常*/
            apiException.setCode(CodeException.RUNTIME_ERROR);
            apiException.setDisplayMessage(e.getMessage());
        } else if (e instanceof ConnectException ||e instanceof SocketTimeoutException) {
             /*连接异常*/
            apiException.setCode(CodeException.HTTP_ERROR);
            apiException.setDisplayMessage(CONNECTEXCEPTION_MSG);
        } else if ( e instanceof JSONException || e instanceof ParseException) {
            /*json解析失败*/
            apiException.setCode(CodeException.JSON_ERROR);
            apiException.setDisplayMessage(JSONEXCEPTION_MSG);
        }else if (e instanceof UnknownHostException){
            /*无法解析该域名异常*/
            apiException.setCode(CodeException.UNKOWNHOST_ERROR);
            apiException.setDisplayMessage(UNKNOWNHOSTEXCEPTION_MSG);
        } else {
            /*未知异常*/
            apiException.setCode(CodeException.UNKNOWN_ERROR);
            apiException.setDisplayMessage(e.getMessage());
        }
        return apiException;
    }
}

package com.haxi.mh.utils.dense;

import android.util.Base64;

import java.io.IOException;

/**
 * Base64转换工具类
 * Created by Han on 2018/9/27
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class Base64Util {

    /**
     * 字节数组转Base64编码
     * @param bytes 字节数组
     * @return 字节数组
     */
    public static String byte2Base64(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }


    /**
     * Base64编码转字节数组
     * @param base64Key Base64编码
     * @return Base64编码
     * @throws IOException
     */
    public static byte[] base642Byte(String base64Key) throws IOException {
        return Base64.decode(base64Key, Base64.NO_WRAP);
    }
}

package com.haxi.mh.utils.dense;

import com.haxi.mh.utils.model.LogUtils;

import org.json.JSONObject;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.SecretKey;

/**
 * 双向RSA + AES 加密解密
 * 双向ECC + AES 加密解密
 * Created by Han on 2018/9/26
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class HttpEncryptUtil {

    //################################双向RSA + AES ##########################

    /**
     * APP端加密请求内容
     *
     * @param action  需要加密的路径地址
     * @param content 需要加密的内容
     * @return 返回map
     * @throws Exception
     */
    public static Map<String, String> appEncrypt(String action, String content) throws Exception {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        //将Base64编码后的(server端)公钥转换成PublicKey对象
        PublicKey serverPublicKey = RSAUtil.string2PublicKey(KeyUtil.SERVER_PUBLIC_KEY.replaceAll("\n", ""));
        //每次都随机生成AES秘钥
        String aesKeyStr = AESUtil.genKeyAES();
        SecretKey aesKey = AESUtil.loadKeyAES(aesKeyStr);
        //用(server端)公钥加密AES秘钥
        byte[] encryptAesKey = RSAUtil.publicEncrypt(aesKeyStr.getBytes("utf-8"), serverPublicKey);
        //用AES秘钥加密路径地址
        byte[] encryptActionRequest = AESUtil.encryptAES(action.getBytes("utf-8"), aesKey);
        //用AES秘钥加密请求内容
        byte[] encryptContentRequest = AESUtil.encryptAES(content.getBytes("utf-8"), aesKey);
        map.put("ak", Base64Util.byte2Base64(encryptAesKey));//加密aesKey
        map.put("act", Base64Util.byte2Base64(encryptActionRequest));//加密路径
        map.put("data", Base64Util.byte2Base64(encryptContentRequest));//加密内容
        LogUtils.e("----AES加密秘钥 ----" + aesKeyStr);
        LogUtils.e("----AES加密数据 ---- ak==" + Base64Util.byte2Base64(encryptAesKey));
        LogUtils.e("----AES加密数据 ---- act==" + Base64Util.byte2Base64(encryptActionRequest));
        LogUtils.e("----AES加密数据 ---- data==" + Base64Util.byte2Base64(encryptContentRequest));
        return map;
    }


    /**
     * APP解密服务器的响应内容
     *
     * @param content 内容
     * @return
     * @throws Exception
     */
    public static String appDecrypt(String content) throws Exception {
        JSONObject result = new JSONObject(content);
        String decryptAesKey = (String) result.get("ak");//加密后的aesKey
        String decryptContent = (String) result.get("data");//内容
        //将Base64编码后的APP私钥转换成PrivateKey对象
        PrivateKey appPrivateKey = RSAUtil.string2PrivateKey(KeyUtil.SERVER_PRIVATE_KEY.replace("\n", ""));
        //用APP私钥解密AES秘钥
        byte[] aesKeyBytes = RSAUtil.privateDecrypt(Base64Util.base642Byte(decryptAesKey), appPrivateKey);
        //用AES秘钥解密请求内容
        String base64Key = new String(aesKeyBytes);
        SecretKey aesKey = AESUtil.loadKeyAES(base64Key);
        byte[] response = AESUtil.decryptAES(Base64Util.base642Byte(decryptContent), aesKey);
        LogUtils.e("----AES解密秘钥 ----" + base64Key);
        return new String(response);
    }


    /**
     * APP加密请求内容
     *
     * @param content 加密内容
     * @return
     * @throws Exception
     */
    public static String appEncrypt(String content) throws Exception {
        //将Base64编码后的Server公钥转换成PublicKey对象
        PublicKey serverPublicKey = RSAUtil.string2PublicKey(KeyUtil.SERVER_PUBLIC_KEY.replaceAll("\n", ""));
        //每次都随机生成AES秘钥
        String aesKeyStr = AESUtil.genKeyAES();
        SecretKey aesKey = AESUtil.loadKeyAES(aesKeyStr);
        //用Server公钥加密AES秘钥
        byte[] encryptAesKey = RSAUtil.publicEncrypt(aesKeyStr.getBytes("utf-8"), serverPublicKey);
        //用AES秘钥加密请求内容
        byte[] encryptContentRequest = AESUtil.encryptAES(content.getBytes("utf-8"), aesKey);
        JSONObject result = new JSONObject();
        String value = Base64Util.byte2Base64(encryptAesKey);
        result.put("ak", value);
        String value1 = Base64Util.byte2Base64(encryptContentRequest);
        result.put("data", value1);
        LogUtils.e("----AES加密秘钥 ----" + aesKeyStr);
        LogUtils.e("----AES加密数据 ---- ak==" + value);
        LogUtils.e("----AES加密数据 ---- data==" + value1);
        return result.toString();
    }


    //################################双向ECC + AES ##########################


    //APP加密请求内容
    public static String appECCEncrypt(String appPublicKeyStr, String content) throws Exception {
        //将Base64编码后的Server公钥转换成PublicKey对象
        PublicKey serverPublicKey = ECCUtil.string2PublicKey(KeyUtil.SERVER_PUBLIC_KEY.replaceAll("\r\n", ""));
        //每次都随机生成AES秘钥
        String aesKeyStr = AESUtil.genKeyAES();
        SecretKey aesKey = AESUtil.loadKeyAES(aesKeyStr);
        //用Server公钥加密AES秘钥
        byte[] encryptAesKey = ECCUtil.publicEncrypt(aesKeyStr.getBytes(), serverPublicKey);
        //用AES秘钥加密APP公钥
        byte[] encryptAppPublicKey = AESUtil.encryptAES(appPublicKeyStr.getBytes(), aesKey);
        //用AES秘钥加密请求内容
        byte[] encryptRequest = AESUtil.encryptAES(content.getBytes(), aesKey);

        JSONObject result = new JSONObject();
        result.put("ak", Base64Util.byte2Base64(encryptAesKey).replaceAll("\r\n", ""));
        result.put("apk", Base64Util.byte2Base64(encryptAppPublicKey).replaceAll("\r\n", ""));
        result.put("ct", Base64Util.byte2Base64(encryptRequest).replaceAll("\r\n", ""));
        return result.toString();
    }

    //APP解密服务器的响应内容
    public static String appECCDecrypt(String appPrivateKeyStr, String content) throws Exception {
        JSONObject result = new JSONObject(content);
        String encryptAesKeyStr = result.optString("ak");
        String encryptContent = result.optString("ct");

        //将Base64编码后的APP私钥转换成PrivateKey对象
        PrivateKey appPrivateKey = ECCUtil.string2PrivateKey(appPrivateKeyStr);
        //用APP私钥解密AES秘钥
        byte[] aesKeyBytes = ECCUtil.privateDecrypt(Base64Util.base642Byte(encryptAesKeyStr), appPrivateKey);
        //用AES秘钥解密请求内容
        SecretKey aesKey = AESUtil.loadKeyAES(new String(aesKeyBytes));
        byte[] response = AESUtil.decryptAES(Base64Util.base642Byte(encryptContent), aesKey);

        return new String(response);
    }

    //服务器加密响应给APP的内容
    public static String serverECCEncrypt(String appPublicKeyStr, String aesKeyStr, String content) throws Exception {
        //将Base64编码后的APP公钥转换成PublicKey对象
        PublicKey appPublicKey = ECCUtil.string2PublicKey(appPublicKeyStr);
        //将Base64编码后的AES秘钥转换成SecretKey对象
        SecretKey aesKey = AESUtil.loadKeyAES(aesKeyStr);
        //用APP公钥加密AES秘钥
        byte[] encryptAesKey = ECCUtil.publicEncrypt(aesKeyStr.getBytes(), appPublicKey);
        //用AES秘钥加密响应内容
        byte[] encryptContent = AESUtil.encryptAES(content.getBytes(), aesKey);

        JSONObject result = new JSONObject();
        result.put("ak", Base64Util.byte2Base64(encryptAesKey).replaceAll("\r\n", ""));
        result.put("ct", Base64Util.byte2Base64(encryptContent).replaceAll("\r\n", ""));
        return result.toString();
    }

    //服务器解密APP的请求内容
    public static String serverECCDecrypt(String content) throws Exception {
        JSONObject result = new JSONObject(content);
        String encryptAesKeyStr = result.optString("ak");
        String encryptAppPublicKeyStr = result.optString("apk");
        String encryptContent = result.optString("ct");

        //将Base64编码后的Server私钥转换成PrivateKey对象
        PrivateKey serverPrivateKey = ECCUtil.string2PrivateKey(KeyUtil.SERVER_PRIVATE_KEY.replaceAll("\r\n", ""));
        //用Server私钥解密AES秘钥
        byte[] aesKeyBytes = ECCUtil.privateDecrypt(Base64Util.base642Byte(encryptAesKeyStr), serverPrivateKey);
        //用AES秘钥解密APP公钥
        SecretKey aesKey = AESUtil.loadKeyAES(new String(aesKeyBytes));
        byte[] appPublicKeyBytes = AESUtil.decryptAES(Base64Util.base642Byte(encryptAppPublicKeyStr), aesKey);
        //用AES秘钥解密请求内容
        byte[] request = AESUtil.decryptAES(Base64Util.base642Byte(encryptContent), aesKey);

        JSONObject result2 = new JSONObject();
        result2.put("ak", new String(aesKeyBytes));
        result2.put("apk", new String(appPublicKeyBytes));
        result2.put("ct", new String(request));
        return result2.toString();
    }
}

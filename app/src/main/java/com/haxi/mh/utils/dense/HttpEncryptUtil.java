package com.haxi.mh.utils.dense;

import com.haxi.mh.utils.model.LogUtils;

import org.json.JSONObject;

import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;

/**
 * Created by Han on 2018/9/26
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class HttpEncryptUtil {

    //################################双向RSA + AES ##########################

    /**
     * APP加密请求内容
     *
     * @param content 加密内容
     * @return
     * @throws Exception
     */
    public static String appEncrypt1(String content) throws Exception {
        //将Base64编码后的Server公钥转换成PublicKey对象
        PublicKey serverPublicKey = RSAUtil.string2PublicKey(KeyUtil.APP_PUBLIC_KEY);
        //每次都随机生成AES秘钥
        String aesKeyStr = AESUtil.genKeyAES();
        SecretKey aesKey = AESUtil.loadKeyAES(aesKeyStr);
        //用Server公钥加密AES秘钥
        byte[] encryptAesKey = RSAUtil.publicEncrypt(aesKeyStr.getBytes(), serverPublicKey);
        //用AES秘钥加密请求内容
        byte[] encryptContentRequest = AESUtil.encryptAES(content.getBytes(), aesKey);
        JSONObject result = new JSONObject();
        String value = Base64Util.byte2Base64(encryptAesKey).replaceAll("\r\n", "");
        result.put("ak", value);
        String value1 = Base64Util.byte2Base64(encryptContentRequest).replaceAll("\r\n", "");
        result.put("ct", value1);

        LogUtils.e("---加密 AESKEY===" + aesKeyStr);
        LogUtils.e("---加密 ak===" + value);
        LogUtils.e("---加密 ct===" + value1);
        return result.toString();
    }


    /**
     * APP解密服务器的响应内容
     *
     * @param content
     * @return
     * @throws Exception
     */
    public static String appDecrypt1(String content) throws Exception {
        JSONObject result = new JSONObject(content);
        String encryptAesKeyStr = (String) result.get("ak");
        String encryptContent = (String) result.get("ct");
        //将Base64编码后的APP私钥转换成PrivateKey对象
        PrivateKey appPrivateKey = RSAUtil.string2PrivateKey(KeyUtil.APP_PRIVATE_KEY);
        //用APP私钥解密AES秘钥
        byte[] aesKeyBytes = RSAUtil.privateDecrypt(Base64Util.base642Byte(encryptAesKeyStr), appPrivateKey);
        //用AES秘钥解密请求内容
        String base64Key = new String(aesKeyBytes);
        SecretKey aesKey = AESUtil.loadKeyAES(base64Key);
        byte[] response = AESUtil.decryptAES(Base64Util.base642Byte(encryptContent), aesKey);
        String s = new String(response);
        LogUtils.e("---解密 AESKEY===" + base64Key);
        LogUtils.e("---解密 ct===" + s);
        return s;
    }


    /**
     * 服务器加密响应给APP的内容
     *
     * @param appPublicKeyStr Base64编码后的APP公钥
     * @param aesKeyStr       Base64编码后的AES秘钥
     * @param content         需要加密的内容
     * @return
     * @throws Exception
     */
    public static String serverEncrypt1(String appPublicKeyStr, String aesKeyStr, String content) throws Exception {
        //将Base64编码后的APP公钥转换成PublicKey对象
        PublicKey appPublicKey = RSAUtil.string2PublicKey(appPublicKeyStr);
        //将Base64编码后的AES秘钥转换成SecretKey对象
        SecretKey aesKey = AESUtil.loadKeyAES(aesKeyStr);
        //用APP公钥加密AES秘钥
        byte[] encryptAesKey = RSAUtil.publicEncrypt(aesKeyStr.getBytes(), appPublicKey);
        //用AES秘钥加密响应内容
        byte[] encryptContent = AESUtil.encryptAES(content.getBytes(), aesKey);

        JSONObject result = new JSONObject();
        result.put("ak", Base64Util.byte2Base64(encryptAesKey).replaceAll("\r\n", ""));
        result.put("ct", Base64Util.byte2Base64(encryptContent).replaceAll("\r\n", ""));
        return result.toString();
    }


    /**
     * 服务器解密APP的请求内容
     *
     * @param content
     * @return
     * @throws Exception
     */
    public static String serverDecrypt1(String content) throws Exception {
        JSONObject result = new JSONObject(content);
        String encryptAesKeyStr = (String) result.get("ak");
        String encryptAppPublicKeyStr = (String) result.get("apk");
        String encryptContent = (String) result.get("ct");

        //将Base64编码后的Server私钥转换成PrivateKey对象
        PrivateKey serverPrivateKey = RSAUtil.string2PrivateKey(KeyUtil.SERVER_PRIVATE_KEY);
        //用Server私钥解密AES秘钥
        byte[] aesKeyBytes = RSAUtil.privateDecrypt(Base64Util.base642Byte(encryptAesKeyStr), serverPrivateKey);
        //用Server私钥解密APP公钥
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


    //################################双向ECC + AES ##########################
    //APP加密请求内容
    public static String appEncrypt(String appPublicKeyStr, String content) throws Exception {
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
    public static String appDecrypt(String appPrivateKeyStr, String content) throws Exception {
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
    public static String serverEncrypt(String appPublicKeyStr, String aesKeyStr, String content) throws Exception {
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
    public static String serverDecrypt(String content) throws Exception {
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

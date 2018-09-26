package com.haxi.mh.utils.dense;

import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.json.JSONObject;

import javax.crypto.SecretKey;

/**
 * Created by Han on 2018/9/26
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class HttpEncryptUtil {
    //	//################################双向RSA + AES ##########################
//	//APP加密请求内容
//	public static String appEncrypt(String appPublicKeyStr, String content) throws Exception{
//		//将Base64编码后的Server公钥转换成PublicKey对象
//		PublicKey serverPublicKey = RSAUtil.string2PublicKey(KeyUtil.SERVER_PUBLIC_KEY);
//		//每次都随机生成AES秘钥
//		String aesKeyStr = AESUtil.genKeyAES();
//		SecretKey aesKey = AESUtil.loadKeyAES(aesKeyStr);
//		//用Server公钥加密AES秘钥
//		byte[] encryptAesKey = RSAUtil.publicEncrypt(aesKeyStr.getBytes(), serverPublicKey);
//		//用AES秘钥加密APP公钥
//		byte[] encryptAppPublicKey = AESUtil.encryptAES(appPublicKeyStr.getBytes(), aesKey);
//		//用AES秘钥加密请求内容
//		byte[] encryptRequest = AESUtil.encryptAES(content.getBytes(), aesKey);
//
//		JSONObject result = new JSONObject();
//		result.put("ak", RSAUtil.byte2Base64(encryptAesKey).replaceAll("\r\n", ""));
//		result.put("apk", RSAUtil.byte2Base64(encryptAppPublicKey).replaceAll("\r\n", ""));
//		result.put("ct", RSAUtil.byte2Base64(encryptRequest).replaceAll("\r\n", ""));
//		return result.toString();
//	}
//
//	//APP解密服务器的响应内容
//	public static String appDecrypt(String appPrivateKeyStr, String content) throws Exception{
//		JSONObject result = JSONObject.fromObject(content);
//		String encryptAesKeyStr = (String) result.get("ak");
//		String encryptContent = (String) result.get("ct");
//
//		//将Base64编码后的APP私钥转换成PrivateKey对象
//		PrivateKey appPrivateKey = RSAUtil.string2PrivateKey(appPrivateKeyStr);
//		//用APP私钥解密AES秘钥
//		byte[] aesKeyBytes = RSAUtil.privateDecrypt(RSAUtil.base642Byte(encryptAesKeyStr), appPrivateKey);
//		//用AES秘钥解密请求内容
//		SecretKey aesKey = AESUtil.loadKeyAES(new String(aesKeyBytes));
//		byte[] response = AESUtil.decryptAES(RSAUtil.base642Byte(encryptContent), aesKey);
//
//		return new String(response);
//	}
//
//	//服务器加密响应给APP的内容
//	public static String serverEncrypt(String appPublicKeyStr, String aesKeyStr, String content) throws Exception{
//		//将Base64编码后的APP公钥转换成PublicKey对象
//		PublicKey appPublicKey = RSAUtil.string2PublicKey(appPublicKeyStr);
//		//将Base64编码后的AES秘钥转换成SecretKey对象
//		SecretKey aesKey = AESUtil.loadKeyAES(aesKeyStr);
//		//用APP公钥加密AES秘钥
//		byte[] encryptAesKey = RSAUtil.publicEncrypt(aesKeyStr.getBytes(), appPublicKey);
//		//用AES秘钥加密响应内容
//		byte[] encryptContent = AESUtil.encryptAES(content.getBytes(), aesKey);
//
//		JSONObject result = new JSONObject();
//		result.put("ak", RSAUtil.byte2Base64(encryptAesKey).replaceAll("\r\n", ""));
//		result.put("ct", RSAUtil.byte2Base64(encryptContent).replaceAll("\r\n", ""));
//		return result.toString();
//	}
//
//	//服务器解密APP的请求内容
//	public static String serverDecrypt(String content) throws Exception{
//		JSONObject result = JSONObject.fromObject(content);
//		String encryptAesKeyStr = (String) result.get("ak");
//		String encryptAppPublicKeyStr = (String) result.get("apk");
//		String encryptContent = (String) result.get("ct");
//
//		//将Base64编码后的Server私钥转换成PrivateKey对象
//		PrivateKey serverPrivateKey = RSAUtil.string2PrivateKey(KeyUtil.SERVER_PRIVATE_KEY);
//		//用Server私钥解密AES秘钥
//		byte[] aesKeyBytes = RSAUtil.privateDecrypt(RSAUtil.base642Byte(encryptAesKeyStr), serverPrivateKey);
//		//用Server私钥解密APP公钥
//		SecretKey aesKey = AESUtil.loadKeyAES(new String(aesKeyBytes));
//		byte[] appPublicKeyBytes = AESUtil.decryptAES(RSAUtil.base642Byte(encryptAppPublicKeyStr), aesKey);
//		//用AES秘钥解密请求内容
//		byte[] request = AESUtil.decryptAES(RSAUtil.base642Byte(encryptContent), aesKey);
//
//		JSONObject result2 = new JSONObject();
//		result2.put("ak", new String(aesKeyBytes));
//		result2.put("apk", new String(appPublicKeyBytes));
//		result2.put("ct", new String(request));
//		return result2.toString();
//	}


    //################################双向ECC + AES ##########################
    //APP加密请求内容
    public static String appEncrypt(String appPublicKeyStr, String content) throws Exception {
        //将Base64编码后的Server公钥转换成PublicKey对象
        ECPublicKey serverPublicKey = ECCUtil.string2PublicKey(KeyUtil.SERVER_PUBLIC_KEY);
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
        result.put("ak", AESUtil.byte2Base64(encryptAesKey).replaceAll("\r\n", ""));
        result.put("apk", AESUtil.byte2Base64(encryptAppPublicKey).replaceAll("\r\n", ""));
        result.put("ct", AESUtil.byte2Base64(encryptRequest).replaceAll("\r\n", ""));
        return result.toString();
    }

    //APP解密服务器的响应内容
    public static String appDecrypt(String appPrivateKeyStr, String content) throws Exception {
        JSONObject result = new JSONObject(content);
        String encryptAesKeyStr = result.optString("ak");
        String encryptContent = result.optString("ct");

        //将Base64编码后的APP私钥转换成PrivateKey对象
        ECPrivateKey appPrivateKey = ECCUtil.string2PrivateKey(appPrivateKeyStr);
        //用APP私钥解密AES秘钥
        byte[] aesKeyBytes = ECCUtil.privateDecrypt(AESUtil.base642Byte(encryptAesKeyStr), appPrivateKey);
        //用AES秘钥解密请求内容
        SecretKey aesKey = AESUtil.loadKeyAES(new String(aesKeyBytes));
        byte[] response = AESUtil.decryptAES(AESUtil.base642Byte(encryptContent), aesKey);

        return new String(response);
    }

    //服务器加密响应给APP的内容
    public static String serverEncrypt(String appPublicKeyStr, String aesKeyStr, String content) throws Exception {
        //将Base64编码后的APP公钥转换成PublicKey对象
        ECPublicKey appPublicKey = ECCUtil.string2PublicKey(appPublicKeyStr);
        //将Base64编码后的AES秘钥转换成SecretKey对象
        SecretKey aesKey = AESUtil.loadKeyAES(aesKeyStr);
        //用APP公钥加密AES秘钥
        byte[] encryptAesKey = ECCUtil.publicEncrypt(aesKeyStr.getBytes(), appPublicKey);
        //用AES秘钥加密响应内容
        byte[] encryptContent = AESUtil.encryptAES(content.getBytes(), aesKey);

        JSONObject result = new JSONObject();
        result.put("ak", AESUtil.byte2Base64(encryptAesKey).replaceAll("\r\n", ""));
        result.put("ct", AESUtil.byte2Base64(encryptContent).replaceAll("\r\n", ""));
        return result.toString();
    }

    //服务器解密APP的请求内容
    public static String serverDecrypt(String content) throws Exception {
        JSONObject result = new JSONObject(content);
        String encryptAesKeyStr = result.optString("ak");
        String encryptAppPublicKeyStr = result.optString("apk");
        String encryptContent = result.optString("ct");

        //将Base64编码后的Server私钥转换成PrivateKey对象
        ECPrivateKey serverPrivateKey = ECCUtil.string2PrivateKey(KeyUtil.SERVER_PRIVATE_KEY);
        //用Server私钥解密AES秘钥
        byte[] aesKeyBytes = ECCUtil.privateDecrypt(AESUtil.base642Byte(encryptAesKeyStr), serverPrivateKey);
        //用AES秘钥解密APP公钥
        SecretKey aesKey = AESUtil.loadKeyAES(new String(aesKeyBytes));
        byte[] appPublicKeyBytes = AESUtil.decryptAES(AESUtil.base642Byte(encryptAppPublicKeyStr), aesKey);
        //用AES秘钥解密请求内容
        byte[] request = AESUtil.decryptAES(AESUtil.base642Byte(encryptContent), aesKey);

        JSONObject result2 = new JSONObject();
        result2.put("ak", new String(aesKeyBytes));
        result2.put("apk", new String(appPublicKeyBytes));
        result2.put("ct", new String(request));
        return result2.toString();
    }
}

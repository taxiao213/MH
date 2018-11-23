package com.haxi.mh.utils.dense;

import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密工具类
 * Created by Han on 2018/9/26
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class AESUtil2 {

    private static final String AES = "AES";
    private static final String STRING_FORMAT = "UTF-8";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    public static String CRYPT_KEY = getRandomString(16);
    public static String IV_STRING = getRandomString(16);

    /**
     * 加密
     *
     * @param aesKey  AESkey
     * @param ivKey   AES偏移量
     * @param content 明文
     * @return 密文
     */
    public static String encrypt(String aesKey, String ivKey, String content) {
        try {
            byte[] byteContent = content.getBytes(STRING_FORMAT);
            // 注意，为了能与 iOS 统一 这里的 key 不可以使用 KeyGenerator、SecureRandom、SecretKey 生成
            byte[] enCodeFormat = aesKey.getBytes();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, AES);
            byte[] initParam = ivKey.getBytes();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
            // 指定加密的算法、工作模式和填充方式
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encryptedBytes = cipher.doFinal(byteContent);
            // 同样对加密后数据进行 base64 编码
            return Base64Util.byte2Base64(encryptedBytes);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 解密
     *
     * @param aesKey  AESkey
     * @param ivKey   AES偏移量
     * @param content 密文
     * @return 解密后的明文
     */
    public static String decrypt(String aesKey, String ivKey, String content) {
        try {
            byte[] encryptedBytes = Base64Util.base642Byte(content);
            byte[] enCodeFormat = aesKey.getBytes();
            SecretKeySpec secretKey = new SecretKeySpec(enCodeFormat, AES);
            byte[] initParam = ivKey.getBytes();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            byte[] result = cipher.doFinal(encryptedBytes);
            return new String(result, STRING_FORMAT);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 随机生成字符串
     *
     * @param length 需要生成的位数
     * @return String
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

}

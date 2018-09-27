package com.haxi.mh.utils.dense;


import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密工具类
 * Created by Han on 2018/9/26
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class AESUtil {

    /**
     * 生成AES秘钥，然后Base64编码
     *
     * @return Base64编码
     * @throws Exception
     */
    public static String genKeyAES() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        SecretKey key = keyGen.generateKey();
        return Base64Util.byte2Base64(key.getEncoded());
    }

    /**
     * 将Base64编码后的AES秘钥转换成SecretKey对象
     *
     * @param base64Key
     * @return SecretKey对象
     * @throws Exception
     */
    public static SecretKey loadKeyAES(String base64Key) throws Exception {
        byte[] bytes = Base64Util.base642Byte(base64Key);
        return new SecretKeySpec(bytes, "AES");
    }


    /**
     * AES加密
     *
     * @param source 加密内容
     * @param key    SecretKey对象
     * @return 加密后的字节数组
     * @throws Exception
     */
    public static byte[] encryptAES(byte[] source, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(source);
    }


    /**
     * AES解密
     *
     * @param source 解密内容
     * @param key    SecretKey对象
     * @return 解密后的字节数组
     * @throws Exception
     */
    public static byte[] decryptAES(byte[] source, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(source);
    }

}

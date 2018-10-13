package com.haxi.mh.utils.dense;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * RSA加密工具类
 * EC和RSA的优缺点：
 * RSA的优点：JDK自己支持。不需要第三方库。同时支持RSA的开发库也很多（最典型的就是OpenSSL）
 * EC的缺点：需要第三方库，支持的广度比不上RSA
 * EC的优点：1.在达到相同加密程度下，EC需要的秘钥长度比RSA要短得多
 *           2.bouncycastle实现的EC加密算法，对密文长度的限制比较松。在下面的测试程序中构造了一个长字符串加密，没有报错。
 * RSA的加密则是有限制的，必须分片。不过我不知道是不是bouncycastle自己事先做了分片
 * 明文长度(bytes) = (加密长度/8 -11)
 * 片数=(明文长度(bytes)/(密钥长度(bytes)-11))的整数部分+1,就是不满一片的按一片算
 * 密文长度=密钥长度*片数
 * <p>
 * 在移动端获取解密的Cipher类时要使用Cipher.getInstance("RSA/ECB/PKCS1Padding");
 * 在后端使用Cipher.getInstance("RSA");来获取
 * <p>
 * Created by Han on 2018/9/26
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class RSAUtil {

    /**
     * 生成RSA秘钥
     *
     * @return 秘钥对
     * @throws Exception
     */
    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");//加密方式
        keyPairGenerator.initialize(2048);//加密长度
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 获取RSA公钥并转为Base64编码
     *
     * @param keyPair 秘钥对
     * @return RSA公钥
     */
    public static String getPublicKey(KeyPair keyPair) {
        PublicKey publicKey = keyPair.getPublic();
        byte[] bytes = publicKey.getEncoded();
        return Base64Util.byte2Base64(bytes);
    }


    /**
     * 获取RSA私钥并转为Base64编码
     *
     * @param keyPair 秘钥对
     * @return RSA私钥
     */
    public static String getPrivateKey(KeyPair keyPair) {
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] bytes = privateKey.getEncoded();
        return Base64Util.byte2Base64(bytes);
    }


    /**
     * 将Base64编码后的公钥转换成PublicKey对象
     *
     * @param pubStr Base64编码后的公钥
     * @return PublicKey公钥对象
     * @throws Exception
     */
    public static PublicKey string2PublicKey(String pubStr) throws Exception {
        byte[] keyBytes = Base64Util.base642Byte(pubStr);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 将Base64编码后的私钥转换成PrivateKey对象
     *
     * @param priStr Base64编码后的私钥
     * @return PrivateKey私钥对象
     * @throws Exception
     */
    public static PrivateKey string2PrivateKey(String priStr) throws Exception {
        byte[] keyBytes = Base64Util.base642Byte(priStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }


    /**
     * 公钥加密
     * 在移动端获取解密的Cipher类时要使用Cipher.getInstance("RSA/ECB/PKCS1Padding");
     * 在后端使用Cipher.getInstance("RSA");来获取
     *
     * @param content   加密的内容
     * @param publicKey PublicKey公钥对象
     * @return 加密后的字节数组
     * @throws Exception
     */
    public static byte[] publicEncrypt(byte[] content, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(content);
    }


    /**
     * 在移动端获取解密的Cipher类时要使用Cipher.getInstance("RSA/ECB/PKCS1Padding");
     * 在后端使用Cipher.getInstance("RSA");来获取
     *
     * @param content    需要解密的内容
     * @param privateKey PrivateKey私钥对象
     * @return 解密后的字节数组
     * @throws Exception
     */
    public static byte[] privateDecrypt(byte[] content, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(content);
    }

}

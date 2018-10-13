package com.haxi.mh.utils.dense;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * ECC加密工具类
 * EC和RSA的优缺点：
 * RSA的优点：JDK自己支持。不需要第三方库。同时支持RSA的开发库也很多（最典型的就是OpenSSL）
 * EC的缺点：需要第三方库，支持的广度比不上RSA
 * EC的优点：1.在达到相同加密程度下，EC需要的秘钥长度比RSA要短得多
 *           2.bouncycastle实现的EC加密算法，对密文长度的限制比较松。在下面的测试程序中构造了一个长字符串加密，没有报错。
 * RSA的加密则是有限制的，必须分片。不过我不知道是不是bouncycastle自己事先做了分片
 * 在Android客户端无法使用
 * Created by Han on 2018/9/26
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class ECCUtil {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 生成ECC秘钥对
     *
     * @return 秘钥对
     * @throws Exception
     */
    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", "BC");
        keyPairGenerator.initialize(256, new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }


    /**
     * 获取ECC公钥(Base64编码)
     *
     * @param keyPair 秘钥对
     * @return
     */
    public static String getPublicKey(KeyPair keyPair) {
        PublicKey publicKey = keyPair.getPublic();
        byte[] bytes = publicKey.getEncoded();
        return Base64Util.byte2Base64(bytes);
    }


    /**
     * 获取ECC私钥(Base64编码)
     *
     * @param keyPair 秘钥对
     * @return
     */
    public static String getPrivateKey(KeyPair keyPair) {
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] bytes = privateKey.getEncoded();
        return Base64Util.byte2Base64(bytes);
    }


    /**
     * 将Base64编码后的公钥转换成ECPublicKey对象
     *
     * @param pubStr Base64编码后的公钥
     * @return ECPublicKey对象
     * @throws Exception
     */
    public static PublicKey string2PublicKey(String pubStr) throws Exception {
        byte[] keyBytes = Base64Util.base642Byte(pubStr);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }


    /**
     * 将Base64编码后的私钥转换成ECPrivateKey对象
     *
     * @param priStr Base64编码后的私钥
     * @return ECPrivateKey对象
     * @throws Exception
     */
    public static PrivateKey string2PrivateKey(String priStr) throws Exception {
        byte[] keyBytes = Base64Util.base642Byte(priStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }


    /**
     * ECC公钥加密
     *
     * @param content   内容
     * @param publicKey PublicKey对象
     * @return 字节数组
     * @throws Exception
     */
    public static byte[] publicEncrypt(byte[] content, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("ECIES", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bytes = cipher.doFinal(content);
        return bytes;
    }


    /**
     * ECC私钥解密
     *
     * @param content    内容
     * @param privateKey PrivateKey对象
     * @return 字节数组
     * @throws Exception
     */
    public static byte[] privateDecrypt(byte[] content, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("ECIES", "BC");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] bytes = cipher.doFinal(content);
        return bytes;
    }

}

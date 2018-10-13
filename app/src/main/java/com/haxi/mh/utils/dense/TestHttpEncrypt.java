package com.haxi.mh.utils.dense;

import com.google.gson.Gson;
import com.haxi.mh.utils.model.LogUtils;

import org.json.JSONObject;

import java.security.KeyPair;
import java.util.Map;

/**
 * 测试类
 * Created by Han on 2018/9/26
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class TestHttpEncrypt {

    public TestHttpEncrypt() {

    }


    /**
     * 生成RSA公钥和私钥，并Base64编码
     *
     * @throws Exception
     */
    public void generateKeyPair() throws Exception {
        KeyPair keyPair = RSAUtil.getKeyPair();
        String publicKeyStr = RSAUtil.getPublicKey(keyPair);
        String privateKeyStr = RSAUtil.getPrivateKey(keyPair);
        LogUtils.e("RSA公钥Base64编码:" + publicKeyStr);
        LogUtils.e("RSA私钥Base64编码:" + privateKeyStr);
    }


    //################################双向RSA + AES ##########################

    /**
     * 测试  APP加密请求内容  解密内容
     *
     * @throws Exception
     */
    public void appTest() throws Exception {
        String action = "demo/demo";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "name");
        jsonObject.put("age", 11);
        Map<String, String> encryptMap = HttpEncryptUtil.appEncrypt(action, jsonObject.toString());
        String decrypt1 = HttpEncryptUtil.appDecrypt(new Gson().toJson(encryptMap));
        LogUtils.e("---解密数据---" + decrypt1);

    }


}

package com.haxi.mh.utils.dense;

import com.haxi.mh.utils.model.LogUtils;

import java.security.KeyPair;

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
     * @throws Exception
     */
    public void testGenerateKeyPair() throws Exception {
        KeyPair keyPair = RSAUtil.getKeyPair();
        String publicKeyStr = RSAUtil.getPublicKey(keyPair);
        String privateKeyStr = RSAUtil.getPrivateKey(keyPair);
        LogUtils.e("RSA公钥Base64编码:" + publicKeyStr);
        LogUtils.e("RSA私钥Base64编码:" + privateKeyStr);
    }


    /**
     * 生成AES秘钥，并Base64编码
     * @throws Exception
     */
    public void testGenerateAesKey() throws Exception {
        String base64Str = AESUtil.genKeyAES();
        LogUtils.e("AES秘钥Base64编码:" + base64Str);
    }



    //################################双向RSA + AES ##########################

    /**
     * 测试  APP加密请求内容  APP端公钥和私钥从配置文件读取，不能写死在代码里
     * @throws Exception
     */
    public void testAppEncrypt1() throws Exception {
        String serverPublicKey = KeyUtil.SERVER_PUBLIC_KEY.replaceAll("\r\n", "");
        //请求的实际内容
        String content = "{\"tenantid\":\"1\", \"account\":\"13015929018\", \"pwd\":\"123456\"}";
        String result = HttpEncryptUtil.appEncrypt1(serverPublicKey, content);
        LogUtils.e(result);
    }


    /**
     * 测试  服务器解密APP的请求内容
     * @throws Exception
     */
    public void testServerDecrypt1() throws Exception {
        String result = "{\"ak\":\"QPLzRIRRbXizn7C8Zw+9oaZf7LlzfWLsmTKD162KdmymdpR1me+E7i+99U+0Ss+thsT2JIm5apkvMCceSpPyvYOdYSSfmeIeSYNrNiOrAQJSCW7uCj1EVJBNKfg0vcpbVz9fg4nktyPizadCDeeKY5XhrtyZrre2S9RV9KdmQj251ZE9G0odkHPLgrZd1hb8w8Lrt8+jaiX6yD2LWiVkKd9O2MqBOv0kOLlr8BJv5WJC56sUqIQEe6\\/iD559Llen4Kcg0pPtdtgrJSYM523dFgjJK8UXu\\/9cBJy2HtXLC63GO2rDpSIhTCvuTB0exeSded+qSChCVQkb0qYsX9XxUQ==\",\"apk\":\"y1ab2sKeslpuVxOjJtfOudwcGboSaccV+w85dF701Y+U6KSSxkQTpakjIOG1GPZQoD7DgjBLsBXq9v26h\\/ENfB8tlHQNd4OQebc+T3hUg4Wmt11R8QjlWs8FrMDugNtKc\\/sroOVc6yJ4JqrmuMV3JKD1sRchOIO3NHRip9cMdUSs9CD3QiemoNeZr2wnI1DuRyc4ePjU+QdAJPI4JcVvGFMq5GC6juPkbcyqrAJUT8BxTfmYlx1ChN6Q\\/3U\\/jn8VSlIehGdG21ylZj67ucqHEEEbZ0zLyk5epJo2BfoSaR844SN4Hss\\/Q6UB0pXxd\\/N0pSq6f+l6JuUAgw6rYKI9QupQYXeSCNBSQKj483CsLn6EUzWKgaViTlmjQ5+r1JqRStnM5R1WTQbg5ydsmt4fUy+4rnvZm1XvOjuSDtvMAVAFPX6HXRVvRdHKhzMj0ln7U7uJA0mR71vqKPeYSXrMyPWL4urDZJtmro9l6ZffFoAT+n3pbUc468OUCn0\\/C6sVZDvaS7Xw+30LvR9WK0HPSw==\",\"ct\":\"oadKf20olI0DfmedLo9hV935NASSKtrJSK17fHxyh\\/1u2pgg59V1u795BSiXcRdpt720EFoi3JUgd96OweVj0A==\"}";
        LogUtils.e(HttpEncryptUtil.serverDecrypt1(result));
    }


    /**
     * 测试 服务器加密响应给APP的内容
     * @throws Exception
     */
    public void testserverEncrypt1() throws Exception {
        String appPublicKeyStr = KeyUtil.SERVER_PUBLIC_KEY.replaceAll("\r\n", "");
        String aesKeyStr = "dSRWXM6IkWkKk7I/ZGouqA==";
        String content = "{\"retcode\":\"200\"}";
        LogUtils.e(HttpEncryptUtil.serverEncrypt1(appPublicKeyStr, aesKeyStr, content));
    }


    /**
     * 测试 	APP解密服务器的响应内容  APP端公钥和私钥从配置文件读取，不能写死在代码里
     * @throws Exception
     */
    public void testAppDecrypt1() throws Exception {
        String serverPrivateKey = KeyUtil.SERVER_PRIVATE_KEY.replaceAll("\r\n", "");
        String content = "{\"ak\":\"mRBa005mea+6QIaFhTHrfCTBBFL+sy1uHI1iSN6LUK5/VQK/Bt9JZ+5/e2TQYMiD8U6KXBzZgHOl4RL8AErno9K4bbC+4Ke5Bl/IIGZ6kPJB4OjzbqBwxmmA+zJrcS3TlzIsVGpuIzGMQzIT0rlJl+BsQj6N9F3jfCeXBXH+JoTPEaTZqzQ9odgfPooP8jvuBOneqAiTmIgNzcVJwr7EB1tB65FjYPWFJqC0xrmLlrvev0KrD/XnKkzL1wGHc/eXeYzRXHuz4tbTHQV0mrZNz+tITXPVorRb0Tl0mglUafiqTkUBsXUv4abUvz2JImlF1nSAmQfKWfMNd7Fwag480g==\",\"ct\":\"DPMIYZaJL5e7Jvs2Vsy6jgnEPWBYFgjb1K1yf7gcWUCVyAfBPkLGK93onQkvLl8urp2yTwEsxzP6o1om0mqjkEU4oPpYf4NJC+QPQRQ2YTo=\"}";
        LogUtils.e(HttpEncryptUtil.appDecrypt1(serverPrivateKey, content));
    }



    //################################双向ECC + AES ##########################

    /**
     * 测试  APP加密请求内容
     * @throws Exception
     */
    public void testAppEncrypt() throws Exception {
        //APP端公钥和私钥从配置文件读取，不能写死在代码里
        String serverPublicKey = KeyUtil.SERVER_PUBLIC_KEY.replaceAll("\r\n", "");
        //请求的实际内容
        String content = "{\"tenantid\":\"1\", \"account\":\"13015929018\", \"pwd\":\"123456\"}";
        String result = HttpEncryptUtil.appEncrypt(serverPublicKey, content);
        LogUtils.e(result);
    }

    /**
     * 测试  服务器解密APP的请求内容
     * @throws Exception
     */
    public void testServerDecrypt() throws Exception {
        String result = "{\"ak\":\"iLHfi1XRz4gnirU2OKggNCkz5x0i6aSonm1u3bE+ncI4AuiUG9LX2nbrQV/lWUIqwRp/q/P+SrIPnh5JbgEzSi+K46N4enyDFYbWpC6gONqQpF3tNt6Q1Y+UdX3L5l9hFPAS9tIhI2kT10AbhMox2kKOhr6ZQmmC/A3qeFEbTuUUf8bOCr4nqz4qSNyCZgcJdoAQonJeN8IilWuTD+LpbllNimFNR/sGY5jlyjvVydrdpNs15oFaXtfTLUjSXe2e5Ha1r3K7lP93C2E+KL55001xFJhQZcZXa9ZlYCMQgI+2cJlED4uA3bl2ul1dtnvXK+41Yky9e9QrRDc5luqB6w==\",\"apk\":\"P0SJaTzKWuBMi/fj2G8wwZ9+FWFIrE3BAwdoXwIfiTxptYXumLxnMpZZkCBNqQBvhvSzAEPyA3c9kCjhYCxdTnV61N+T/DZM+B62u4vqCy1MsFZT06BJjrNFW29AfSRNmQdKhJEyDPARcf5FerULbIDWGvrHzHys7jVbicjlYWtQpnyQf5Wl0Bd7taEqSwUSKejoEsN74frwlk8Hu4KP4bLvVy9S7DjOP2juXbVkHYaKgVmhM2V3yElVOEb1TDCLSFMNtug74+7itlzlChDR8wEWdh11vQcp69iGmDXMo2vcJ9tO1YZP+hCYZvujHMRwAzHtkqafEoSJsvSN8PWS+qmQdUX2frf6A0cl6SGnTbGUUEV/w0rBIU/oGhP8cl8+ghqPbp7HzvwXFOJsUciy+7tsLRrdDpLeOcz+fh/c0RSpCKNEZtRmcuUqBQ+3tZKYGPhl+StsFh3s1RCkhI4EsSD95bCbES4r1r1E4dytdELi0ebJug7Quk3rwFVXGX9o4wrnnvcbTaSyyAAg2YTNfA==\",\"ct\":\"mkC7hE/crHbmW+h2OCMBANCA64xtMFLTRmLahOU+UysZrXzK30qRj6RUcvpQz4mJ6EOYYAK34+BQBkN9gapdIw==\"}";
        LogUtils.e(HttpEncryptUtil.serverDecrypt(result));
    }


    /**
     * 测试 服务器加密响应给APP的内容
     * @throws Exception
     */
    public void testserverEncrypt() throws Exception {
        String aesKeyStr = "dSRWXM6IkWkKk7I/ZGouqA==";
        String appPublicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqqKPH/L0AZyn1fJ9xK2ol2nHY5jPu8qw7COwFukkRdr2j0oNJmD8vCTmxgzKWV0CkihiJ7Y0OekrGc78JL5tpL2SqeZTLa2bCJZJaTM3KFOXYb82nc8Xbr2caDnf7mgjyt0AALHG/YfYwd7hifZRB6Ct89uBTn6W5x/7oxGT6D1C8siXKV+99AZPMv2HobglWyquyjIL5TZOhYmCMzFUPMOiXzzGYXMZj2gmfUFXMf/2jitMPGg3zQPJxPSYunjoE1fMInk1obEhEfU8n2YxT5ZbGMWZGjt4hZwF+FJJLV+WOantfUJ4rMBB8qxgQtkT+VzddfLCEoyy4Rl50fvjzwIDAQAB";
        String content = "{\"retcode\":\"200\"}";
        LogUtils.e(HttpEncryptUtil.serverEncrypt(appPublicKeyStr, aesKeyStr, content));
    }


    /**
     * 测试 	APP解密服务器的响应内容 APP端公钥和私钥从配置文件读取，不能写死在代码里
     * @throws Exception
     */
    public void testAppDecrypt() throws Exception {
        String serverPrivateKey = KeyUtil.SERVER_PRIVATE_KEY.replaceAll("\r\n", "");
        String content = "{\"ak\":\"mRBa005mea+6QIaFhTHrfCTBBFL+sy1uHI1iSN6LUK5/VQK/Bt9JZ+5/e2TQYMiD8U6KXBzZgHOl4RL8AErno9K4bbC+4Ke5Bl/IIGZ6kPJB4OjzbqBwxmmA+zJrcS3TlzIsVGpuIzGMQzIT0rlJl+BsQj6N9F3jfCeXBXH+JoTPEaTZqzQ9odgfPooP8jvuBOneqAiTmIgNzcVJwr7EB1tB65FjYPWFJqC0xrmLlrvev0KrD/XnKkzL1wGHc/eXeYzRXHuz4tbTHQV0mrZNz+tITXPVorRb0Tl0mglUafiqTkUBsXUv4abUvz2JImlF1nSAmQfKWfMNd7Fwag480g==\",\"ct\":\"DPMIYZaJL5e7Jvs2Vsy6jgnEPWBYFgjb1K1yf7gcWUCVyAfBPkLGK93onQkvLl8urp2yTwEsxzP6o1om0mqjkEU4oPpYf4NJC+QPQRQ2YTo=\"}";
        LogUtils.e(HttpEncryptUtil.appDecrypt(serverPrivateKey, content));
    }


}

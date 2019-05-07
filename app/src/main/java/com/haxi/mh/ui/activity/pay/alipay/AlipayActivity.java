package com.haxi.mh.ui.activity.pay.alipay;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.haxi.mh.R;
import com.haxi.mh.base.BaseActivity;
import com.haxi.mh.mvp.base.IBaseView;

import java.util.Map;

import butterknife.OnClick;

/**
 * 支付宝支付
 * Created by Han on 2018/8/25
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class AlipayActivity extends BaseActivity {

    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "201808246110735";

    /**
     * 支付宝账户登录授权业务：入参pid值
     */
    public static final String PID = "2088402919508714";
    /**
     * 支付宝账户登录授权业务：入参target_id值
     */
    public static final String TARGET_ID = "";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /**
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA2_PRIVATE = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC6L3i84qAf4jJ0\n" +
            "6mljYQwD5ogweHlfjVJ+qU57YUAKmEEpwkT5OaMBzuTCmeSMY8zdCvBBLisrniJY\n" +
            "8+ibc1oyElhNpYnc2m7UIwVIpxUWWWJRtXek5ZIrgI7X2zt6q+kwnilftIkd6WHs\n" +
            "t1pN+c8C6nI20lf6gUaheY/yOEwAI97hFHx6M1NkEbIAGQMtn5XX7qxp6dscbtC9\n" +
            "CSmgov+G3mXiJNSoesBUeWnZa5GegNgzmhj9DnRzd7ybEJdp6PYfaXdH3UOW6BoI\n" +
            "6dbk/7McOUv6E0ByC6y6+TPhVGwepBKTvWRGBuexkWrFIZKLTACKanGXPYNPfhsM\n" +
            "wF16v2qfAgMBAAECggEAZganihJCHtasQadG0x+WHvkQPkd2c2cVArWyfSfgmS2v\n" +
            "37tGYrAz5ETE1OLR/CCgXayl1YIARCmmtsoJ+E1w6qlk3D/PWUgVnYgHWDW+931J\n" +
            "xG7FvoXS4KXVIPJRGLrl0S+Kiph63vpKIBallL9EsltY9TO05GA9HTVpLRmuhBA0\n" +
            "sR6aKZdhiLYNUStTOAtmyWHp8kDzoolMzmDft0t/n0VppcADry1m5t2TygdZNfy3\n" +
            "kefOHmMYmS19waTEpF8cqbIN+SBdMLBOWhJj/bmg+eojyiUchQxTX6ktX831JE2u\n" +
            "biGV9TCESC51THtCkYqeJL2NxObAaDDfnYJkssVngQKBgQDgSqqVclLnz9HDMr+w\n" +
            "PQsEcRlNfbtPvjoYMov5+9Mp9NNeeu/ypslxF0K3P44t4073lfUMNzdfj65ygHex\n" +
            "GjTkkZnRVy3FOgrTndn8WI2zFTGSp3jyLRs+94VBgkbrpofU8X87gVjPpM5jLVgW\n" +
            "B3fdnUuFDl33PyAFwjcYjnOY3wKBgQDUgbQp2bWrsqXeK49BFKUzGGcLDKNLCdQJ\n" +
            "agzbagXeHnbGOkPKz0Qhn3tEgbS02W03g09aU+gKPsdk0BS8/MRVzyT0lCZxIdaS\n" +
            "+yJ1TdNYySB5wdr9nGtTT3FCVk3MwToYnEpgaBReM7SvG/pkwCA9pdjBj9HUeytd\n" +
            "zhUeXVumQQKBgAmEgZqX4Fa/5dnPpHy5Cmek2PsF09X9b3+pXXE9M2e7YODIfLgi\n" +
            "RbgL0aPvCXx/AdHVxjE/gpUGhtOLCgk04Hu2hf1xIpsU7HKwZxijNVgdFR5xBtvX\n" +
            "+CkC3d+6xIGpbmgl201OHtOo3a0ttmgw5vA7hYHe6zPbtOMpq6baHKOnAoGAeXS8\n" +
            "VSMSW1TaylVZJhg3pzFuS3lopw2x+8N6H7nEKsR8nCVW88ZsqU7udCQx9R9D9fN9\n" +
            "2iGUwK5K5kqrX6yURnsDIyq9ofF2I0GX2zWk8tzZS93JXSQWj2IbWI/dHYbevowt\n" +
            "UlaKNaEVxvOVtBm/E50DevXJNslC6vVLSrSPGkECgYBYfTHO+gmqTJZx+LhK+Nn8\n" +
            "5vhqOqXKPn83t8EOKgpjmJS3XNQ0shzeKOq5f2wAmNzVbJp3WbbrCeUL83sD5tkf\n" +
            "GB14SUMZZUhfZVzNIYTtViZHvmPORacLf52tQP+kW+nIvCDlmKYBIZFoPUN1LFxd\n" +
            "Gy3IrK1oaLZPQZHdPDcaHw==";

    public static final String RSA_PRIVATE = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8A" +
            "MIIBCgKCAQEAv1Wvd6CWJq6i2p2gYpBpB4lyPcfPKRzmMZUYqlkgIz2Yi5WyVqEbvf" +
            "UhZOk4bRQFlhDWydgseq5563ziJ98a3yL1CsTVuj025yKJRi0KzrmALwtaVsUChNwom" +
            "8Z/bRK+ZNuFJ6OVpFQA6Giy7FDKto8IkQ366MY5Gck9qBWJ5u3UVTl0CRfcjVzBgyVB" +
            "icumHd0oJiQil5559AXLNqdJCXwyJYIblCumOvyjyXj09u+4d5wl/o/WhDscRakBRIiW" +
            "SnKjdXctorHvXoh9KuDlG/fV9cj0rwnXgMoyE90eadh7Rc9LY1vYpwcz4sMbWqCLCJRrjQbo76kOVfNLNF8COQIDAQAB";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(mActivity, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(mActivity, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(mActivity,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(mActivity,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_alipay;
    }

    @Override
    protected void getData() {

    }


    @OnClick({R.id.tv_alipay, R.id.tv_auth_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_alipay:
                payV2();
                break;
            case R.id.tv_auth_login:
                authV2();
                break;
        }
    }

    /**
     * 支付宝支付业务
     */
    public void payV2() {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(mActivity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 支付宝账户授权业务
     *
     */
    public void authV2() {
        if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID)
                || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))
                || TextUtils.isEmpty(TARGET_ID)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER |APP_ID| RSA_PRIVATE| TARGET_ID")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * authInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
        final String authInfo = info + "&" + sign;
        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(mActivity);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);

                Message msg = new Message();
                msg.what = SDK_AUTH_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }
}

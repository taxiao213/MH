package com.haxi.mh.ui.activity.pay.ylpay;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.haxi.mh.R;
import com.haxi.mh.base.BaseActivity;
import com.unionpay.UPPayAssistEx;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import butterknife.OnClick;

import static com.haxi.mh.ui.activity.pay.ylpay.RSAUtil.verify;

/**
 * 银联支付
 * Created by Han on 2018/8/25
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class YlPayActivity extends BaseActivity {

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }

            String tn = "";
            if (msg.obj == null || ((String) msg.obj).length() == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(YlPayActivity.this);
                builder.setTitle("错误提示");
                builder.setMessage("网络连接失败,请重试!");
                builder.setNegativeButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
            } else {
                tn = (String) msg.obj;
                /*************************************************
                 * 步骤2：通过银联工具类启动支付插件
                 ************************************************/
                doStartUnionPayPlugin(tn, mMode);
            }

            super.handleMessage(msg);
        }
    };
    private ProgressDialog mLoadingDialog = null;

    public static final int PLUGIN_VALID = 0;
    public static final int PLUGIN_NOT_INSTALLED = -1;
    public static final int PLUGIN_NEED_UPGRADE = 2;

    /*****************************************************************
     * mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
     *****************************************************************/
    private final String mMode = "01";
    private static final String TN_URL_01 = "http://101.231.204.84:8091/sim/getacptn";
//    private static final String TN_URL_01 = "http://192.168.0.42:8081/ACPSample_WuTiaoZhuan/getacptn";


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_ylpay;
    }

    @Override
    protected void getData() {

    }

    @OnClick(R.id.tv_ylpay)
    public void onViewClicked() {
        mLoadingDialog = ProgressDialog.show(mActivity, // context
                "", // title
                "正在努力的获取tn中,请稍候...", // message
                true); // 进度是否是不确定的，这只和创建进度条有关

        /*************************************************
         * 步骤1：从网络开始,获取交易流水号即TN
         ************************************************/

        new Thread() {
            @Override
            public void run() {
                String tn = null;
                InputStream is;
                try {
                    String url = TN_URL_01;
                    URL myURL = new URL(url);
                    URLConnection ucon = myURL.openConnection();
                    ucon.setConnectTimeout(120000);
                    is = ucon.getInputStream();
                    int i = -1;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    while ((i = is.read()) != -1) {
                        baos.write(i);
                    }

                    tn = baos.toString();
                    is.close();
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Message msg = mHandler.obtainMessage();
                msg.obj = tn;
                mHandler.sendMessage(msg);
            }
        }.start();

    }


    private void doStartUnionPayPlugin(String tn, String mode) {
        // mMode参数解释：
        // 0 - 启动银联正式环境
        // 1 - 连接银联测试环境
        int ret = UPPayAssistEx.startPay(this, null, null, tn, mode);
        if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
            // 需要重新安装控件
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("完成购买需要安装银联支付控件，是否安装？");

            builder.setNegativeButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UPPayAssistEx.installUPPayPlugin(YlPayActivity.this);
                            dialog.dismiss();
                        }
                    });

            builder.setPositiveButton("取消",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*************************************************
         * 步骤3：处理银联手机支付控件返回的支付结果
         ************************************************/
        if (data == null) {
            return;
        }

        String msg = "";
        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
        String str = data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase("success")) {

            // 如果想对结果数据验签，可使用下面这段代码，但建议不验签，直接去商户后台查询交易结果
            // result_data结构见c）result_data参数说明
            if (data.hasExtra("result_data")) {
                String result = data.getExtras().getString("result_data");
                try {
                    JSONObject resultJson = new JSONObject(result);
                    String sign = resultJson.getString("sign");
                    String dataOrg = resultJson.getString("data");
                    // 此处的verify建议送去商户后台做验签
                    // 如要放在手机端验，则代码必须支持更新证书
                    boolean ret = verify(dataOrg, sign, mMode);
                    if (ret) {
                        // 验签成功，显示支付结果
                        msg = "支付成功！";
                    } else {
                        // 验签失败
                        msg = "支付失败！";
                    }
                } catch (JSONException e) {
                }
            }
            // 结果result_data为成功时，去商户后台查询一下再展示成功
            msg = "支付成功！";
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("支付结果通知");
        builder.setMessage(msg);
        builder.setInverseBackgroundForced(true);
        // builder.setCustomTitle();
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}

package com.haxi.mh.ui.activity.pay.wxpay;

import android.widget.TextView;
import android.widget.Toast;

import com.haxi.mh.R;
import com.haxi.mh.mvp.base.BaseActivityM;
import com.haxi.mh.mvp.view.IWxView;
import com.haxi.mh.utils.model.LogUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 微信支付
 * Created by Han on 2018/8/28
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class Wxpay2Activity extends BaseActivityM implements IWxView {

    @BindView(R.id.tv_content)
    TextView tvContent;

    // APP_ID 替换为你的应用从官方网站申请到的合法appId
    public  String APP_ID = "wxb4ba3c02aa476ea1";
    private IWXAPI wxapi;

    @Override
    protected int getLayout() {
        return R.layout.activity_wx2;
    }

    @Override
    protected void initView() {
        wxapi = WXAPIFactory.createWXAPI(mActivity, APP_ID, false);
        wxapi.registerApp(APP_ID);
    }

    @Override
    protected void getData() {
        loadingSuccess();
    }

    @OnClick(R.id.tv_content)
    public void onViewClicked() {
        String url = "https://wxpay.wxutil.com/pub_v2/app/app_pay.php";

        Toast.makeText(mActivity, "获取订单中...", Toast.LENGTH_SHORT).show();
        try{
            byte[] buf = Util.httpGet(url);
            if (buf != null && buf.length > 0) {
                String content = new String(buf);
                LogUtils.e("get server pay params:",content);
                JSONObject json = new JSONObject(content);
                if(null != json && !json.has("retcode") ){
                    PayReq req = new PayReq();
                    //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                    req.appId			= json.getString("appid");
                    req.partnerId		= json.getString("partnerid");
                    req.prepayId		= json.getString("prepayid");
                    req.nonceStr		= json.getString("noncestr");
                    req.timeStamp		= json.getString("timestamp");
                    req.packageValue	= json.getString("package");
                    req.sign			= json.getString("sign");
                    req.extData			= "app data"; // optional
                    Toast.makeText(mActivity, "正常调起支付", Toast.LENGTH_SHORT).show();
                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                    wxapi.sendReq(req);
                }else{
                    LogUtils.e("PAY_GET", "返回错误"+json.getString("retmsg"));
                    Toast.makeText(mActivity, "返回错误"+json.getString("retmsg"), Toast.LENGTH_SHORT).show();
                }
            }else{
                LogUtils.e("PAY_GET", "服务器请求错误");
                Toast.makeText(mActivity, "服务器请求错误", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            LogUtils.e("PAY_GET", "异常："+e.getMessage());
            Toast.makeText(mActivity, "异常："+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void loadingData() {
    }

    @Override
    public void loading() {
        load();
    }

    @Override
    public void loadingSuccess() {
        success();
    }

    @Override
    public void loadingError() {
        error();
    }


    @Override
    public void setText(Long log) {
        tvContent.setText(String.valueOf(log));
    }

    @Override
    public void setAdapter(Object object) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

package com.haxi.mh.utils.zxingutil;

import android.content.Intent;

import com.haxi.mh.R;
import com.haxi.mh.base.BaseActivity;
import com.haxi.mh.constant.HConstants;
import com.haxi.mh.utils.dbutil.DBMsgTipUtil;
import com.haxi.mh.utils.zxingutil.android.CaptureActivity;
import com.haxi.mh.utils.zxingutil.bean.ZxingConfig;
import com.haxi.mh.utils.zxingutil.common.Constant;

/**
 * 扫一扫工具类
 * Create by Han on 2018/12/7
 */
public class ZxingUtils {
    private static ZxingUtils zxingUtils;

    private ZxingUtils() {
    }

    public static ZxingUtils getInstance() {
        if (zxingUtils == null) {
            synchronized (DBMsgTipUtil.class) {
                if (zxingUtils == null) {
                    zxingUtils = new ZxingUtils();
                }
            }
        }
        return zxingUtils;
    }

    /**
     * 打开扫一扫
     *
     * @param mActivity 上下文
     */
    public void openQRScan(BaseActivity mActivity) {
        Intent intent = new Intent(mActivity, CaptureActivity.class);
        /* ZxingConfig是配置类
         *可以设置是否显示底部布局，闪光灯，相册，
         * 是否播放提示音  震动
         * 设置扫描框颜色等
         * 也可以不传这个参数
         * */
        ZxingConfig config = new ZxingConfig();
        config.setPlayBeep(true);//是否播放扫描声音 默认为true
        config.setShake(true);//是否震动  默认为true
        config.setDecodeBarCode(true);//是否扫描条形码 默认为true
        config.setReactColor(R.color.colorAccent);//设置扫描框四个角的颜色 默认为白色
        config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色 默认无色
        config.setScanLineColor(R.color.colorAccent);//设置扫描线的颜色 默认白色
        config.setFullScreenScan(true);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描 true解决短距离无法识别
        config.setShowAlbum(false);//是否显示相册  默认为true  设为false则不显示
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        mActivity.startActivityForResult(intent, HConstants.REQUEST_CODE_SCAN);
    }
}

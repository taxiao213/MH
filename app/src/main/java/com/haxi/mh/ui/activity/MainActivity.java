package com.haxi.mh.ui.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.haxi.mh.MyApplication;
import com.haxi.mh.R;
import com.haxi.mh.base.BaseActivity;
import com.haxi.mh.constant.Constant;
import com.haxi.mh.service.PlayMusicService;
import com.haxi.mh.ui.fragment.HomeCreateTaskFragment;
import com.haxi.mh.ui.fragment.HomeManageFragment;
import com.haxi.mh.ui.fragment.HomePageFragment;
import com.haxi.mh.ui.fragment.HomePeopleFragment;
import com.haxi.mh.utils.background.BackgroundUtils;
import com.haxi.mh.utils.model.LogUtils;
import com.haxi.mh.utils.net.DownFileService;
import com.haxi.mh.utils.ui.UIUtil;
import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiAvailability;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.client.PendingResult;
import com.huawei.hms.support.api.client.ResultCallback;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.huawei.hms.support.api.push.TokenResult;
import com.xiaomi.mipush.sdk.MiPushClient;

import butterknife.BindView;

import static com.haxi.mh.constant.Constant.REQUEST_HMS_RESOLVE_ERROR;
import static com.haxi.mh.constant.Constant.TAG_FRAGMENT1;
import static com.haxi.mh.constant.Constant.TAG_FRAGMENT2;
import static com.haxi.mh.constant.Constant.TAG_FRAGMENT3;
import static com.haxi.mh.constant.Constant.TAG_FRAGMENT4;

/**
 * 首页
 * Created by Han on 2017/12/13
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {


    @BindView(R.id.fl)
    FrameLayout fl;
    @BindView(R.id.rb_homepage)
    RadioButton rbHomepage;
    @BindView(R.id.rb_homecreatetask)
    RadioButton rbHomecreatetask;
    @BindView(R.id.rb_homemanage)
    RadioButton rbHomemanage;
    @BindView(R.id.rb_homepeople)
    RadioButton rbHomepeople;
    @BindView(R.id.rg)
    RadioGroup rg;
    private String current_tag = null;
    private PopupWindow window;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void getData() {
        rg.setOnCheckedChangeListener(this);
        switchFragment(TAG_FRAGMENT1);
        startService();
        /* 初始化华为push */
        initHuaWeiPush();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MiPushClient.clearNotification(mActivity);
        NotificationManager manager = (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.cancelAll();
        }

        startService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        startService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        startService();
    }

    /**
     * 开启服务
     */
    private void startService() {
        if (!UIUtil.isServiceRunning(mActivity, PlayMusicService.class.getName())) {
            mActivity.startService(new Intent(mActivity, PlayMusicService.class));
            LogUtils.e("mh-->>MainActivity--->>startService()开启");
        }
    }


    /**
     * 切换fragment
     *
     * @param tab
     */
    private void switchFragment(String tab) {
        switch (tab) {
            case TAG_FRAGMENT1:
                rg.check(R.id.rb_homepage);
                break;
            case TAG_FRAGMENT2:
                rg.check(R.id.rb_homecreatetask);
                break;
            case TAG_FRAGMENT3:
                rg.check(R.id.rb_homemanage);
                break;
            case TAG_FRAGMENT4:
                rg.check(R.id.rb_homepeople);
                break;
        }

    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment1 = fragmentManager.findFragmentByTag(TAG_FRAGMENT1);
        Fragment fragment2 = fragmentManager.findFragmentByTag(TAG_FRAGMENT2);
        Fragment fragment3 = fragmentManager.findFragmentByTag(TAG_FRAGMENT3);
        Fragment fragment4 = fragmentManager.findFragmentByTag(TAG_FRAGMENT4);
        if (fragment1 != null) {
            transaction.hide(fragment1);
        }
        if (fragment2 != null) {
            transaction.hide(fragment2);
        }
        if (fragment3 != null) {
            transaction.hide(fragment3);
        }
        if (fragment4 != null) {
            transaction.hide(fragment4);
        }

        switch (i) {
            case R.id.rb_homepage:
                if (fragment1 == null) {
                    fragment1 = new HomePageFragment();
                    transaction.add(R.id.fl, fragment1, TAG_FRAGMENT1);
                } else {
                    transaction.show(fragment1);
                }
                current_tag = TAG_FRAGMENT1;
                break;
            case R.id.rb_homecreatetask:
                if (fragment2 == null) {
                    fragment2 = new HomeCreateTaskFragment();
                    transaction.add(R.id.fl, fragment2, TAG_FRAGMENT2);
                } else {
                    transaction.show(fragment2);
                }
                current_tag = TAG_FRAGMENT2;
                break;
            case R.id.rb_homemanage:
                if (fragment3 == null) {
                    fragment3 = new HomeManageFragment();
                    transaction.add(R.id.fl, fragment3, TAG_FRAGMENT3);
                } else {
                    transaction.show(fragment3);
                }
                current_tag = TAG_FRAGMENT3;
                break;
            case R.id.rb_homepeople:
                if (fragment4 == null) {
                    fragment4 = new HomePeopleFragment();
                    transaction.add(R.id.fl, fragment4, TAG_FRAGMENT4);
                } else {
                    transaction.show(fragment4);
                }
                current_tag = TAG_FRAGMENT4;
                break;
        }
        //transaction.commitNow();2018.07.17修改
        transaction.commitNowAllowingStateLoss();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (current_tag != null) {
            outState.putString("current_tag", current_tag);
        }
        LogUtils.e("onSaveInstanceState", current_tag);
        startService();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            String tag = savedInstanceState.getString("current_tag");
            if (tag != null) {
                switchFragment(tag);
            }
        }
        LogUtils.e("onRestoreInstanceState", current_tag);
        startService();
    }


    /**
     * 后退键不finish界面
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && BackgroundUtils.getApplicationValue(MyApplication.getMyApplication())) {
            //showForcePOP("v1.0.1", "1.大家好，今天是星期四###2.你是谁啊，我是风儿你是沙###3.今天好高兴，杀了一个程序员祭天", "http://img2.imgtn.bdimg.com");
        }
    }

    /**
     * 升级提示框
     *
     * @param apkVersionVame
     * @param updateContent
     * @param address
     */
    private void showForcePOP(String apkVersionVame, String updateContent, final String address) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.activity_main_force_pop, null);
        if (window == null) {
            window = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        window.setBackgroundDrawable(new ColorDrawable(0x80000000));
        window.setOutsideTouchable(false);
        window.showAtLocation(view, Gravity.CENTER, 0, 0);
        TextView main_code = (TextView) view.findViewById(R.id.tv_main_code);
        TextView main_content = (TextView) view.findViewById(R.id.tv_main_content);
        TextView main_bt = (TextView) view.findViewById(R.id.tv_main_bt);
        main_code.setText(apkVersionVame == null ? " " : apkVersionVame);
        if (updateContent != null) {
            String[] strings = updateContent.split("###");
            main_content.setText(TextUtils.join("\r\n", strings));
        } else {
            main_content.setText(" ");
        }
        if (address != null) {
            main_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent updataService = new Intent(mActivity, DownFileService.class);
                    updataService.putExtra("downloadurl", address);
                    startService(updataService);
                }
            });
        }
    }


    /**
     * 初始化华为服务 华为推送下载地址 https://obs.cn-north-2.myhwclouds.com/hms-ds-wf/sdk/HMS_SDK_2.6.0.301.zip
     */
    private HuaweiApiClient client = null;

    private void initHuaWeiPush() {
        client = new HuaweiApiClient.Builder(mActivity)
                .addApi(HuaweiPush.PUSH_API)
                .addConnectionCallbacks(new HuaweiApiClient.ConnectionCallbacks() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public void onConnected() {
                        //华为移动服务client连接成功，在这边处理业务自己的事件
                        if (!MainActivity.this.isDestroyed() && !MainActivity.this.isFinishing()) {
                            Log.e("------------", "HuaweiApiClient 连接成功");
                            getTokenAsync();
                        }
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                        //HuaweiApiClient异常断开连接, if 括号里的条件可以根据需要修改
                        client.connect(mActivity);
                        Log.e("---------", "HuaweiApiClient 连接断开");
                    }
                })
                .addOnConnectionFailedListener(new HuaweiApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.e("------------", "HuaweiApiClient连接失败，错误码：" + result.getErrorCode());
                        if (HuaweiApiAvailability.getInstance().isUserResolvableError(result.getErrorCode())) {
                            final int errorCode = result.getErrorCode();
                            new Handler(getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        // 此方法必须在主线程调用, xxxxxx.this 为当前界面的activity
                                        HuaweiApiAvailability.getInstance().resolveError(MainActivity.this, errorCode, REQUEST_HMS_RESOLVE_ERROR);
                                    } catch (Exception e) {

                                    }
                                }
                            });
                        } else {
                            //其他错误码请参见开发指南或者API文档
                        }
                    }
                }).build();
        //建议在oncreate的时候连接华为移动服务
        //业务可以根据自己业务的形态来确定client的连接和断开的时机，但是确保connect和disconnect必须成对出现
        client.connect(mActivity);
    }

    private void getTokenAsync() {
        if (!client.isConnected()) {
            Log.e("华为token-----------", "获取token失败，原因：HuaweiApiClient未连接");
            client.connect(mActivity);
            return;
        }

        Log.e("------------", "异步接口获取push token");
        PendingResult<TokenResult> tokenResult = HuaweiPush.HuaweiPushApi.getToken(client);
        tokenResult.setResultCallback(new ResultCallback<TokenResult>() {
            @Override
            public void onResult(TokenResult result) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_HMS_RESOLVE_ERROR) {
            if (resultCode == Activity.RESULT_OK) {
                int result = data.getIntExtra(Constant.REQUEST_HMS_EXTRA_RESULT, 0);
                if (result == ConnectionResult.SUCCESS) {
                    Log.e("--------------", "错误成功解决");
                    if (!client.isConnecting() && !client.isConnected()) {
                        client.connect(mActivity);
                    }
                } else if (result == ConnectionResult.CANCELED) {
                    Log.e("------------------", "解决错误过程被用户取消");
                } else if (result == ConnectionResult.INTERNAL_ERROR) {
                    Log.e("---------------", "发生内部错误，重试可以解决");
                    //开发者可以在此处重试连接华为移动服务等操作，导致失败的原因可能是网络原因等
                } else {
                    Log.e("----------------", "未知返回码");
                }
            } else {
                Log.e("-------------", "调用解决方案发生错误");
            }
        }
    }
}

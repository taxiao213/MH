package com.haxi.mh.ui.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
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
import com.haxi.mh.constant.HConstants;
import com.haxi.mh.model.db.DBMsgTipDoInfo;
import com.haxi.mh.service.PlayMusicService;
import com.haxi.mh.ui.fragment.HomeCreateTaskFragment;
import com.haxi.mh.ui.fragment.HomeManageFragment;
import com.haxi.mh.ui.fragment.HomePageFragment;
import com.haxi.mh.ui.fragment.HomePeopleFragment;
import com.haxi.mh.utils.background.BackgroundUtils;
import com.haxi.mh.utils.dbutil.DBMsgTipUtil;
import com.haxi.mh.utils.im.IMConstants;
import com.haxi.mh.utils.im.IMLogHelper;
import com.haxi.mh.utils.im.IMNoticeReceiver;
import com.haxi.mh.utils.im.IMUtils;
import com.haxi.mh.utils.model.LogUtils;
import com.haxi.mh.utils.net.DownFileService;
import com.haxi.mh.utils.ui.UIUtil;
import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiAvailability;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.client.PendingResult;
import com.huawei.hms.support.api.client.ResultCallback;
import com.huawei.hms.support.api.entity.push.TokenResp;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.huawei.hms.support.api.push.TokenResult;
import com.meizu.cloud.pushinternal.DebugLogger;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

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
    private BroadcastReceiver receiver;
    private IMNoticeReceiver noticeBroad;
    private PowerManager pm;
    private Long timedf = 1000 * 60 * 2L;//10min
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void getData() {
        rg.setOnCheckedChangeListener(this);
        switchFragment(TAG_FRAGMENT1);
        initReceiver();
        initHuaWeiSDK();
        initIMReceiver();
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
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


    //<editor-fold desc="即时通讯 - API">

    /**
     * 开启服务
     */
    private void startService() {
        IMUtils.getInstance().startServiceMain();
    }


    private void startTimer() {
        Timer onlineTimer = IMConstants.timer.get("onlineTimer");
        if (onlineTimer == null) {
            onlineTimer = new Timer();
            IMConstants.timer.put("onlineTimer", onlineTimer);
            LogUtils.e("mh-->>mqService>>MainActivity--startTimer--" + formatter.format(new Date()) + "--进入APP--开启连接的定时,关闭获取离线的定时");
            IMLogHelper.getInstances().write("mh-->>mqService>>MainActivity--startTimer--" + formatter.format(new Date()) + "--进入APP--开启连接的定时");
            onlineTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    startService();
                }
            }, 0, 1000 * 60 * 5);
        }
    }

    /**
     * 最小化APP操作
     */
    private void backTimmer() {
        LogUtils.e("mh-->>mqService>>MainActivity--ACTION_APP_ONSTOP->>最小化APP 关闭连接服务的定时 10min后去主动获取离线消息");
        IMLogHelper.getInstances().write("mh-->>mqService>>MainActivity--backTimmer>>最小化APP 关闭连接服务的定时");
        IMConstants.recording.put("record", true);
        Timer onlineTimer = IMConstants.timer.get("onlineTimer");
        if (onlineTimer != null) {
            onlineTimer.cancel();
            IMConstants.timer.put("onlineTimer", null);
        }
        sendBroadcast(new Intent(IMConstants.ACTION_APP_BACK));
    }


    /**
     * 获取是否有离线消息 0定时 1主动获取一次
     */
    private void getOffMessage(final int type) {
        if (!IMUtils.getInstance().isNetConnected()) {
            if (type == 1) {
                return;
            }
        }

        /*OkHttpClient build = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .build();
        RequestBody body = new FormBody.Builder()
                .add("account", IMUtils.getInstance().getAccount())
                .build();
        Request post = new Request.Builder()
                .post(body)
                .url(IMConstants.baseUrl + "myCenterAction/getIfHaveOfofflineMsg.action")
                .build();
        build.newCall(post).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (type == 1) {
                    LogUtils.e("mh-->>mqService>>MainActivity--getOffMessage--接口调用失败 再次连接");
                    IMLogHelper.getInstances().write("mh-->>mqService>>MainActivity--getOffMessage--接口调用失败 再次连接");
                    tryAgain();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null) {
                    try {
                        String string = response.body().string();
                        JSONObject object = new JSONObject(string);
                        if (object.has("resCode")) {
                            String resCode = object.optString("resCode");
                            LogUtils.e("mh-->>mqService>>MainActivity--getOffMessage-- 接口调用成功 1没有离线消息 0有离线消息 s==" + resCode);
                            IMLogHelper.getInstances().write("mh-->>mqService>>MainActivity--getOffMessage-- 接口调用成功 1没有离线消息 0有离线消息 s==" + resCode);
                            //resCode "1"没有离线消息 "0"有离线消息
                            if (TextUtils.equals("0", resCode)) {
                                Timer timer = IMConstants.timer.get("timer");
                                if (timer != null) {
                                    timer.cancel();
                                    IMConstants.timer.put("timer", null);
                                }
                                if (IMConstants.recording.get("resume") == null || !IMConstants.recording.get("resume")) {
                                    if (IMConstants.recording.get("record") == null || IMConstants.recording.get("record")) {
                                        startService();
                                    }
                                }
                            } else {
                                if (IMConstants.recording.get("resume") == null || !IMConstants.recording.get("resume")) {
                                    if (IMConstants.recording.get("record") == null || IMConstants.recording.get("record")) {
                                        sendBroadcast(new Intent(IMConstants.ACTION_APP_BACK));
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        if (type == 1) {
                            LogUtils.e("mh-->>mqService>>MainActivity--接口调用报错 再次连接 Exception==" + e.getMessage());
                            IMLogHelper.getInstances().write("mh-->>mqService>>MainActivity--getOffMessage--接口调用报错 再次连接 Exception==" + e.getMessage());
                            tryAgain();
                        }
                    }
                }
            }
        });*/
    }

    private void tryAgain() {
        try {
            Thread.sleep(1000 * 60 * 5);
            getOffMessage(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册广播
     */
    private void initReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent == null) return;
                String action = intent.getAction();
                if (action == null) return;
                if (MainActivity.this.isFinishing()) return;
                switch (action) {
                    case IMConstants.ACTION_TEXT_MESSAGE_STATE_YES:
                        // update_state_view.setVisibility(View.VISIBLE);
                        //新消息红点的展示
                        break;
                    case IMConstants.ACTION_TEXT_MESSAGE_STATE_NO:
                        // update_state_view.setVisibility(View.GONE);
                        break;
                    case IMConstants.ACTION_APP_MAIN:
                        String tagFragment = intent.getStringExtra("tag_fragment");
                        if (!TextUtils.equals(current_tag, tagFragment)) {
                            switchFragment(tagFragment);
                        }
                        break;
                    case IMConstants.ACTION_LOGIN_SUCCESS:
                        upLoadToken();
                        getMessage();
                        break;
                    case Intent.ACTION_SCREEN_ON:
                        break;
                    case Intent.ACTION_USER_PRESENT:
                        boolean screenOn = pm.isScreenOn();
                        if (screenOn) {
                            if (IMUtils.getInstance().isLogin()) {
                                if (!IMUtils.getInstance().isForegroundProcess()) {
                                    Long histime = IMUtils.getInstance().getPresentTime();
                                    long currentTime = System.currentTimeMillis();
                                    if (histime == 0) {
                                        LogUtils.e("mh-->>mqService>>MainActivity--->>屏幕解锁(初始化)  从接口获取离线消息  ");
                                        IMLogHelper.getInstances().write("mh-->>mqService>>MainActivity--->>屏幕解锁(初始化)  从接口获取离线消息  ");
                                        IMConstants.recording.put("record", true);
                                        IMUtils.getInstance().savePresentTime(currentTime);
                                        getOffMessage(1);
                                    } else {
                                        if ((currentTime - histime) > timedf) {
                                            LogUtils.e("mh-->>mqService>>MainActivity--->>屏幕解锁(距上次主动获取离线时间 > 2min) 从接口获取离线消息  ");
                                            IMLogHelper.getInstances().write("mh-->>mqService>>MainActivity--->>屏幕解锁(距上次主动获取离线时间 > 2min) 从接口获取离线消息 ");
                                            IMConstants.recording.put("record", true);
                                            IMUtils.getInstance().savePresentTime(currentTime);
                                            getOffMessage(1);
                                        } else {
                                            LogUtils.e("mh-->>mqService>>MainActivity--->>屏幕解锁(距上次主动获取离线时间 < 2min) 不进行操作  ");
                                            IMLogHelper.getInstances().write("mh-->>mqService>>MainActivity--->>屏幕解锁(距上次主动获取离线时间 < 2min) 不进行操作 ");
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    case Intent.ACTION_SCREEN_OFF:
                        boolean isScreenOn = pm.isScreenOn();
                        if (!isScreenOn) {
                            LogUtils.e("mh-->>mqService>>MainActivity--->>锁屏 定时都取消 断开连接");
                            IMLogHelper.getInstances().write("mh-->>mqService>>MainActivity--->>锁屏 定时都取消 断开连接");
                            IMConstants.recording.put("record", false);
                            Timer onlineTimer = IMConstants.timer.get("onlineTimer");
                            if (onlineTimer != null) {
                                onlineTimer.cancel();
                                IMConstants.timer.put("onlineTimer", null);
                            }
                            Timer timer = IMConstants.timer.get("timer");
                            if (timer != null) {
                                timer.cancel();
                                IMConstants.timer.put("timer", null);
                            }
                            sendBroadcast(new Intent(IMConstants.ACTION_APP_BACK));
                        }
                        break;
                    case IMConstants.ACTION_APP_ONSTART:
                        if (IMUtils.getInstance().isLogin()) {
                            if (IMUtils.getInstance().isForegroundProcess()) {
                                LogUtils.e("mh-->>mqService>>MainActivity--ACTION_APP_ONSTART->>进入APP 开启连接服务的定时，关闭获取离线的定时");
                                IMConstants.recording.put("resume", true);
                                IMConstants.recording.put("record", true);
                                startTimer();
                                Timer timer = IMConstants.timer.get("timer");
                                if (timer != null) {
                                    timer.cancel();
                                    IMConstants.timer.put("timer", null);
                                }
                            }
                        }

                        break;
                    case IMConstants.ACTION_APP_ONSTOP:
                        if (IMUtils.getInstance().isLogin()) {
                            if (!IMUtils.getInstance().isForegroundProcess()) {
                                IMConstants.recording.put("resume", false);
                                if (pm != null && pm.isScreenOn()) {
                                    backTimmer();
                                }
                            }
                        }
                        break;
                    case IMConstants.ACTION_TEXT_MESSAGE_REFRESH:
                        haveNoReadMessage();
                        break;

                }
            }
        };
        //注册广播接收者
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(IMConstants.ACTION_TEXT_MESSAGE_STATE_YES);
        mFilter.addAction(IMConstants.ACTION_TEXT_MESSAGE_STATE_NO);
        mFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mFilter.addAction(Intent.ACTION_SCREEN_ON);
        mFilter.addAction(Intent.ACTION_USER_PRESENT);
        mFilter.addAction(IMConstants.ACTION_APP_ONSTART);
        mFilter.addAction(IMConstants.ACTION_APP_ONSTOP);
        mFilter.addAction(IMConstants.ACTION_APP_MAIN);
        mFilter.addAction(IMConstants.ACTION_LOGIN_SUCCESS);
        mFilter.addAction(IMConstants.ACTION_TEXT_MESSAGE_REFRESH);
        registerReceiver(receiver, mFilter);
    }

    /**
     * 注册即时通讯广播
     */
    private void initIMReceiver() {
        noticeBroad = new IMNoticeReceiver();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(IMConstants.MSG_BROADCAST_TYPE_KNOWLEDGE_NOTIFICATION);
        mFilter.addAction(IMConstants.MSG_BROADCAST_TYPE_RECHARGE_REMINDER);
        mFilter.addAction(IMConstants.MSG_BROADCAST_TYPE_MEDICAL_REMINDER);
        mFilter.addAction(IMConstants.MSG_BROADCAST_TYPE_CALL_NOTIFICATION);
        mFilter.addAction(IMConstants.MSG_BROADCAST_TYPE_RETREAT_REMINDER);
        mFilter.addAction(IMConstants.MSG_BROADCAST_TYPE_REGISTRATION_APPOINTMENT_REMINDER);
        mFilter.addAction(IMConstants.MSG_BROADCAST_TYPE_CANCEL_REGISTRATION_APPOINTMENT_REMINDER);
        mFilter.addAction(IMConstants.MSG_BROADCAST_TYPE_WAITING_FOR_CALL_REMINDER);
        mFilter.addAction(IMConstants.MSG_BROADCAST_TYPE_ADVICE_REMINDER);
        mFilter.addAction(IMConstants.MSG_BROADCAST_TYPE_UPDATE_APP);
        registerReceiver(noticeBroad, mFilter);
    }

    private HuaweiApiClient client;

    /**
     * 初始化华为推送SDK 依赖最新版不唤起安装华为移动服务，HMS_SDK_2.6.0.301.jar
     */
    private void initHuaWeiSDK() {
        String brand = "";
        if (brand.equals(IMConstants.BRAND_HUAWEI)) {
            //创建华为移动服务client实例用以使用华为push服务
            //需要指定api为HuaweiPush.PUSH_API
            //连接回调以及连接失败监听
            //华为移动服务client连接成功，在这边处理业务自己的事件
            //HuaweiApiClient异常断开连接, if 括号里的条件可以根据需要修改
            client = new HuaweiApiClient.Builder(this)
                    .addApi(HuaweiPush.PUSH_API)
                    .addConnectionCallbacks(new HuaweiApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected() {
                            //华为移动服务client连接成功，在这边处理业务自己的事件
                            LogUtils.e("--push-- Huawei ApiClient 连接成功");
                            getTokenAsyn();
                        }

                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                        @Override
                        public void onConnectionSuspended(int i) {
                            //HuaweiApiClient异常断开连接, if 括号里的条件可以根据需要修改
                            if (!MainActivity.this.isDestroyed() && !MainActivity.this.isFinishing()) {
                                client.connect(mActivity);
                            }
                            LogUtils.e("--push-- Huawei ApiClient 连接断开");
                        }
                    })
                    .addOnConnectionFailedListener(new HuaweiApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {
                            LogUtils.e("--push-- Huawei ApiClient 连接失败，错误码：" + connectionResult.getErrorCode());
                            if (HuaweiApiAvailability.getInstance().isUserResolvableError(connectionResult.getErrorCode())) {
                                final int errorCode = connectionResult.getErrorCode();
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
                    })
                    .build();

            //建议在oncreate的时候连接华为移动服务
            //业务可以根据自己业务的形态来确定client的连接和断开的时机，但是确保connect和disconnect必须成对出现
            client.connect(mActivity);
        }
    }

    public static final String EXTRA_RESULT = "intent.extra.RESULT";
    private static final int REQUEST_HMS_RESOLVE_ERROR = 1000;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_HMS_RESOLVE_ERROR) {
            if (resultCode == Activity.RESULT_OK) {
                int result = data.getIntExtra(EXTRA_RESULT, 0);
                if (result == ConnectionResult.SUCCESS) {
                    LogUtils.e("--push-- Huawei  错误成功解决");
                    if (!client.isConnecting() && !client.isConnected()) {
                        client.connect(mActivity);
                    }
                } else if (result == ConnectionResult.CANCELED) {
                    LogUtils.e("--push-- Huawei  解决错误过程被用户取消");
                } else if (result == ConnectionResult.INTERNAL_ERROR) {
                    LogUtils.e("--push-- Huawei  发生内部错误，重试可以解决");
                    //开发者可以在此处重试连接华为移动服务等操作，导致失败的原因可能是网络原因等
                } else {
                    LogUtils.e("--push-- Huawei  未知返回码");
                }
            } else {
                LogUtils.e("--push-- Huawei  调用解决方案发生错误");
            }
        }
    }

    /**
     * 异步接口获取 token
     */
    private void getTokenAsyn() {
        if (!client.isConnected()) {
            client.connect(mActivity);
            return;
        }

        PendingResult<TokenResult> tokenResult = HuaweiPush.HuaweiPushApi.getToken(client);
        tokenResult.setResultCallback(new ResultCallback<TokenResult>() {
            @Override
            public void onResult(TokenResult result) {
                String brand = "";//手机厂商
                if (brand.equals(IMConstants.BRAND_HUAWEI)) {
                    if (result != null) {
                        TokenResp tokenRes = result.getTokenRes();
                        if (tokenRes != null) {
                            String token = tokenRes.getToken();
                            sendBroadcast(new Intent(IMConstants.ACTION_TOKEN_REFRESH));
                            LogUtils.e("--push-- Huawei  异步接口获取 token== " + token);

                        }
                    }
                }
            }
        });
    }

    /**
     * 登陆成功后上传Token
     */
    private void upLoadToken() {
    }

    @Override
    protected void onResume() {
        MiPushClient.clearNotification(MainActivity.this);
        NotificationManager manager = (NotificationManager) mActivity.getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.cancelAll();
        }
        super.onResume();

        haveNoReadMessage();
    }

    @Override
    protected void onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        if (noticeBroad != null) {
            unregisterReceiver(noticeBroad);
        }
        super.onDestroy();
    }

    //</editor-fold>


    /**
     * 开启获取消息
     */
    private void getMessage() {
        IMUtils.getInstance().startServiceLogin();
        IMUtils.getInstance().getMessage(IMUtils.IM_MESSAGE_TYPE2);
    }

    /**
     * 是否有未读消息 红点(有)
     */
    public void haveNoReadMessage() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                List<DBMsgTipDoInfo> doInfos = DBMsgTipUtil.getInstance().queryNoReadMessage();
                if (doInfos != null && doInfos.size() > 0) {
                    if (TextUtils.equals(current_tag, "")) {
                        for (DBMsgTipDoInfo info : doInfos) {
                            if (info != null) {
                                info.setMsgtip_is_read(true);
                                DBMsgTipUtil.getInstance().update(info);
                            }
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //切换是否有消息的图标
//                                cdMessageRB.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home_message_selector_have_new_message, 0, 0);
                            }
                        });
                        return;
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //切换是否有消息的图标
//                        cdMessageRB.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home_message_selector, 0, 0);
                    }
                });
            }
        });
    }

    private String getAppKey(String tag) {
        String appKey = null;
        try {
            ApplicationInfo appInfo = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            appKey = appInfo.metaData.getString(tag);
            DebugLogger.e("push", tag + "=" + appKey);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appKey;
    }


    private String getAppId(String tag){
        int appId = 0;
        try {
            ApplicationInfo appInfo = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            appId = appInfo.metaData.getInt(tag);
            DebugLogger.e("push", tag + "=" + appId);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return String.valueOf(appId);
    }

}

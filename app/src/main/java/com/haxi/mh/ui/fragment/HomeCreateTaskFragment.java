package com.haxi.mh.ui.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.haxi.mh.R;
import com.haxi.mh.base.BaseFragment;
import com.haxi.mh.service.PlayMusicService;
import com.haxi.mh.ui.activity.AidlActivity;
import com.haxi.mh.ui.activity.BusinessApprovalActivity;
import com.haxi.mh.ui.activity.MaterialDesignActivity;
import com.haxi.mh.utils.model.LogUtils;
import com.haxi.mh.utils.ui.toast.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 文档
 * Created by Han on 2017/12/27
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class HomeCreateTaskFragment extends BaseFragment {

    @BindView(R.id.title_back)
    ImageView titleBack;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.et)
    EditText et;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_homecreatetask;
    }

    @Override
    protected void initView() {
        titleBack.setVisibility(View.GONE);
        titleTv.setText(R.string.homecreatetask_name);
        //监听软键盘搜索键
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ToastUtils.showShortToast("哈哈");
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.bt_01, R.id.bt_02, R.id.bt_03, R.id.bt_04})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_01:
                startActivity(new Intent(mActivity, MaterialDesignActivity.class));
                break;
            case R.id.bt_02:
                createAlarm();
                startActivity(new Intent(mActivity, MaterialDesignActivity.class));
                break;
            case R.id.bt_03:
                startActivity(new Intent(mActivity, BusinessApprovalActivity.class));

                java();
                helo((a, b) -> {
                    String ss = a + b;
                    LogUtils.e(ss);
                    return ss;

                });
                break;
            case R.id.bt_04:
                startActivity(new Intent(mActivity, AidlActivity.class));
                break;
        }
    }

    private void helo(OnJava java) {
        String a = "2222";
        int b = 10;
        String add = java.add(a, b);
    }

    private void java() {
        new Thread(() -> {
            LogUtils.e("runnable");
        }).start();
    }

    public interface OnJava {
        String add(String a, int b);
    }

    /**
     * 创建定时 Java中的Timer 和Android 中的Alarm机制
     * Timer不适合长期运行在后台的定时任务，Android 手机 在长期不使用的情况下会CPU 会进入睡眠状态，可能导致定时任务无法执行
     * Alarm 具有唤醒CPU功能，大多数情况下都能保证正常运行 AlarmManager来实现
     * Android 4.4之后Alarm任务触发会变得不准确，解决方案调用AlarmManager setExact() 代替 set()
     * Android 6.0之后为了省电间断性的进入Doze模式 该模式下Alarm任务触发会变得不准确 setExactAndAllowWhileIdle() 代替 setAndAllowWhileIdle()
     */
    private void createAlarm() {
        AlarmManager service = (AlarmManager) mActivity.getSystemService(mActivity.ALARM_SERVICE);
        /*
         参数含义
         RTC_WAKEUP  System.currentTimeMillis() 会唤醒CPU
         RTC  System.currentTimeMillis()
         ELAPSED_REALTIME_WAKEUP  SystemClock.elapsedRealtime() 会唤醒CPU
         ELAPSED_REALTIME  SystemClock.elapsedRealtime()
        */
        Long timer = SystemClock.elapsedRealtime() + 10;
        Intent intent = new Intent(mActivity, PlayMusicService.class);
        PendingIntent pendingIntent = PendingIntent.getService(mActivity, 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        service.set(AlarmManager.ELAPSED_REALTIME, timer, pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            service.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME, timer, pendingIntent);
            service.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME, timer, pendingIntent);
        }
        //service.setExact(AlarmManager.ELAPSED_REALTIME, timer, pendingIntent);
    }
}

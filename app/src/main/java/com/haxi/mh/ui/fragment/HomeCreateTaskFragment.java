package com.haxi.mh.ui.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.haxi.mh.R;
import com.haxi.mh.aidl.BinderPool;
import com.haxi.mh.aidl.Book;
import com.haxi.mh.aidl.ComputeImpl;
import com.haxi.mh.aidl.ICompute;
import com.haxi.mh.aidl.ISecurityCenter;
import com.haxi.mh.aidl.SecurityCenterImpl;
import com.haxi.mh.base.BaseFragment;
import com.haxi.mh.contentprovider.BookContentProvider;
import com.haxi.mh.service.PlayMusicService;
import com.haxi.mh.service.TcpService;
import com.haxi.mh.ui.activity.AidlActivity;
import com.haxi.mh.ui.activity.BusinessApprovalActivity;
import com.haxi.mh.ui.activity.MaterialDesignActivity;
import com.haxi.mh.utils.model.LogUtils;
import com.haxi.mh.utils.ui.toast.ToastUtils;
import com.haxi.mh.utils.ui.view.StatusBarUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

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
    private Socket clientSocket = null;
    private PrintWriter printWriter;
    private static final int SOCKET_SUCCESS = 0;
    private static final int SOCKET_NEWMESSAGE = 1;
    private String[] strings = new String[]{"client1", "client2", "client3"};
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SOCKET_SUCCESS:
                    LogUtils.e("--- initSocket --- SOCKET_SUCCESS");
                    break;
                case SOCKET_NEWMESSAGE:
                    LogUtils.e("--- initSocket --- SOCKET_NEWMESSAGE" + msg.obj);
                    break;
            }
        }
    };

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
        StatusBarUtil.setPaddingSmart(mActivity, titleTv);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (clientSocket != null) {
            try {
                clientSocket.shutdownInput();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick({R.id.bt_01, R.id.bt_02, R.id.bt_03, R.id.bt_04, R.id.bt_05, R.id.bt_06, R.id.bt_07, R.id.bt_08})
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
            case R.id.bt_05:
                initProvider();
                break;
            case R.id.bt_06:
                mActivity.startService(new Intent(mActivity, TcpService.class));
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        initSocket();
                    }
                }.start();
                break;
            case R.id.bt_07:
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        connectBinderPool();
                    }
                }.start();
                break;

                case R.id.bt_08:

                    break;
        }
    }

    /**
     * 连接线程池
     */
    private void connectBinderPool() {
        BinderPool instance = BinderPool.getInstance(mActivity);
        IBinder securitybinder = instance.queryBinder(BinderPool.BINDER_SECURITY);
        ISecurityCenter iSecurityCenter = SecurityCenterImpl.asInterface(securitybinder);
        try {
            String encrypt = iSecurityCenter.encrypt("hahahahah----");

            LogUtils.e("--- BinderPool --- encrypt ==" + encrypt);

            LogUtils.e("--- BinderPool --- decrypt ==" + iSecurityCenter.decrypt(encrypt));

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        IBinder computebinder = instance.queryBinder(BinderPool.BINDER_COMPUTE);
        ICompute iCompute = ComputeImpl.asInterface(computebinder);
        try {
            int add = iCompute.add(1, 2);
            LogUtils.e("--- BinderPool --- compute ==" + add);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化socket
     */
    private void initSocket() {
        Socket socket = null;
        while (socket == null) {
            try {
                socket = new Socket("localhost", 8080);
                clientSocket = socket;
                printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                handler.sendEmptyMessage(SOCKET_SUCCESS);
                LogUtils.e("--- initSocket --- success");
            } catch (IOException e) {
                SystemClock.sleep(1000);
                e.printStackTrace();
                LogUtils.e("--- initSocket --- failure");
            }
        }

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!this.isDetached()) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    handler.obtainMessage(SOCKET_NEWMESSAGE, line).sendToTarget();
                }
                Random random = new Random();
                int anInt = random.nextInt(strings.length);
                printWriter.println(strings[anInt]);
            }
            printWriter.close();
            bufferedReader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化内容提供者
     */
    private void initProvider() {
        Uri bookuri = BookContentProvider.BOOK_CONTENT_URI;
        ContentValues values = new ContentValues();
        values.put("_id", 10);
        values.put("name", "name1110");
        mActivity.getContentResolver().insert(bookuri, values);

        Cursor query = mActivity.getContentResolver().query(bookuri, new String[]{"_id", "name"}, null, null, null);
        while (query.moveToNext()) {
            Book book = new Book();
            book.bookId = query.getInt(0);
            book.bookName = query.getString(1);

            LogUtils.e("--- contentprovider --- " + book.toString());
        }
        query.close();
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

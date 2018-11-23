package com.haxi.mh.utils.im;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;


import com.haxi.mh.utils.model.LogUtils;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.StreamError;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.offline.OfflineMessageManager;

import java.util.Timer;
import java.util.concurrent.Executors;

/**
 * Created by Han on 2018/10/15
 * Email:yin13753884368@163.com
 */

public class IMService extends Service implements ConnectionListener {

    private XMPPTCPConnection connection = null;
    private IMManager manager = null;
    private Timer timer = null;
    private IMMessageListener listener;
    private BroadcastReceiver receiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        IMLogHelper.getInstances().write("mh-->>IMService---onCreate()>> 第一次初始化连接");
        listener = new IMMessageListener(IMService.this);
        registerBroadCast();
        super.onCreate();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        init();
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        LogUtils.e("mh-->>IMService---ondestory>>关闭服务");
        if (manager != null && manager.connection != null) {
            if (connection != null) {
                connection.removeConnectionListener(IMService.this);
                connection.disconnect();
            }
            if (manager != null) {
                manager.logout();
            }
            connection = null;
            manager = null;
            LogUtils.e("mh-->>IMService---ondestory>>关闭服务 退出openfire");
            IMLogHelper.getInstances().write("mh-->>IMService---ondestory()>>关闭服务  退出openfire");
        }
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }


    /**
     * 注册广播
     */
    private void registerBroadCast() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String action = intent.getAction();
                    if (!TextUtils.isEmpty(action))
                        switch (action) {
                            case IMConstants.ACTION_APP_BACK:
                                loginOut();
                                break;
                            default:
                                break;
                        }
                }
            }
        };

        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction(IMConstants.ACTION_APP_BACK);
        registerReceiver(receiver, intentfilter);
    }

    /**
     * 退出登录
     */
    private void loginOut() {
        if (manager != null && manager.connection != null) {
            if (connection != null) {
                connection.removeConnectionListener(IMService.this);
            }
            boolean logout = manager.logout();
            connection = null;
            manager = null;
            sendBroadcast(new Intent(IMConstants.ACTION_LOGIN_OPENFIRE_FAILURE_STATE));
            LogUtils.e("mh-->>IMService---initXMPP()>> 移除监听,退出登录,断开连接 logout==" + logout);
            IMLogHelper.getInstances().write("mh-->>IMService---initXMPP()>> 移除监听,退出登录,断开连接 logout==" + logout);
        }
    }


    /**
     * 初始化XMPP
     */
    private void init() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                initXMPP();
            }
        });
    }

    private void initXMPP() {
        LogUtils.e("mh-->>IMService---initXMPP()>>记录的状态值：" + IMConstants.recording.get("record"));
        if (IMConstants.recording.get("record") != null && !IMConstants.recording.get("record")) {
            if (manager != null && manager.connection != null) {
                if (connection != null) {
                    connection.removeConnectionListener(IMService.this);
                }
                boolean logout = manager.logout();
                connection = null;
                manager = null;
                sendBroadcast(new Intent(IMConstants.ACTION_LOGIN_OPENFIRE_FAILURE_STATE));
                LogUtils.e("mh-->>IMService---initXMPP()>> 移除监听,退出登录,断开连接 logout==" + logout);
                IMLogHelper.getInstances().write("mh-->>IMService---initXMPP()>> 移除监听,退出登录,断开连接 logout==" + logout);
            }
            return;
        }

        if (IMUtils.getInstance().isNetConnected()) {
            if (manager == null || manager.connection == null || connection == null || !connection.isConnected() || !connection.isAuthenticated()) {
                try {
                    LogUtils.e("mh-->>IMService---initXMPP()>> 初始化连接开始");
                    IMLogHelper.getInstances().write("mh-->>IMService---initXMPP()>> 初始化连接开始");
                    manager = IMManager.getInstance();
                    manager.connect();
                    connection = manager.getConnection();
                    if (manager.isConnected()) {
                        if (connection != null) {
                            connection.removeConnectionListener(IMService.this);
                            connection.addConnectionListener(IMService.this);
                            String account = IMUtils.getInstance().getAccount().toLowerCase();
                            connection.login(account, account + "123");
                            ChatManager chatManager = ChatManager.getInstanceFor(connection);
                            chatManager.addChatListener(new ChatManagerListener() {
                                @Override
                                public void chatCreated(Chat chat, boolean b) {
                                    if (listener == null) {
                                        listener = new IMMessageListener(IMService.this);
                                    }
                                    chat.addMessageListener(listener);
                                }
                            });
                        }
                    } else {
                        LogUtils.e("mh-->>IMService---initXMPP()>>初始化时 未连接");
                        IMLogHelper.getInstances().write("mh-->>IMService---initXMPP()>>初始化时 未连接");
                        errorInit();
                    }
                } catch (Exception e) {
                    LogUtils.e("mh-->>IMService---initXMPP()>>初始化时报错...再次连接 " + e.getMessage());
                    IMLogHelper.getInstances().write("mh-->>IMService---initXMPP()>>初始化时报错...再次连接" + e.getMessage());
                    if (manager != null && manager.connection != null) {
                        if (connection != null) {
                            connection.removeConnectionListener(IMService.this);
                        }
                        manager.logout();
                    }
                    if (e.getMessage().contains("SASLError")) {
                        return;
                    }
                    errorInit();
                }
            } else {
                LogUtils.e("mh-->>IMService---initXMPP()>>已连接");
                IMLogHelper.getInstances().write("mh-->>IMService---initXMPP()>>已连接");
                getOfflineMessage();
            }
        } else {
            LogUtils.e("mh-->>IMService---initXMPP()>>初始化连接时 网络断开");
            sendBroadcast(new Intent(IMConstants.ACTION_LOGIN_OPENFIRE_FAILURE_STATE));
            if (connection != null) {
                connection.disconnect();
            }
            manager = null;
            connection = null;
            IMLogHelper.getInstances().write("mh-->>IMService---initXMPP()>>初始化连接时 网络断开");
        }
    }

    /**
     * 发生错误再次调用
     */
    private void errorInit() {
        sendBroadcast(new Intent(IMConstants.ACTION_LOGIN_OPENFIRE_FAILURE_STATE));
        if (connection != null) {
            connection.disconnect();
        }
        manager = null;
        connection = null;
        try {
            Thread.sleep(1000 * 5);
            initXMPP();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void timeCancel() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void connected(XMPPConnection xmppConnection) {
        LogUtils.e("mh-->>IMService---ConnectionListener>>connected");
    }

    /**
     * xmppConnection - the XMPPConnection which successfully authenticated.
     * resumed - true if a previous XMPP session's stream was resumed
     *
     * @param xmppConnection
     * @param resumed
     */
    @Override
    public void authenticated(XMPPConnection xmppConnection, boolean resumed) {
        timeCancel();
        if (xmppConnection != null && xmppConnection.isAuthenticated()) {
            try {
                LogUtils.e("mh-->>IMService---authenticated>>发送广播");
                IMLogHelper.getInstances().write("mh-->>IMService---authenticated 连接认证成功");
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        getOfflineMessage();
                    }
                });

            } catch (Exception e) {
                LogUtils.e("mh-->>IMService---authenticated NotConnectedException==" + e.getMessage());
                IMLogHelper.getInstances().write("mh-->>IMService---authenticated NotConnectedException==" + e.getMessage());
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (manager != null && manager.connection != null) {
                            if (connection != null) {
                                connection.removeConnectionListener(IMService.this);
                            }
                            manager.logout();
                        }
                        errorInit();
                    }
                });
            }
        }
    }

    @Override
    public void connectionClosed() {
        LogUtils.e("mh-->>IMService---ConnectionListener>>connectionClosed");
        IMLogHelper.getInstances().write("mh-->>IMService---ConnectionListener>>connectionClosed");
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        timeCancel();
        if (!IMUtils.getInstance().isNetConnected()) {
            return;
        }
        IMLogHelper.getInstances().write("mh-->>IMService---connectionClosedOnError>>" + e.getMessage());

        if (e instanceof XMPPException) {
            XMPPException.StreamErrorException exception = (XMPPException.StreamErrorException) e;
            StreamError error = exception.getStreamError();
            if (error != null) {
                String string = error.getCondition().toString();
                LogUtils.e("mh-->>IMService---connectionClosedOnError>>" + string);
                if (string.equalsIgnoreCase("conflict")) {// 被踢下线
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                LogUtils.e("mh-->>IMService---connectionClosedOnError>>重新设置状态");
                                Presence presenced = new Presence(Presence.Type.available);
                                if (connection != null) {
                                    connection.sendStanza(presenced);
                                }
                                sendBroadcast(new Intent(IMConstants.ACTION_LOGIN_OPENFIRE_SUCCESS_STATE));
                                timeCancel();

                            } catch (Exception e) {
                                LogUtils.e("mh-->>IMService---Exception>" + e.getMessage());
                                timeCancel();
                                if (manager != null && manager.connection != null) {
                                    if (connection != null) {
                                        connection.removeConnectionListener(IMService.this);
                                    }
                                    manager.logout();
                                }
                                errorInit();
                            }
                        }
                    });
                }
            }
        }

    }

    @Override
    public void reconnectionSuccessful() {
        LogUtils.e("mh-->>IMService---ConnectionListener>>reconnectionSuccessful");
        timeCancel();
        sendBroadcast(new Intent(IMConstants.ACTION_LOGIN_OPENFIRE_SUCCESS_STATE));
        IMLogHelper.getInstances().write("mh-->>IMService---ConnectionListener>>reconnectionSuccessful 重连成功");
    }

    @Override
    public void reconnectingIn(int i) {
        LogUtils.e("mh-->>IMService---ConnectionListener>>reconnectingIn" + i);
        sendBroadcast(new Intent(IMConstants.ACTION_LOGIN_OPENFIRE_FAILURE_STATE));
        IMLogHelper.getInstances().write("mh-->>IMService---ConnectionListener>>reconnectingIn 重新开始连接" + i);
    }

    @Override
    public void reconnectionFailed(Exception e) {
        LogUtils.e("mh-->>IMService---ConnectionListener>>reconnectionFailed" + e.getMessage());
        timeCancel();
        sendBroadcast(new Intent(IMConstants.ACTION_LOGIN_OPENFIRE_FAILURE_STATE));
        if (!IMUtils.getInstance().isNetConnected()) {
            return;
        }
        IMLogHelper.getInstances().write("mh-->>IMService---reconnectionFailed>>" + e.getMessage());
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                errorInit();
            }
        });
    }

    public synchronized void getOfflineMessage() {
        LogUtils.e("mh-->>IMService---getOfflineMessage-->开始");
        if (manager == null || manager.connection == null || connection == null || !connection.isConnected() || !connection.isAuthenticated()) {
            sendBroadcast(new Intent(IMConstants.ACTION_LOGIN_OPENFIRE_FAILURE_STATE));
            return;
        }
        Presence presence;
        presence = new Presence(Presence.Type.unavailable);
        try {
            connection.sendStanza(presence);
            OfflineMessageManager offlineMessageManager = new OfflineMessageManager(connection);
            sendBroadcast(new Intent(IMConstants.ACTION_LOGIN_OPENFIRE_SUCCESS_STATE));
            LogUtils.e("mh-->>IMService---getOfflineMessage-->离线消息count==" + offlineMessageManager.getMessageCount());
            offlineMessageManager.getMessages();
            offlineMessageManager.deleteMessages();
            presence = new Presence(Presence.Type.available);
            connection.sendStanza(presence);
        } catch (Exception e) {
            LogUtils.e("mh-->>IMService---getOfflineMessage +Exception==" + e.getMessage());
            //  sendBroadcast(new Intent(IMConstants.ACTION_LOGIN_OPENFIRE_FAILURE_STATE));
        }
    }
}

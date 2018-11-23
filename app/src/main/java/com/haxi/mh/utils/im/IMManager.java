package com.haxi.mh.utils.im;

import android.util.Log;


import com.haxi.mh.utils.model.LogUtils;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterGroup;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;

/**
 * IMManager 消息管理
 * Created by Han on 2018/10/15
 * Email:yin13753884368@163.com
 */

public class IMManager {

    private static IMManager mConnManager;
    public XMPPTCPConnection connection = null;

    private IMManager() {

    }

    /**
     * 初始化管理单例
     *
     * @return
     */
    public static IMManager getInstance() {
        if (mConnManager == null) {
            synchronized (IMManager.class) {
                if (mConnManager == null)
                    mConnManager = new IMManager();
            }
        }
        return mConnManager;
    }


    /**
     * 连接服务器
     *
     * @return XMPPTCPConnection对象
     */
    public XMPPTCPConnection connect() {
        try {
            XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                    .setHost(IMConstants.HOST)//服务器IP地址
                    //服务器端口
                    .setPort(IMConstants.PORT)
                    //服务器名称
                    .setServiceName(IMConstants.SERVICE_NAME)
                    //是否开启安全模式
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                    //是否开启压缩
                    .setCompressionEnabled(true)
                    //发送登录状态，为了获取离线消息，不发送登录状态
                    .setSendPresence(false)
                    //开启调试模
                    // .setConnectTimeout(4000)
                    .setDebuggerEnabled(false)
                    .build();

            connection = new XMPPTCPConnection(config);
            ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(connection);
            reconnectionManager.enableAutomaticReconnection();//允许自动重连
            reconnectionManager.setFixedDelay(5);//重连间隔时间
            connection.setPacketReplyTimeout(20000);//30000
            connection.connect();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("mh-->>mqService--->>IMManager  connect()" + e.getMessage());
            IMLogHelper.getInstances().write("mh-->>mqService--->>IMManager connect() 初始化manager报错" + e.getMessage());
        }
        return connection;

    }

    /**
     * 获取连接
     *
     * @return XMPPTCPConnection 对象
     */
    public XMPPTCPConnection getConnection() {
        if (connection != null && connection.isConnected()) {
            return connection;
        }
        return null;
    }


    /**
     * 是否连接成功
     *
     * @return true 连接成功 false 连接失败
     */
    public boolean isConnected() {
        if (IMUtils.getInstance().isNetConnected()) {
            if (connection == null) {
                return false;
            }
            try {
                if (!connection.isConnected()) {
                    connection.connect();
                    return true;
                }
                return true;
            } catch (Exception e) {
                connection = null;
                return false;
            }
        } else {
            return false;
        }
    }


    /**
     * 退出openfire
     *
     * @return true 退出成功 false 退出失败
     */
    public boolean logout() {
        try {
            if (!isConnected()) {
                return false;
            }
            connection.disconnect();
            connection.instantShutdown();
            IMLogHelper.getInstances().write("mh-->>IMManager logout()>>退出登录");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            IMLogHelper.getInstances().write("mh-->>IMManager logout()>>退出登录报错 " + e.getMessage());
            LogUtils.e("mh-->>IMManager logout()>>退出登录报错 " + e.getMessage());
            return false;
        }
    }

    /**
     * 获取账户昵称
     *
     * @return String
     */
    public String getAccountName() {
        String log = "";
        if (isConnected()) {
            try {
                return AccountManager.getInstance(connection).getAccountAttribute("name");
            } catch (Exception e) {

            }
        }
        return log;
    }


    /**
     * 获取聊天对象管理器
     *
     * @return ChatManager
     */
    public ChatManager getChatManager() {
        if (isConnected()) {
            return ChatManager.getInstanceFor(connection);
        }
        return null;
    }

    /**
     * 发送聊天信息
     *
     * @param message 消息
     * @param to      发送的对象
     * @return true 发送成功 false 发送失败
     */
    public boolean sendChatMessage(String message, String to) {
        try {
            ChatManager chatManager = getChatManager();
            if (chatManager != null) {
                Chat chat = chatManager.createChat(to + "@" + IMConstants.SERVICE_NAME);
                if (connection != null && connection.isConnected()) {
                    Roster roster = Roster.getInstanceFor(connection);
                    if (roster != null) {
                        Presence presence = roster.getPresence(IMUtils.getInstance().getAccount() + "@" + IMConstants.SERVICE_NAME);
                        if (presence != null) {
                            Presence.Type type = presence.getType();
                            if (type.equals(Presence.Type.unavailable)) {
                                Presence presenceOnline = new Presence(Presence.Type.available);
                                connection.sendStanza(presenceOnline);
                            }
                        }
                    }
                    chat.sendMessage(message);
                    Log.e("sendMessge", "发送成功");
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.e("sendMessge", "发送失败", e);
            return false;
        }
    }


    /**
     * 给不同的服务器发送聊天信息
     *
     * @param message    消息
     * @param to         发送的对象
     * @param serverName 服务器名
     * @return true 发送成功 false 发送失败
     */
    public boolean sendChatMessageToServer(String message, String to, String serverName) {
        try {
            ChatManager chatManager = getChatManager();
            if (chatManager != null) {
                Chat chat = chatManager.createChat(to + "@" + serverName);
                if (connection != null && connection.isConnected()) {
                    Roster roster = Roster.getInstanceFor(connection);
                    if (roster != null) {
                        Presence presence = roster.getPresence(IMUtils.getInstance().getAccount() + "@" + IMConstants.SERVICE_NAME);
                        if (presence != null) {
                            Presence.Type type = presence.getType();
                            if (type.equals(Presence.Type.unavailable)) {
                                Presence presenceOnline = new Presence(Presence.Type.available);
                                connection.sendStanza(presenceOnline);
                            }
                        }
                    }
                    chat.sendMessage(message);
                    Log.e("sendMessge", "发送成功");
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.e("sendMessge", "发送失败", e);
            return false;
        }
    }


    /**
     * 添加好友
     *
     * @param userAccount 用户账号
     * @return true 添加成功 false 添加失败
     */
    public boolean addFriend(String userAccount) {
        if (isConnected()) {
            try {
                String groupName = "我的好友";//所属组名
                Roster roster = Roster.getInstanceFor(connection);
                RosterGroup group = roster.getGroup(groupName);
                if (group == null) {
                    roster.createGroup(groupName);
                }
                roster.createEntry(userAccount, userAccount, new String[]{groupName});
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    /**
     * 删除好友
     *
     * @param userAccount 用户账号
     * @return true 删除成功 false 删除失败
     */
    public boolean deleteFriend(String userAccount) {
        if (isConnected()) {
            try {
                Roster roster = Roster.getInstanceFor(connection);
                RosterEntry entry = roster.getEntry(userAccount);
                if (entry != null) {
                    roster.removeEntry(entry);
                    return true;
                } else {
                    RosterEntry rosterEntry = roster.getEntry(userAccount + "@" + IMConstants.SERVICE_NAME);
                    if (rosterEntry != null) {
                        roster.removeEntry(rosterEntry);
                        return true;
                    } else {
                        return false;
                    }
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

}

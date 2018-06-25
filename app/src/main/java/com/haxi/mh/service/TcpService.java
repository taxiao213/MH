package com.haxi.mh.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.haxi.mh.utils.model.LogUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * socket 连接 TCP(三次握手，传输数据稳定，超时机制) || UDP(无连接，单向通信，效率高，保证不了正确传输)
 * Created by Han on 2018/6/25
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class TcpService extends Service {
    private boolean isDestory = false;
    private String[] strings = new String[]{"1", "2", "3"};

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new TcpServe()).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestory = true;
    }

    private class TcpServe implements Runnable {
        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(8080);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            while (!isDestory) {
                //接收客户端请求
                try {
                    Socket client = serverSocket.accept();
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                responseClient(client);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }

    /**
     * 接收客户端信息
     *
     * @param client
     */
    private void responseClient(Socket client) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true);
        printWriter.println("欢迎来到聊天室");

        while (!isDestory) {
            String line = bufferedReader.readLine();
            LogUtils.e("--- --- TcpService " + line);
            if (line == null) break;
            int nextInt = new Random().nextInt(strings.length);
            String msg = strings[nextInt];
            printWriter.println(msg);
        }

        bufferedReader.close();
        printWriter.close();
        client.close();
    }


}

package com.xiaoniu.wifihotspotdemo.socket;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.xiaoniu.wifihotspotdemo.MainActivity;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Faioo on 2018/5/29.
 * 服务器监听线程
 */

public class serverThread extends Thread {
    private ServerSocket serverSocket = null;
    private Handler handler;
    private int port;
    private Socket socket;

    public serverThread(int port, Handler handler){
        setName("serverThread");
        this.port = port;
        this.handler = handler;
        try {
            serverSocket=new ServerSocket(port);//监听本机的12345端口
            GetIpAddress.getLocalIpAddress(serverSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true){
            try {
                Log.w("AAA","阻塞");
                //阻塞，等待设备连接
                socket = serverSocket.accept();
                Message message = Message.obtain();
                message.what = MainActivity.DEVICE_CONNECTING;
                handler.sendMessage(message);
            } catch (IOException e) {
                Log.w("AAA","error:"+e.getMessage());
                e.printStackTrace();
            }
        }
    }


    public Socket getSocket() {
        return socket;
    }

}


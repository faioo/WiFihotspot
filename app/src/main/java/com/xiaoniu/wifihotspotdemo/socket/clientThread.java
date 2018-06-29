package com.xiaoniu.wifihotspotdemo.socket;

import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.xiaoniu.wifihotspotdemo.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Faioo on 2018/5/29.
 * 客户端连接线程
 */

public class clientThread extends Thread{
    private Socket clientSocket = null;
    private Handler handler;
    private InputStream inputStream;
    private OutputStream outputStream;

    public clientThread(Socket socket, Handler handler){
        setName("ConnectThread");
        Log.w("AAA","ConnectThread");
        this.clientSocket = socket;
        this.handler = handler;
    }

    //获取服务器主机IP地址
    public String getServerIP(WifiManager wifiManager)
    {
        DhcpInfo dhcpinfo = wifiManager.getDhcpInfo();
        String serverAddress = intToIp(dhcpinfo.serverAddress);
        return serverAddress;
    }

    public String intToIp(int address)
    {
        String ip = null;

        return ip;
    }

    @Override
    public void run() {
/*        if(activeConnect){
//            socket.c
        }*/
        if(clientSocket==null){
            return;
        }
        handler.sendEmptyMessage(MainActivity.DEVICE_CONNECTED);
        try {
            //获取数据流
            inputStream = clientSocket.getInputStream();
            outputStream = clientSocket.getOutputStream();

            byte[] buffer = new byte[1024];
            int bytes;
            while (true){
                //读取数据
                bytes = inputStream.read(buffer);
                if (bytes > 0) {
                    final byte[] data = new byte[bytes];
                    System.arraycopy(buffer, 0, data, 0, bytes);

                    Message message = Message.obtain();
                    message.what = MainActivity.GET_MSG;
                    Bundle bundle = new Bundle();
                    bundle.putString("MSG",new String(data));
                    message.setData(bundle);
                    handler.sendMessage(message);

                    Log.w("AAA","读取到数据:"+new String(data));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送数据
     */
    public void sendData(String msg){
        Log.w("AAA","发送数据:"+(outputStream==null));
        if(outputStream!=null){
            try {
                outputStream.write(msg.getBytes());
                Log.w("AAA","发送消息："+msg);
                Message message = Message.obtain();
                message.what = MainActivity.SEND_MSG_SUCCSEE;
                Bundle bundle = new Bundle();
                bundle.putString("MSG",new String(msg));
                message.setData(bundle);
                handler.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
                Message message = Message.obtain();
                message.what = MainActivity.SEND_MSG_ERROR;
                Bundle bundle = new Bundle();
                bundle.putString("MSG",new String(msg));
                message.setData(bundle);
                handler.sendMessage(message);
            }
        }
    }
}

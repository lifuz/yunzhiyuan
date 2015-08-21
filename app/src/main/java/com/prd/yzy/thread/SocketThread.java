package com.prd.yzy.thread;

import android.util.Log;

import com.prd.yzy.service.TraceAgentService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * 接收socket通讯的消息
 *
 * Created by 李富 on 2015/7/24.
 */
public class SocketThread extends Thread {

    private BufferedReader br;
    private DatagramSocket ds;
    private InetAddress inetAddress;
    private Socket s;
    public static Map<String, String> loginInfo;
    private boolean flag = true;

    public SocketThread(DatagramSocket ds, Socket s) {
        this.ds = ds;
        this.s = s;
        try {
            inetAddress = InetAddress.getByName("121.40.199.67"); // 需要发送的地址
        } catch (UnknownHostException e) {
            System.out.println("Cannot open findhost!");
            System.exit(1);
        }

        try {
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        loginInfo = new HashMap<>();

    }

    @Override
    public void run() {


        while (TraceAgentService.flag){

            try {
                //按行读取消息
                String line = br.readLine();

                //处理空行
                if (line == null) {
                    break;
                }
                if(line.equals("")) {
                    continue;
                }

                //处理接收到的数据
                StringTokenizer st =  new StringTokenizer(line, " \t", false);
                String key = st.nextToken();
                if (key.equals("version")) {
                    st.nextToken();
                    loginInfo.put(key, st.nextToken());
                } else if (key.equals("key")) {
                    String val = st.nextToken();
                    loginInfo.put(key, val);
                    byte[] buf = val.getBytes();
                    // 打包到DatagramPacket类型中（DatagramSocket的send()方法接受此类，注意7211是接受地址的端口，不同于自己的端口！
                    DatagramPacket dp = new DatagramPacket(buf,
                            buf.length, inetAddress, 7211);
                    try {
                        ds.send(dp); // 发送数据
                    } catch (IOException e) {
                    }

                } else {

                    String val = st.nextToken();
                    loginInfo.put(key, val);

                }

            } catch (Exception e) {
                e.printStackTrace();

            }

        }
        Log.i("tag","socket通道关闭");
    }
}

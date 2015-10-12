package com.prd.testtv.thread;

import android.util.Log;

import com.prd.testtv.service.TraceAgentService;

import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 心跳机制
 *
 * Created by 李富 on 2015/7/27.
 */
public class HeartBeatThread extends Thread {

    private PrintStream ps;
    private InetAddress destination;
    private DatagramSocket ds;

    public static boolean hbFlag = false;

    public HeartBeatThread(PrintStream ps, DatagramSocket ds) {
        this.ps = ps;
        this.ds = ds;

        try {
            destination = InetAddress.getByName("121.40.199.67"); // 需要发送的地址
        } catch (UnknownHostException e) {
            System.out.println("Cannot open findhost!");
            System.exit(1);
        }

    }

    /**
     * 定时向服务器发送固定的消息，已确认数据链路的畅通
     */

    @Override
    public void run() {

        while (TraceAgentService.flag) {


            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ps.print("cmd Noop\n\n");

            String val = SocketThread.loginInfo.get("key");

            if (val == null){
                continue;
            }
            byte[] buf = val.getBytes();
            // 打包到DatagramPacket类型中（DatagramSocket的send()方法接受此类，注意7211是接受地址的端口，不同于自己的端口！
            DatagramPacket dp = new DatagramPacket(buf,
                    buf.length, destination, 7211);
            try {
                ds.send(dp); // 发送数据
                hbFlag = true;

            } catch (IOException e) {
            }

        }

        Log.i("tag","关闭心跳");
        hbFlag = false;
    }
}

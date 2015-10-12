package com.prd.testtv.thread;

import android.util.Log;

import com.prd.testtv.service.TraceAgentService;

import org.simple.eventbus.EventBus;

import java.io.IOException;
import java.net.DatagramPacket;

/**
 * 作者：李富 on 2015/9/9.
 * 邮箱：lifuzz@163.com
 */
public class UdpThread extends Thread {

    @Override
    public void run() {

        Log.i("tag","开启udp通道");
        EventBus.getDefault().register(this);

        //定义一次接收的数据的长度
        byte[] buf = new byte[4096];
        //将接收的数据打包到这个对象
        DatagramPacket dp = new DatagramPacket(buf, buf.length);


        //循环等待接收数据
        while (TraceAgentService.flag) {
            //设置包的长度
            dp.setLength(buf.length);
            try {
                //将程序挂起，等待数据包，并将接收到的数据打包到的dp对象中
                TraceAgentService.ds.receive(dp);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //将接收到的数据包，转换成字符串
            String str = new String(dp.getData(), 0, dp.getLength());

            EventBus.getDefault().post(str,"udp");


        }

        Log.i("tag","udp通道关闭");
        EventBus.getDefault().unregister(this);
    }
}

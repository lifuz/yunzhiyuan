package com.prd.yzy.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.prd.yzy.thread.HeartBeatThread;
import com.prd.yzy.thread.SocketThread;
import com.prd.yzy.utils.Base64;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramSocket;
import java.net.Socket;

/**
 * 跟TraceAgent服务器交互，
 * 执行点名，订阅等一系列服务。
 * <p/>
 * 作者：李富 on 2015/8/13
 * 邮箱：lifuzz@163.com
 */
public class TraceAgentService extends Service {

    private Socket s;
    public static DatagramSocket ds;

    private PrintStream ps;

    public static boolean isRunning = false;

    SharedPreferences share;

    public static Thread socket;

    public static boolean flag = false;

    private static final int TIMER = 1 * 60 * 1000;

    private boolean timetask = false;

    public TraceAgentService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 将对象注册到事件总线中， ****** 注意要在onDestory中进行注销 ****
        EventBus.getDefault().register(this);

        isRunning = true;


        Log.i("tag", "服务开启");

        socket = new Thread("lifuz") {
            @Override
            public void run() {
                try {

                    //与121.40.199.67的服务器建立连接
                    s = new Socket("121.40.199.67", 7210);
                    share = getSharedPreferences("login", Activity.MODE_PRIVATE);
                    String opid = share.getString("opid", "");
                    String pass = share.getString("password", "");
                    //对密码进行编码
                    String enStr = new String(Base64.encode(pass.getBytes()));



                    //设置udp通讯的端口号
                    ds = new DatagramSocket(65411);

                    //获取socket链路的输出流
                    ps = new PrintStream(s.getOutputStream());
                    //向服务器发送登录信息
                    ps.print("cmd Auth\nuserid " + opid + "\npasswd " + enStr + "\n\n");
                    ps.flush();

                    flag = true;

                    //开启socket接收通道的线程
                    new SocketThread(ds, s).start();
                    //开启心跳机制，即定时向服务器发送固定的消息，以确认通道的畅通性
                    new HeartBeatThread(ps, ds).start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        socket.start();


    }

    //事件处理
    @Subscriber(tag = "timeTask")
    private void timeTask(String msg) {

        Log.i("tag", "有数据吗");
        if ("关闭定时器".equals(msg)) {
            if(timetask) {

                timetask = false;
                Log.i("tag","关闭定时器");
            }
        } else if("开启定时器".equals(msg)){

            if (!timetask) {

                timetask = true;

                new Thread(runnable).start();
                Log.i("tag","开启定时器");
            }

        }

    }


    /**
     * 定时器
     */
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what ==0x123) {
                stopService(new Intent(TraceAgentService.this,TraceAgentService.class));
                timetask = false;
            }
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            int count = 0;
            while(timetask){
                try {
                    Thread.sleep(1000);
                    count = count + 1;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(count >= 60) {
                    handler.sendEmptyMessage(0x123);
                }


            }
        }
    };

    //事件处理
    @Subscriber(tag = "event")
    private void event(String msg) {
        ps.print(msg);
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // ****** 不要忘了进行注销 ****



//        Log.i("tag", "服务关闭");
        ps.print("cmd Quit\n\n");
        try {
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ds.close();
        flag = false;
        EventBus.getDefault().unregister(this);
        isRunning = false;
        timetask = false;
//        handler.removeCallbacks(runnable);
        Log.i("tag", "服务关闭");


    }
}

package com.prd.yzy.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

/**
 * 监控系统事件，当屏幕被关闭或者程序进入后台
 * 则关闭TraceAgentService服务
 * <p/>
 * 作者：李富 on 2015/8/13
 * 邮箱：lifuzz@163.com
 */
public class MonitorService extends Service {

    private static final int TIMER = 5 * 60 * 1000;

    private static boolean flag = false;

    public MonitorService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 将对象注册到事件总线中， ****** 注意要在onDestory中进行注销 ****
        EventBus.getDefault().register(this);

        //定义一个意图过滤对象
        final IntentFilter filter = new IntentFilter();
        //屏幕灭屏广播
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        // 当长按电源键弹出“关机”对话或者锁屏时系统会发出这个广播
        // example：有时候会用到系统对话框，权限可能很高，会覆盖在锁屏界面或者“关机”对话框之上，
        // 所以监听这个广播，当收到时就隐藏自己的对话，如点击pad右下角部分弹出的对话框
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

        final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();

                if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                    Log.i("Service", "灭屏");
                    if (!flag) {
                        handler.postDelayed(runnable, TIMER);
                        flag = true;
                    }


                } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
                    Log.i("Service", "boss");

                    if (!flag) {
                        handler.postDelayed(runnable, TIMER);
                        flag = true;
                    }
                }


            }
        };

        registerReceiver(broadcastReceiver, filter);

    }

    //事件处理
    @Subscriber(tag = "timeTask")
    private void timeTask(String msg) {

        Log.i("tag","有数据吗");
        if ("关闭定时器".equals(msg)) {
            if(flag) {
                handler.removeCallbacks(runnable);
                flag = false;
                Log.i("tag","关闭定时器");
            }
        }

    }

    Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, TIMER);
            if (TraceAgentService.isRunning) {


                stopService(new Intent(MonitorService.this, TraceAgentService.class));
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        // ****** 不要忘了进行注销 ****
        EventBus.getDefault().unregister(this);
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}

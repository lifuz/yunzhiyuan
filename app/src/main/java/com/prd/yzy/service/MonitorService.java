package com.prd.yzy.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

/**
 * 监控系统事件，当屏幕被关闭或者程序进入后台
 * 则关闭TraceAgentService服务
 *
 * 作者：李富 on 2015/8/13
 * 邮箱：lifuzz@163.com
 */
public class MonitorService extends Service {
    public MonitorService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

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

                if(Intent.ACTION_SCREEN_OFF.equals(action)) {
                    Log.i("Service", "灭屏");
                    Log.i("Service", TraceAgentService.isRunning + "");
                    if (TraceAgentService.isRunning) {
                        Log.i("tag","jinlai");

                        stopService(new Intent(MonitorService.this, TraceAgentService.class));
                    }

                }  else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
                    Log.i("Service","boss");


                    if (TraceAgentService.isRunning) {


                        stopService(new Intent(MonitorService.this,TraceAgentService.class));
                    }
                }



            }
        };

        registerReceiver(broadcastReceiver,filter);

    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}

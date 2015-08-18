package com.prd.yzy.application;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.prd.yzy.service.TraceAgentService;

import org.simple.eventbus.EventBus;

/**
 * Created by 李富 on 2015/8/18.
 */
public class PRDAplication extends Application {

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

                    if(TraceAgentService.isRunning) {
                        EventBus.getDefault().post("开启定时器", "timeTask");
                    }



                } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
                    Log.i("Service", "boss");

                    if(TraceAgentService.isRunning) {
                        EventBus.getDefault().post("开启定时器", "timeTask");
                    }
                }


            }
        };

        registerReceiver(broadcastReceiver, filter);
    }
}

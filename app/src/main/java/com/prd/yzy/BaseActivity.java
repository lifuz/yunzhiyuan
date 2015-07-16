package com.prd.yzy;

import android.app.Activity;
import android.os.Bundle;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

/**
 * Created by 李富 on 2015/7/8.
 */
public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 将对象注册到事件总线中， ****** 注意要在onDestory中进行注销 ****
        EventBus.getDefault().register(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // ****** 不要忘了进行注销 ****
        EventBus.getDefault().unregister(this);
    }

    //订阅事件
    @Subscriber(tag = "csuicide")
    private void csuicideMyself(String msg) {
        finish();
    }

}
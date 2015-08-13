package com.prd.yzy.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.PrintStream;
import java.net.DatagramSocket;
import java.net.Socket;

public class TraceAgentService extends Service {

    private Socket s;
    private DatagramSocket ds;

    private PrintStream ps;

    public TraceAgentService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();



    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}

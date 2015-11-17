package com.prd.yzy;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.prd.yzy.service.TraceAgentService;
import com.prd.yzy.thread.HeartBeatThread;
import com.prd.yzy.thread.SocketThread;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 作者：李富 on 2015/9/28.
 * 邮箱：lifuzz@163.com
 */
public class JPAcitivity extends BaseActivity implements View.OnClickListener {

    private BarChart barChart;
    private Typeface mtf;
    private BarData data;

    private String mac;

    private String preStr = "";

    private String preData;

    private int barVal = 0;

    private int count = 0;
    private TextView car_tv;
    private ImageView car_btn;

    private boolean jdq_flag = false;

    List<Map<String, Long>> xyValue = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.jp_layout);

        // 将对象注册到事件总线中， ****** 注意要在onDestory中进行注销 ****
        EventBus.getDefault().register(this);

        car_tv = (TextView) findViewById(R.id.car_zt_tv);
        car_btn = (ImageView) findViewById(R.id.car_zt_btn);
        car_btn.setOnClickListener(this);

        Intent it = getIntent();

        mac = it.getStringExtra("mac");

        String str = "cmd Retr\n" +
                "mac " + mac + "\n" +
                "app " + (count + 1) + "\n" +
                "\n" +
                "cmd Ctlm\n" +
                "mac " + mac + "\n" +
                "optcode 2\n" +
                "optargs 0\n" +
                "app " + (count + 1) + "\n\n";

//                            Thread.sleep(10000);
        EventBus.getDefault().post(str, "event");

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.car_zt_btn:
                Log.i("tag", "dianjile");

                String str = "";

                if (jdq_flag) {
                    str = "cmd Ctlm\n" +
                            "mac " + mac + "\n" +
                            "optcode 73\n" +
                            "optargs 2;1;1;B4;0;1;1;0;0;0;0\n" +
                            "app " + count + "\n\n";
                } else {
                    str = "cmd Ctlm\n" +
                            "mac " + mac + "\n" +
                            "optcode 73\n" +
                            "optargs 1;1;1;B4;0;1;1;0;0;0;0\n" +
                            "app " + count + "\n\n";
                }

                EventBus.getDefault().post(str, "event");

                break;
        }

    }


    /**
     * socket通讯
     * 当页面被显示出来就就建立socket连接。
     */
    @Override
    protected void onResume() {
        super.onResume();



        if (TraceAgentService.isRunning) {

        } else {
            Log.i("tag", "开启服务");
            startService(new Intent(JPAcitivity.this, TraceAgentService.class));
        }


        new Thread() {

            @Override
            public void run() {
                try {

                    //对设备下发点名指令
                    //控制循环条件
                    boolean bn = true;
                    while (bn) {

                        if (TraceAgentService.socket == null) {
                            continue;
                        }


                        if (!HeartBeatThread.hbFlag) {
                            continue;
                        }

                        //判断条件，如果car对象不无空，Mac的属性已经被赋值，和数据通道是否建立
                        if (SocketThread.loginInfo.containsKey("key")
                                && !SocketThread.loginInfo.get("key").equals("")) {
                            String str = "cmd Retr\n" +
                                    "mac " + mac+ "\n" +
                                    "app " + (count + 1) + "\n" +
                                    "\n" +
                                    "cmd Ctlm\n" +
                                    "mac " + mac + "\n" +
                                    "optcode 2\n" +
                                    "optargs 0\n" +
                                    "app " + (count + 1) + "\n\n";

                            EventBus.getDefault().post(str, "event");

                            Log.i("tag", "jinqule");
                            bn = false;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.i("tag","udp链路断开");

            }
        }.start();


    }

    /**
     * 给图表加载数据
     */
    private void setData() {
        //设置x轴的标签

//        for (Chart chart : charts) {
//            xVals.add(chart.getDate());
//        }
        ArrayList<String> xVals = new ArrayList<>();
        //设置y轴的数据
        ArrayList<BarEntry> yVals = new ArrayList<>();
//        for (int i = 0; i < charts.size(); i++) {
//            float f = Float.parseFloat(charts.get(i).getCount());
//            yVals.add(new BarEntry(f, i));
//        }

//        for (int i = 0 ; i< longs.size();i++) {
//
////            xVals.add((i + 1) + "");
////            yVals.add(new B);
//
//        }

        //设置数据的条目，并命名
        BarDataSet set = new BarDataSet(yVals, "统计");

        //设置条柱之间的距离
        set.setBarSpacePercent(25f);

        //设置有多少类型
        ArrayList<BarDataSet> barDataSets = new ArrayList<>();
        barDataSets.add(set);

        //集合成完整的柱状图的数据
        data = new BarData(xVals, barDataSets);
//        data.re

        data.setValueTextSize(10f);
        data.setValueTypeface(mtf);

        barChart.setData(data);

        //设置标签
        Legend l = barChart.getLegend();
        //使标签不可见
        l.setEnabled(false);

//        barChart.setVisibleXRange(2f,2f);

//        barChart.setvi

        //立即显示图表
//        barChart.animateX(10);

        barChart.invalidate();

    }

    /**
     * 初始化barChart控件
     */
    private void initBarChart() {

        barChart = (BarChart) findViewById(R.id.mp_bar);
        //图标不产生阴影
        barChart.setDrawBarShadow(false);

        //柱子上是否显示数值
        barChart.setDrawValueAboveBar(true);

        //图表的描述
        barChart.setDescription("");

        //规定柱状图最多含有几个柱子
        barChart.setMaxVisibleValueCount(10);
//        barChart.setMaxVisibleValueCount(10000);

//        barChart.r

        barChart.setPinchZoom(false);

        mtf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        //设置背景
//        barChart.setBackgroundColor(getResources().getColor(R.color.mp_back));

        barChart.setDrawGridBackground(false);

        //设置没有数据的显示的文字
        barChart.setNoDataText("节拍统计表");
//
//        //设置图表能否缩放
        barChart.setScaleEnabled(true);

        barChart.setFocusable(true);

        barChart.setDragEnabled(true);

        //设置双击是否对图表进行缩放
        barChart.setDoubleTapToZoomEnabled(false);

        //设置x轴
        XAxis xAxis = barChart.getXAxis();

        //设置x轴显示的位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置x轴的字体
        xAxis.setTypeface(mtf);

//        xAxis.setLabelsToSkip(5);

//        //是否画横轴的表格
//        xAxis.setDrawGridLines(false);
//
//        //设置连个标签的间隔
//        xAxis.setSpaceBetweenLabels(2);
//
//        //设置是否画x轴的线
//        xAxis.setDrawAxisLine(false);

        //设置y轴
        //设置y轴的左轴
        YAxis left = barChart.getAxisLeft();
        //设置是否显示网格
//        left.setDrawGridLines(false);
//        //设置是否显示y轴的左轴
//        left.setDrawAxisLine(false);
//        //设置是否显示y轴左轴的标签
//        left.setDrawLabels(false);

        left.setAxisMaxValue(20f);
        LimitLine ll = new LimitLine(10f, "10s");
        ll.setLineColor(Color.RED);
        ll.setLineWidth(1f);
        ll.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll.enableDashedLine(5f, 5f, 0f);
        left.addLimitLine(ll);

        //设置y轴的右轴
        YAxis right = barChart.getAxisRight();
        //设置是否显示网格
        right.setDrawGridLines(false);
        //设置是否显示y轴的右轴
        right.setDrawAxisLine(false);
        //设置是否显示y轴右轴的标签
        right.setDrawLabels(false);


    }


    //事件处理
    @Subscriber(tag = "udp")
    private void udp(String str) {
        Log.i("tag", str);

        // 处理数据
        String[] arrStr = str.split(" ");

        Log.i("tag", "length" + arrStr.length);

        //当数组的长度为小于或等于1，则直接跳过下面的部分
        if (arrStr.length <= 1) {
            return;
        }

        str = arrStr[2];


//        Log.i("tag", car.getMac() + "   " + str);

        if (!str.equals(mac)) {
            return;
        }


        //对数据的处理
        str = arrStr[4];
        arrStr = str.split("\\|");

        if (preStr.equals("")) {
            preStr = arrStr[0];

            preData = arrStr[4];

            initBarChart();
            setData();
        } else {


            str = arrStr[4];

            if (!str.equals(preData)) {
                str = arrStr[0];
                long utc = Long.valueOf(str, 16) - Long.valueOf(preStr, 16);

                Log.i("tag", utc + "");

                barVal++;


                Map<String, Long> map = new HashMap<>();

                map.put(barVal + "", utc);

                if (barVal > 5) {
                    xyValue.remove(0);
                    xyValue.add(map);

//                    data.removeXValue(0);
//                    data.addXValue(barVal + "");
                } else {
                    xyValue.add(map);
                }

                Log.i("lifuz", xyValue.toString());

                updataBarChart(xyValue);

                map = null;

                preStr = str;
                preData = arrStr[4];
            }


        }

        str = arrStr[9];
        arrStr = str.split(";");

        if (!(Long.parseLong(arrStr[0]) == 300)) {
            car_btn.setImageResource(R.drawable.jdq1);
            jdq_flag = false;
            car_tv.setText("继电器状态:关");
        } else {
            car_btn.setImageResource(R.drawable.jdq2);
            jdq_flag = true;

            car_tv.setText("继电器状态:开");
        }


    }


    private void updataBarChart(List<Map<String, Long>> xyValue) {

        data.removeDataSet(0);

        ArrayList<String> xVals = new ArrayList<>();
        //设置y轴的数据
        ArrayList<BarEntry> yVals = new ArrayList<>();

        for (int i = 0; i < xyValue.size(); i++) {

            Map<String, Long> map = xyValue.get(i);
            Set<String> set = map.keySet();
            Iterator<String> it = set.iterator();
            String xVal = it.next();
            xVals.add(xVal);

            yVals.add(new BarEntry(map.get(xVal), i));

        }

        BarDataSet dataSet = new BarDataSet(yVals, "shuju");

        BarData barData = new BarData(xVals, dataSet);
//        data.addDataSet(dataSet);
        barChart.setData(barData);
        barChart.animateX(10);

//        barData = null;

    }

}

package com.prd.testtv;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.prd.testtv.application.PrdApplication;
import com.prd.testtv.bean.Car;
import com.prd.testtv.formatter.XValueFormatter;
import com.prd.testtv.formatter.YValueFormatter;
import com.prd.testtv.request.GJsonObjectRequest;
import com.prd.testtv.service.TraceAgentService;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 作者：李富 on 2015/9/24.
 * 邮箱：lifuzz@163.com
 */
public class CarActivity extends BaseActivity implements View.OnClickListener {

    private PrdApplication prdApplication;
    private String vid;
    private Car car;
    private int count = 0;
    private int barVal = 0;
    private TextView car_js, car_tv;

    private LinearLayout car_zt;
    private ImageView car_btn;

    private boolean jdq_flag = false;
    private BarChart barChart;
    private Typeface mtf;

    private String preStr = "";

    private String preData;

    private List<Long> longs;
    private BarData data;

    List<Map<String, Long>> xyValue = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.car_layout);

        //获取传来的数据，如果为空，则退出页面
        Intent it = getIntent();
        vid = it.getStringExtra("vid");

        Log.i("tag", vid);
        if (vid == null) {
            finish();
        }

        longs = new ArrayList<>();

        car_js = (TextView) findViewById(R.id.car_js);
        prdApplication = (PrdApplication) getApplication();


        car_zt = (LinearLayout) findViewById(R.id.car_zt);
        car_tv = (TextView) findViewById(R.id.car_zt_tv);
        car_btn = (ImageView) findViewById(R.id.car_zt_btn);
        car_btn.setOnClickListener(this);


        Map<String, String> params = new HashMap<>();
        params.put("vid", vid);

        String url = getResources().getString(R.string.url_car);

        GJsonObjectRequest<Car> gJsonObjectRequest = new GJsonObjectRequest<Car>(Request.Method.POST,
                url, params, Car.class, new Response.Listener<Car>() {
            @Override
            public void onResponse(Car cars) {

                car = cars;

                Log.i("tag", car.getAlt());

                car_js.setText(car.getAlt());

                if (TraceAgentService.isRunning) {
//                    EventBus.getDefault().post("关闭定时器", "timeTask");
                    EventBus.getDefault().post(getCmd(), "event");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                volleyError.printStackTrace();

            }
        });

        prdApplication.getQueue().add(gJsonObjectRequest);

    }

    /**
     * 给图表加载数据
     */
    private void setData() {
        //设置x轴的标签
        ArrayList<String> xVals = new ArrayList<>();
//        for (Chart chart : charts) {
//            xVals.add(chart.getDate());
//        }

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

        data.setValueTextSize(10f);
        data.setValueTypeface(mtf);


        barChart.setData(data);

        //设置标签
        Legend l = barChart.getLegend();
        //使标签不可见
        l.setEnabled(false);

        //立即显示图表
        barChart.animateX(10);


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
        barChart.setMaxVisibleValueCount(100000);

//        barChart.r

        barChart.setPinchZoom(false);

        mtf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        //设置背景
//        barChart.setBackgroundColor(getResources().getColor(R.color.mp_back));

        barChart.setDrawGridBackground(false);

        //设置没有数据的显示的文字
        barChart.setNoDataText("");
//
//        //设置图表能否缩放
        barChart.setScaleEnabled(false);

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

        //是否画横轴的表格
        xAxis.setDrawGridLines(false);

        //设置连个标签的间隔
        xAxis.setSpaceBetweenLabels(2);

        xAxis.setValueFormatter(new XValueFormatter());
//        //设置是否画x轴的线
//        xAxis.setDrawAxisLine(false);

        //设置y轴
        //设置y轴的左轴
        YAxis left = barChart.getAxisLeft();
        //设置是否显示网格
        left.setDrawGridLines(false);
//        //设置是否显示y轴的左轴
//        left.setDrawAxisLine(false);
//        //设置是否显示y轴左轴的标签
//        left.setDrawLabels(false);

        left.setAxisMaxValue(20f);
        LimitLine ll = new LimitLine(10f, "10s");
        ll.setLineColor(Color.RED);
        ll.setLineWidth(1f);
        ll.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
//        ll.enableDashedLine(5f,5f,5f);
        ll.enableDashedLine(5f, 5f, 0f);
        left.addLimitLine(ll);
        left.setValueFormatter(new YValueFormatter());
//        left.setValueFormatter(xValue);

        //设置y轴的右轴
        YAxis right = barChart.getAxisRight();
        //设置是否显示网格
        right.setDrawGridLines(false);
        //设置是否显示y轴的右轴
        right.setDrawAxisLine(false);
        //设置是否显示y轴右轴的标签
        right.setDrawLabels(false);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.car_zt_btn:
                Log.i("tag", "dianjile");

                String str = "";

                if (jdq_flag) {
                    str = "cmd Ctlm\n" +
                            "mac " + car.getMac() + "\n" +
                            "optcode 73\n" +
                            "optargs 2;1;1;B4;0;1;1;0;0;0;0\n" +
                            "app " + count + "\n\n";
                } else {
                    str = "cmd Ctlm\n" +
                            "mac " + car.getMac() + "\n" +
                            "optcode 73\n" +
                            "optargs 1;1;1;B4;0;1;1;0;0;0;0\n" +
                            "app " + count + "\n\n";
                }

                EventBus.getDefault().post(str, "event");

                break;

        }
    }

    //    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//
//            if (msg.what == 0x123) {
//                objectToList(car);
//            }
//
//        }
//    };

    //事件处理
    @Subscriber(tag = "udp")
    private void udp(String str) {
        Log.i("tag", str);

        //处理数据
        String[] arrStr = str.split(" ");

        Log.i("tag", "length" + arrStr.length);

        //当数组的长度为小于或等于1，则直接跳过下面的部分
        if (arrStr.length <= 1) {
            return;
        }

        str = arrStr[2];


//        Log.i("tag", car.getMac() + "   " + str);

        if (!str.equals(car.getMac())) {
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

                if (barVal > 20) {
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


            car_js.setText(Long.parseLong(arrStr[4], 16) + "");

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


//        ztFlag = false;
//
//        handler.sendEmptyMessage(0x123);
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


    public String getCmd() {


        String cmdStr = "";

        cmdStr = "cmd Retr\n" +
                "mac " + car.getMac() + "\n" +
                "app " + (count + 1) + "\n" +
                "\n" +
                "cmd Ctlm\n" +
                "mac " + car.getMac() + "\n" +
                "optcode 2\n" +
                "optargs 0\n" +
                "app " + (count + 1) + "\n\n";

        return cmdStr;
    }

    //事件处理
    @Subscriber(tag = "cmd")
    private void cmd(String str) {


        Log.i("tag","发送消息");

        new Thread(){

            @Override
            public void run() {
                boolean flag = true;

                while (flag){
                    if (car != null) {
                        EventBus.getDefault().post(getCmd(), "event");
                        flag = false;
                    }

                }

            }
        }.start();

//
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (TraceAgentService.isRunning) {
            EventBus.getDefault().post("关闭定时器", "timeTask");
        } else {
            Log.i("tag", "开启服务");
            startService(new Intent(CarActivity.this, TraceAgentService.class));
        }

    }

    /**
     * 当页面的生命进程到onPause阶段，注销socket通讯
     */
    @Override
    protected void onPause() {
        super.onPause();

        if (car != null) {
            String str = "cmd Abor\n" +
                    "mac " + car.getMac() + "\n" +
                    "app " + (count + 1) + "\n" +
                    "\n\n";
            EventBus.getDefault().post(str, "event");
        }
    }
}

package com.prd.yzy;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.prd.yzy.bean.Chart;
import com.prd.yzy.bean.Work;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 实现图表和详情
 *
 * 作者：李富 on 2015/9/1.
 * 邮箱：lifuzz@163.com
 */
public class BarChartInfo extends Activity implements View.OnClickListener,
        OnChartValueSelectedListener {

    private TextView prd_back_title, prd_title;

    private LinearLayout prd_back;

    private AsyncHttpClient client;

    private BarChart barChart;
    private Typeface mtf;
    private List<Chart> charts;
    private List<Work> works;
    private MyAdapter adapter;

    private ListView mp_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mpa_barchart);

        initView();
        initBarChart();

        initRequest();

    }

    private void initRequest() {

        client = new AsyncHttpClient();
        client.post(getResources().getString(R.string.count),
                new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                        Chart chart = null;

                        charts = new ArrayList<Chart>();

                        for (int i = 0; i < response.length(); i++) {

                            chart = new Chart();
                            try {
                                JSONObject object = response.getJSONObject(i);

                                chart.setDate(object.getString("date"));
                                chart.setCount(object.getString("count"));
                                charts.add(chart);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        Collections.reverse(charts);
                        Log.d("tag", charts.toString());
                        dealEvent();

                    }
                });

    }

    private void dealEvent() {

        setData();

        works = new ArrayList();
        Work work = null;
        for (int i = 0; i < 5; i++) {

            work = new Work();
            work.setName("张" + (i + 1));
            work.setBjName("部件" + (i + 1));
            work.setCount(1000 + i + "");
            work.setPass(950 + i + "");
            works.add(work);

        }

        adapter = new MyAdapter(getApplicationContext(),works);
        mp_list.setAdapter(adapter);


    }

    /**
     * 给图表加载数据
     */
    private void setData() {
        //设置x轴的标签
        ArrayList<String> xVals = new ArrayList<>();
        for (Chart chart : charts) {
            xVals.add(chart.getDate());
        }

        //设置y轴的数据
        ArrayList<BarEntry> yVals = new ArrayList<>();
        for (int i = 0; i < charts.size(); i++) {
            float f = Float.parseFloat(charts.get(i).getCount());
            yVals.add(new BarEntry(f, i));
        }

        //设置数据的条目，并命名
        BarDataSet set = new BarDataSet(yVals, "统计");

        //设置条柱之间的距离
        set.setBarSpacePercent(25f);

        //设置有多少类型
        ArrayList<BarDataSet> barDataSets = new ArrayList<>();
        barDataSets.add(set);

        //集合成完整的柱状图的数据
        BarData data = new BarData(xVals, barDataSets);

        data.setValueTextSize(10f);
        data.setValueTypeface(mtf);

        barChart.setData(data);

        //设置标签
        Legend l = barChart.getLegend();
        //使标签不可见
        l.setEnabled(false);

        //立即显示图表
        barChart.animateX(1000);


    }


    /**
     * 初始化barChart控件
     */
    private void initBarChart() {

        barChart = (BarChart) findViewById(R.id.mpa_bar);
        //图标不产生阴影
        barChart.setDrawBarShadow(false);

        //柱子上是否显示数值
        barChart.setDrawValueAboveBar(true);

        //图表的描述
        barChart.setDescription("");

        //规定柱状图最多含有几个柱子
        barChart.setMaxVisibleValueCount(10);

        barChart.setPinchZoom(false);

        mtf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        //设置背景
        barChart.setBackgroundColor(getResources().getColor(R.color.mp_back));

        barChart.setDrawGridBackground(false);

        //设置没有数据的显示的文字
        barChart.setNoDataText("");

        //设置图表能否缩放
        barChart.setScaleEnabled(false);

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

        //设置是否画x轴的线
        xAxis.setDrawAxisLine(false);

        //设置y轴
        //设置y轴的左轴
        YAxis left = barChart.getAxisLeft();
        //设置是否显示网格
        left.setDrawGridLines(false);
        //设置是否显示y轴的左轴
        left.setDrawAxisLine(false);
        //设置是否显示y轴左轴的标签
        left.setDrawLabels(false);

        //设置y轴的右轴
        YAxis right = barChart.getAxisRight();
        //设置是否显示网格
        right.setDrawGridLines(false);
        //设置是否显示y轴的右轴
        right.setDrawAxisLine(false);
        //设置是否显示y轴右轴的标签
        right.setDrawLabels(false);


    }

    private void initView() {

        prd_back_title = (TextView) findViewById(R.id.prd_back_title);
        prd_back_title.setText("返回");

        prd_title = (TextView) findViewById(R.id.prd_title);
        prd_title.setText("工作信息");

        prd_back = (LinearLayout) findViewById(R.id.prd_back);
        prd_back.setOnClickListener(this);

        mp_list = (ListView) findViewById(R.id.mp_list);

    }

    @Override
    public void onValueSelected(Entry entry, int i, Highlight highlight) {

        if (entry == null) return;


    }

    @Override
    public void onNothingSelected() {



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.prd_back:

                finish();
                break;


        }
    }

    class MyAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private List<Work> works;

        public MyAdapter(Context context, List<Work> works) {

            inflater = LayoutInflater.from(context);
            this.works = works;

        }

        class WorkItem {
            private TextView name;
            private TextView bj_name;
            private TextView count;
            private TextView pass;

        }


        @Override
        public int getCount() {
            return works.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            WorkItem workItem = null;

            if (convertView == null) {

                workItem = new WorkItem();
                convertView = inflater.inflate(R.layout.mp_item, null);

                workItem.name = (TextView) convertView.findViewById(R.id.mp_name);
                workItem.bj_name = (TextView) convertView.findViewById(R.id.mp_bj);
                workItem.count = (TextView) convertView.findViewById(R.id.mp_count);
                workItem.pass = (TextView) convertView.findViewById(R.id.mp_pass);

                convertView.setTag(workItem);

            } else {
                workItem = (WorkItem) convertView.getTag();
            }

            Work work = works.get(position);

            workItem.name.setText(work.getName());
            workItem.bj_name.setText(work.getBjName());
            workItem.count.setText(work.getCount());
            workItem.pass.setText(work.getPass());

            return convertView;
        }
    }
}

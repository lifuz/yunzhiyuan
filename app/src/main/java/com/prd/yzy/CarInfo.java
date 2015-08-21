package com.prd.yzy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.prd.yzy.bean.Car;
import com.prd.yzy.service.TraceAgentService;
import com.prd.yzy.thread.HeartBeatThread;
import com.prd.yzy.thread.SocketThread;
import com.prd.yzy.utils.HttpUrls;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 李富 on 2015/7/9.
 */
public class CarInfo extends BaseActivity implements View.OnClickListener {

    private String location;
    private String vid;

    private GeoCoder gc;

    private View car_back, car_flush;
    private TextView car_name;

    private AsyncHttpClient client;
    private RequestParams params;

    private Car car;

    private List<Map<String, Object>> listItems;

    private ListView car_list;

    private Button car_dm;


    public static boolean flag = true;
    private boolean ztFlag = true;

    private static int count = 0;

    private CarAdapter adapter;
    private SharedPreferences share;

    public static int licount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.car_layout);



        Log.i("tag","onCreate");

        //初始化组件
        initViews();

        //获取传来的数据，如果为空，则退出页面
        Intent it = getIntent();
        vid = it.getStringExtra("vid");
        if (vid == null) {
            finish();
        }

        //设置参数
        params.put("vid", vid);
        //访问服务器获取数据
        client.get(HttpUrls.http_car, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                //把获取到的数据封装成car对象
                car = new Car();
                try {

                    car.setVid(response.getString("vid"));
                    car.setName(response.getString("name"));
                    //设置标头
                    car_name.setText(car.getName());
                    if (response.has("companyName")) {
                        car.setCompanyName(response.getString("companyName"));
                    }

                    if (response.has("driverName")) {
                        car.setDriverName(response.getString("driverName"));
                    }
                    if (response.has("alt")) {
                        car.setSpeed(response.getString("alt"));
                    }
                    if (response.has("lat")) {
                        car.setLat(response.getString("lat"));
                    }
                    if (response.has("lon")) {
                        car.setLon(response.getString("lon"));
                    }

                    if (response.has("miles")) {
                        car.setMiles(response.getString("miles"));
                    }

                    if (response.has("times")) {
                        car.setTimes(response.getString("times"));
                    }

                    if (response.has("beforeMiles")) {
                        car.setBeforeMiles(response.getString("beforeMiles"));
                    }

                    if (response.has("beforeTimes")) {
                        car.setBeforeTimes(response.getString("beforeTimes"));
                    }

//                    car.setUtc(response.getString("utc"));

                    car.setMac(response.getString("mac"));

                    Log.i("tag", car.getMac());

                    ztFlag = true;

                    //进行反地理编码
                    initAddress();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


    }

    /**
     * 进行反地理编码
     */
    public void initAddress() {

        if(gc == null) {
            gc = GeoCoder.newInstance();
        }

        //设置经纬度的对象
        LatLng ll = new LatLng(Double.parseDouble(car.getLat()), Double.parseDouble(car.getLon()));


        //设置查询结果监听者
        gc.setOnGetGeoCodeResultListener(new MyOnGetGeoCoderResultListener());

        //对给定经纬度进行反地理编码
        gc.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
    }

    /**
     * 初始化组件
     */
    public void initViews() {

        car_back = findViewById(R.id.car_back);
        car_back.setOnClickListener(this);

        car_name = (TextView) findViewById(R.id.car_name);

        car_list = (ListView) findViewById(R.id.car_list);

        car_dm = (Button) findViewById(R.id.car_dm);
        car_dm.setOnClickListener(this);
//        car_dm.setVisibility(View.GONE);

        //构建参数
        listItems = new ArrayList<Map<String, Object>>();

        client = new AsyncHttpClient();
        params = new RequestParams();

        //初始化地理编码查询接口
        gc = GeoCoder.newInstance();
        share = getSharedPreferences("login", Activity.MODE_PRIVATE);


    }

    /**
     * 处理点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //设置点击返回时，处理过程
            case R.id.car_back:
                finish();
                break;
            case R.id.car_dm:
//                ps.print();
                break;
        }

    }


    /**
     * 构建适配器的参数和创建适配器
     *
     * @param car
     */
    public void objectToList(Car car) {


        Log.i("tag", "适配器" + car.getAddress() + car.getSpeed());

//        List<Map<String,Object>> mapList = new ArrayList<>();

        listItems.clear();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("img", R.drawable.carinfo_ico_speed);
        map.put("title", "速度");
        map.put("info", car.getSpeed() + " Km/h");
        Log.i("tag", car.getSpeed());
//        mapList.add(map);
        listItems.add(map);

        map = new HashMap<String, Object>();
        map.put("img", R.drawable.carinfo_ico_position);
        map.put("title", "位置");
        map.put("info", car.getAddress());
        listItems.add(map);
//        mapList.add(map);

        map = new HashMap<String, Object>();
        map.put("img", R.drawable.carinfo_ico_company);
        map.put("title", "所属公司");
        map.put("info", car.getCompanyName());
        listItems.add(map);
//        mapList.add(map);

        map = new HashMap<String, Object>();
        map.put("img", R.drawable.carinfo_ico_attribute);
        map.put("title", "运营属性");
        map.put("info", "-");
        listItems.add(map);
//        mapList.add(map);

        map = new HashMap<String, Object>();
        map.put("img", R.drawable.carinfo_ico_driver);
        map.put("title", "当前司机");
        map.put("info", car.getDriverName());
        listItems.add(map);
//        mapList.add(map);

        map = new HashMap<String, Object>();
        map.put("img", R.drawable.carinfo_ico_miles);
        map.put("title", "本月运行里程");
        map.put("info", car.getMiles() + "公里");
        listItems.add(map);
//        mapList.add(map);

        map = new HashMap<String, Object>();
        map.put("img", R.drawable.carinfo_ico_time);
        map.put("title", "本月运行时间");
        map.put("info", car.getTimes());
        listItems.add(map);
//        mapList.add(map);

        map = new HashMap<String, Object>();
        map.put("img", R.drawable.carinfo_ico_miles);
        map.put("title", "上月运行里程");
        map.put("info", car.getBeforeMiles() + "公里");
        listItems.add(map);
//        mapList.add(map);

        map = new HashMap<String, Object>();
        map.put("img", R.drawable.carinfo_ico_time);
        map.put("title", "上月运行时间");
        map.put("info", car.getBeforeTimes());
        listItems.add(map);
//        mapList.add(map);


        if (ztFlag) {

            if (car_list == null) {
                car_list = (ListView) findViewById(R.id.car_list);
            }

            adapter = new CarAdapter(listItems,this);


            Log.i("tag","到这里了");

            //给ListView添加适配器
            car_list.setAdapter(adapter);
        } else {

            if(adapter !=null) {
                adapter.notifyDataSetChanged();
            } else {
                adapter = new CarAdapter(listItems,this);


                Log.i("tag","到这里了");


                if (car_list == null) {
                    car_list = (ListView) findViewById(R.id.car_list);
                }

                //给ListView添加适配器
                car_list.setAdapter(adapter);
            }


//            listItems.clear();
//            listItems.addAll(mapList);
//
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

//            handler.sendEmptyMessage(1);
        }




    }

//    Handler handler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//
//            if (msg.what == 1) {
//                Log.i("tag","刷新了吗");
//                adapter.setData(listItems);
//                adapter.notifyDataSetChanged();
//            }
//
//        }
//    };


    /**
     * 查询结果监听者
     */

    class MyOnGetGeoCoderResultListener implements OnGetGeoCoderResultListener {

        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

//            Log.i("tag", reverseGeoCodeResult.getAddress());
            car.setAddress(reverseGeoCodeResult.getAddress());
            Log.i("tag", reverseGeoCodeResult.getAddress());
            Log.i("tag", car.getAddress() + "  :  " + car.getSpeed());

            objectToList(car);

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
            startService(new Intent(CarInfo.this, TraceAgentService.class));
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
//                            Log.i("tag", "线程开了吗");
                            continue;
                        }


                        if (!HeartBeatThread.hbFlag) {
//                            Log.i("tag", "登录成功了吗");
                            continue;
                        }

//                        Log.i("tag",car.toString());
//
//                        Log.i("tag", car.getMac());
//                        Log.i("tag",SocketThread.loginInfo.containsKey("key") + "");
//                        Log.i("tag",SocketThread.loginInfo.get("key").equals("") + "");
//                        Log.i("tag","访问网络的线程开启");
                        //判断条件，如果car对象不无空，Mac的属性已经被赋值，和数据通道是否建立
                        if (car != null && car.getMac() != null && SocketThread.loginInfo.containsKey("key")
                                && !SocketThread.loginInfo.get("key").equals("")) {
//                            //发送点名信息。
//                            ps.print("cmd Retr\n" +
//                                    "mac " + car.getMac() + "\n" +
//                                    "app " + (count + 1) + "\n" +
//                                    "\n" +
//                                    "cmd Ctlm\n" +
//                                    "mac " + car.getMac() + "\n" +
//                                    "optcode 2\n" +
//                                    "optargs 0\n" +
//                                    "app " + (count + 1) + "\n\n");

                            String str = "cmd Retr\n" +
                                    "mac " + car.getMac() + "\n" +
                                    "app " + (count + 1) + "\n" +
                                    "\n" +
                                    "cmd Ctlm\n" +
                                    "mac " + car.getMac() + "\n" +
                                    "optcode 2\n" +
                                    "optargs 0\n" +
                                    "app " + (count + 1) + "\n\n";

//                            Thread.sleep(10000);
                            EventBus.getDefault().post(str, "event");

                            Log.i("tag", "jinqule");
                            bn = false;
                        }
                    }


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

                        Log.i("tag", str);

                        //处理数据
                        String[] arrStr = str.split(" ");

                        Log.i("tag", "length" + arrStr.length);

                        //当数组的长度为小于或等于1，则直接跳过下面的部分
                        if (arrStr.length <= 1) {
                            return;
                        }

                        str = arrStr[3];

                        if (car == null && !car.getMac().equals(str)) {
                            continue;
                        }

                        //对数据的处理
                        str = arrStr[4];
                        arrStr = str.split("\\|");


                        long dl = Long.parseLong(arrStr[2], 16);
                        Log.i("tag", "纬度" + arrStr[2]);
                        car.setLat(dl * 1.0 / 3600000 + "");

                        Log.i("tag", "纬度" + dl);

                        dl = Long.parseLong(arrStr[3], 16);
                        car.setLon(dl * 1.0 / 3600000 + "");

                        Log.i("tag", "经度" + dl);

                        dl = Long.parseLong(arrStr[4], 16);
                        car.setSpeed(dl + "");

                        ztFlag = false;

                        //进行反地理编码
                        initAddress();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.i("tag","udp链路断开");

            }
        }.start();


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




//        //发送退出登录的消息
//        ps.print("cmd Quit\n\n");
//        //把标志符设成false
//        flag = false;
//        ps.flush();
//        //关闭udp和输出流的
//        ps.close();
//        ds.close();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        car_list = null;
        Log.i("tag","onDestroy() ");
        gc.destroy();

    }


    class CarAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;
        private List<Map<String ,Object>> data;

        public CarAdapter(List<Map<String,Object>> data,Context context) {

            this.data = data;
            layoutInflater = LayoutInflater.from(context);

        }

        public void setData(List<Map<String,Object>> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        class CarItem{
            public ImageView icon;
            public TextView title;
            public TextView content;
        }

        @Override
        public int getCount() {
            return data.size();
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

            CarItem carItem = null;

            if (convertView == null) {
                carItem = new CarItem();

                convertView = layoutInflater.inflate(R.layout.car_item,null);

                carItem.icon = (ImageView) convertView.findViewById(R.id.info_icon);
                carItem.title = (TextView) convertView.findViewById(R.id.info_title);
                carItem.content = (TextView) convertView.findViewById(R.id.info_content);
                convertView.setTag(carItem);

            } else {
                carItem = (CarItem)convertView.getTag();
            }


            carItem.icon.setImageResource((Integer)data.get(position).get("img"));
            carItem.title.setText((String) data.get(position).get("title"));
            carItem.content.setText((String)data.get(position).get("info"));

            return convertView;
        }
    }

}

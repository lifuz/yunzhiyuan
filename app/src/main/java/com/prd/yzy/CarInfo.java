package com.prd.yzy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import com.prd.yzy.thread.HeartBeatThread;
import com.prd.yzy.thread.SocketThread;
import com.prd.yzy.utils.Base64;
import com.prd.yzy.utils.HttpUrls;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
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

    private Socket s;
    private DatagramSocket ds;

    private PrintStream ps;

    public static boolean flag = true;
    private boolean ztFlag = true;

    private static int count = 0;

    private SimpleAdapter adapter;
    private SharedPreferences share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.car_layout);

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
                    if (response.has("speed")) {
                        car.setSpeed(response.getString("speed"));
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

                    car.setUtc(response.getString("utc"));

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
        car_dm.setVisibility(View.GONE);


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
                ps.print("cmd Retr\n" +
                        "mac " + car.getMac() + "\n" +
                        "app " + (count + 1) + "\n" +
                        "\n" +
                        "cmd Ctlm\n" +
                        "mac " + car.getMac() + "\n" +
                        "optcode 2\n" +
                        "optargs 0\n" +
                        "app " + (count + 1) + "\n\n");
                break;
        }

    }


    /**
     * 构建适配器的参数和创建适配器
     *
     * @param car
     */
    public void objectToList(Car car) {

        //构建参数
        listItems = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("img", R.drawable.carinfo_ico_speed);
        map.put("title", "速度");
        map.put("info", car.getSpeed() + " Km/h");
        listItems.add(map);
        map = new HashMap<String, Object>();
        map.put("img", R.drawable.carinfo_ico_position);
        map.put("title", "位置");
        map.put("info", car.getAddress());
        listItems.add(map);

        map = new HashMap<String, Object>();
        map.put("img", R.drawable.carinfo_ico_company);
        map.put("title", "所属公司");
        map.put("info", car.getCompanyName());
        listItems.add(map);

        map = new HashMap<String, Object>();
        map.put("img", R.drawable.carinfo_ico_attribute);
        map.put("title", "运营属性");
        map.put("info", "-");
        listItems.add(map);

        map = new HashMap<String, Object>();
        map.put("img", R.drawable.carinfo_ico_driver);
        map.put("title", "当前司机");
        map.put("info", car.getDriverName());
        listItems.add(map);

        map = new HashMap<String, Object>();
        map.put("img", R.drawable.carinfo_ico_miles);
        map.put("title", "本月运行里程");
        map.put("info", car.getMiles() + "公里");
        listItems.add(map);

        map = new HashMap<String, Object>();
        map.put("img", R.drawable.carinfo_ico_time);
        map.put("title", "本月运行时间");
        map.put("info", car.getTimes());
        listItems.add(map);

        map = new HashMap<String, Object>();
        map.put("img", R.drawable.carinfo_ico_miles);
        map.put("title", "上月运行里程");
        map.put("info", car.getBeforeMiles() + "公里");
        listItems.add(map);

        map = new HashMap<String, Object>();
        map.put("img", R.drawable.carinfo_ico_time);
        map.put("title", "上月运行时间");
        map.put("info", car.getBeforeTimes());
        listItems.add(map);


        if (ztFlag) {
            //创建适配器
            adapter = new SimpleAdapter(CarInfo.this, listItems,
                    R.layout.car_item, new String[]{"img", "title", "info"},
                    new int[]{R.id.info_icon, R.id.info_title, R.id.info_content});

            //给ListView添加适配器
            car_list.setAdapter(adapter);

            car_dm.setVisibility(View.VISIBLE);
        } else {
            adapter = new SimpleAdapter(CarInfo.this, listItems,
                    R.layout.car_item, new String[]{"img", "title", "info"},
                    new int[]{R.id.info_icon, R.id.info_title, R.id.info_content});

            //给ListView添加适配器
            car_list.setAdapter(adapter);
        }


    }

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

        new Thread() {

            @Override
            public void run() {
                try {

                    //获取登录信息
                    String opid = share.getString("opid", "");
                    String pass = share.getString("password", "");

                    //对密码进行编码
                    String enStr = new String(Base64.encode(pass.getBytes()));

                    Log.i("tag", "opid : " + opid + ", pass : " + enStr + ",pass=" + pass);

                    //与121.40.199.67的服务器建立连接
                    s = new Socket("121.40.199.67", 7210);

                    //连接建立完成后，把标志位改为true；
                    flag = true;

                    //设置udp通讯的端口号
                    ds = new DatagramSocket(65411);

                    //获取socket链路的输出流
                    ps = new PrintStream(s.getOutputStream());
                    //向服务器发送登录信息
                    ps.print("cmd Auth\nuserid " + opid + "\npasswd " + enStr + "\n\n");
                    ps.flush();
                    //开启socket接收通道的线程
                    new SocketThread(ds, s).start();
                    //开启心跳机制，即定时向服务器发送固定的消息，以确认通道的畅通性
                    new HeartBeatThread(ps, ds).start();

                    //开启udp数据接收线程
                    new Thread() {
                        //定义一次接收的数据的长度
                        byte[] buf = new byte[4096];
                        //将接收的数据打包到这个对象
                        DatagramPacket dp = new DatagramPacket(buf, buf.length);

                        public void run() {
                            //循环等待接收数据
                            while (true) {
                                //设置包的长度
                                dp.setLength(buf.length);
                                try {
                                    //将程序挂起，等待数据包，并将接收到的数据打包到的dp对象中
                                    ds.receive(dp);
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

                                //对数据的处理
                                str = arrStr[4];
                                arrStr = str.split("\\|");


                                long dl = Long.parseLong(arrStr[2], 16);
                                car.setLat(dl * 1.0 / 3600000 + "");

                                Log.i("tag", "纬度" + dl);

                                dl = Long.parseLong(arrStr[3], 16);
                                car.setLon(dl * 1.0 / 3600000 + "");

                                Log.i("tag", "经度" + dl);

                                dl = Long.parseLong(arrStr[6], 16);
                                car.setSpeed(dl + "");

                                ztFlag = false;

                                //进行反地理编码
                                initAddress();
                            }

                        }

                        ;


                    }.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();


    }

    /**
     * 当页面的生命进程到onPause阶段，注销socket通讯
     */
    @Override
    protected void onPause() {
        super.onPause();

        //发送退出登录的消息
        ps.print("cmd Quit\n\n");
        //把标志符设成false
        flag = false;
        ps.flush();
        //关闭udp和输出流的
        ps.close();
        ds.close();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gc.destroy();

    }
}

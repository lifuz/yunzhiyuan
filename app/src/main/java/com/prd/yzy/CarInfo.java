package com.prd.yzy;

import android.content.Intent;
import android.os.Bundle;
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
import com.prd.yzy.thread.SocketThread;
import com.prd.yzy.utils.HttpUrls;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintStream;
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

    private List<Map<String,Object>> listItems;

    private ListView car_list;

    private Button car_dm;

    private Socket s;
    private DatagramSocket ds;

    private PrintStream ps;

    public static boolean flag = true;

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

        //初始化地理编码查询接口
        gc = GeoCoder.newInstance();

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


        client = new AsyncHttpClient();
        params = new RequestParams();


    }

    /**
     * 处理点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //设置点击返回时，处理过程
            case R.id.car_back:
                finish();
                break;
        }

    }


    /**
     * 构建适配器的参数和创建适配器
     * @param car
     */
    public void objectToList(Car car){

        //构建参数
        listItems = new ArrayList<Map<String,Object>>();

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("img",R.drawable.carinfo_ico_speed);
        map.put("title","速度");
        map.put("info",car.getSpeed()+" Km/h");
        listItems.add(map);
        map = new HashMap<String,Object>();
        map.put("img",R.drawable.carinfo_ico_position);
        map.put("title","位置");
        map.put("info",car.getAddress());
        listItems.add(map);

        map = new HashMap<String,Object>();
        map.put("img",R.drawable.carinfo_ico_company);
        map.put("title","所属公司");
        map.put("info",car.getCompanyName());
        listItems.add(map);

        map = new HashMap<String,Object>();
        map.put("img",R.drawable.carinfo_ico_attribute);
        map.put("title","运营属性");
        map.put("info","-");
        listItems.add(map);

        map = new HashMap<String,Object>();
        map.put("img",R.drawable.carinfo_ico_driver);
        map.put("title","当前司机");
        map.put("info",car.getDriverName());
        listItems.add(map);

        map = new HashMap<String,Object>();
        map.put("img",R.drawable.carinfo_ico_miles);
        map.put("title","本月运行里程");
        map.put("info",car.getMiles()+"公里");
        listItems.add(map);

        map = new HashMap<String,Object>();
        map.put("img",R.drawable.carinfo_ico_time);
        map.put("title","本月运行时间");
        map.put("info",car.getTimes());
        listItems.add(map);

        map = new HashMap<String,Object>();
        map.put("img",R.drawable.carinfo_ico_miles);
        map.put("title","上月运行里程");
        map.put("info",car.getBeforeMiles()+"公里");
        listItems.add(map);

        map = new HashMap<String,Object>();
        map.put("img",R.drawable.carinfo_ico_time);
        map.put("title","上月运行时间");
        map.put("info",car.getBeforeTimes());
        listItems.add(map);


        //创建适配器
        SimpleAdapter adapter = new SimpleAdapter(CarInfo.this,listItems,
                R.layout.car_item,new String[]{"img","title","info"},
                new int[]{R.id.info_icon,R.id.info_title,R.id.info_content});

        //给ListView添加适配器
        car_list.setAdapter(adapter);

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

            objectToList(car);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        flag = true;

       new Thread() {

           @Override
           public void run() {
               try {

                   s = new Socket("121.40.199.67", 7210);
                   ds = new DatagramSocket(65411);
                   ps = new PrintStream(s.getOutputStream());
                   ps.print("cmd Auth\nuserid 9369\npasswd OA==\n\n");
                   ps.flush();

                   new SocketThread(ds,s).start();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }.start();

    }

    @Override
    protected void onPause() {
        super.onPause();

        ps.print("cmd Quit\n\n");
        flag = false;
        ps.flush();
        ps.close();
        ds.close();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gc.destroy();

    }
}

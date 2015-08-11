package com.prd.yzy.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.prd.yzy.LoginActivity;
import com.prd.yzy.R;
import com.prd.yzy.bean.Car;
import com.prd.yzy.utils.HttpUrls;
import com.prd.yzy.utils.ImageLoaderTask;
import com.prd.yzy.widget.LazyScrollView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 李富 on 2015/7/7.
 */
public class XHBFragment extends Fragment {

//    private ListView xhb_list;
//    private EditText et;

    private AsyncHttpClient client;
    private RequestParams params;

    private SharedPreferences shared;

    private  String suid;

    private List<Car> cars;



    private Display display;

    private int column_count = 3; //显示列数
    private int row_count = 3;//显示行数
    private int itemWidth; //每列的宽度；
    private int itemHeight;//每行高度

    private AssetManager assetManager;//assets文件夹管理器

    private int page_count = 15;//每次加载多少张图片
    private int current_page = 0;//目前的页数
    private List<String> list_image;//图片路径集合

    private LazyScrollView waterfall_scroll;
    private LinearLayout waterfall_container;

    private List<LinearLayout> waterfall_items;



//    private List<Map<String,String>> listItems;
//    private Map<String,String> listItem;

    public static List<Car> pre_cars;

    private SimpleAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate( R.layout.xhb_layout,container,false);
        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        client = new AsyncHttpClient();
        params = new RequestParams();

        shared = getActivity().getSharedPreferences("login", Activity.MODE_PRIVATE);

        suid = shared.getString("suid", "");

        cars = new ArrayList<Car>();
        pre_cars = new ArrayList<Car>();

        assetManager = getActivity().getAssets();

        try {
            list_image = Arrays.asList(assetManager.list("images"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("tag",list_image.toString());

//        xhb_list = (ListView) view.findViewById(R.id.xhb_list);
//        et = (EditText) view.findViewById(R.id.num);
//        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    et.setText("");
//                } else {
//                    et.setText("请输入车牌号");
//                }
//            }
//        });

//        et.addTextChangedListener(watcher);

        if("".equals(suid)){
            startActivity(new Intent(getActivity(), LoginActivity.class));
        } else {
            params.put("sgid",suid);
        }

       initLazyScroll(view);


        client.post(HttpUrls.http_xhb,params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                for(int i = 0 ; i <response.length();i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        Car car = new Car();
                        car.setVid(obj.getString("vid"));
                        car.setName(obj.getString("name"));
                        car.setDesc(obj.getString("desc"));

                        if (i%2 ==0) {
                            car.setPicFile("images/"+list_image.get(3));
                        } else {
                            car.setPicFile("images/"+list_image.get(4));
                        }

                        car.setAssetManager(assetManager);

                        Log.i("tag","images/"+car.getPicFile());

                        cars.add(car);
                        pre_cars.add(car);
                        car = null;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                addItemToContainer(current_page,page_count);

//                listItems = new ArrayList<Map<String, String>>();
//
//                for(Car car:cars){
//                    listItem = new HashMap<String, String>();
//                    listItem.put("name",car.getName());
//                    listItem.put("desc",car.getDesc());
//                    listItem.put("vid",car.getVid());
////                    Log.i("tag",car.getDesc());
//                    listItems.add(listItem);
//                }

//               adapter = new SimpleAdapter(getActivity(),listItems,R.layout.xhb_item,
//                        new String[]{"name","desc","vid"},
//                        new int[]{R.id.xhb_cph,R.id.xhb_zt,R.id.xhb_id});
//
//                xhb_list.setAdapter(adapter);

            }
        });


//        xhb_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//
////                Toast.makeText(getActivity(),cars.get(position).getName(),Toast.LENGTH_SHORT).show();
//                Intent it = new Intent(getActivity(), CarInfo.class);
//
//                it.putExtra("vid",cars.get(position).getVid());
//
//                startActivity(it);
//            }
//        });

    }

    private void addItemToContainer(int pageIndex,int page_count) {
        int j = 0;
        int imageCount = cars.size();

        for (int i = pageIndex * page_count; i < page_count * (pageIndex + 1) && i < imageCount; i++) {
            //判断这张图片在一行的什么位置
            j = j >= column_count ? 0 : j;
            addItem(cars.get(i), j++);
        }

    }


    private void addItem(Car car,int columnIndex) {
        LinearLayout item = (LinearLayout) LayoutInflater.from(getActivity())
                .inflate(R.layout.xhb_waterfall_item, null);

        ImageView xhb_iv = (ImageView)item.findViewById(R.id.xhb_waterfall_pic);
        TextView xhb_name = (TextView) item.findViewById(R.id.xhb_waterfall_name);
        TextView xhb_zt = (TextView) item.findViewById(R.id.xhb_waterfall_zt);
        xhb_name.setText(car.getName());
        xhb_zt.setText("机车状态：" + car.getDesc());

        waterfall_items.get(columnIndex).addView(item);

        ImageLoaderTask task = new ImageLoaderTask(xhb_iv);
        task.execute(car);

    }

    private void initLazyScroll(View view) {
        display = getActivity().getWindowManager().getDefaultDisplay();

        itemWidth = display.getWidth() / column_count;
        itemHeight = display.getHeight() / row_count;

        waterfall_scroll = (LazyScrollView) view.findViewById(R.id.xhb_waterfall_scroll);
        waterfall_scroll.getView();
        waterfall_scroll.setOnScrollListener(new LazyScrollView.OnScrollListener() {
            @Override
            public void onBotom() {
                addItemToContainer(++current_page, page_count);
            }

            @Override
            public void onTop() {

            }

            @Override
            public void onScroll() {

            }
        });

        waterfall_container = (LinearLayout) view.findViewById(R.id.xhb_waterfall_container);

        //这个list容器放的是LinearLayout，这些Linearlayout里主要放ImageView
        waterfall_items = new ArrayList<>();
        //根据列数确定放到waterfall_container里的LinearLayout数量，即每个LinearLayout都充当一列
        for (int i = 0; i < column_count; i++) {

            //定义一个LinearLayout布局
            LinearLayout itemLayout = new LinearLayout(getActivity());
            //定义布局的高度和宽度
            LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(itemWidth,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            //设置组件的间隔
            itemLayout.setPadding(2,2,2,2);

            //设置线性布局的方向，这里设置是垂直方向
            itemLayout.setOrientation(LinearLayout.VERTICAL);

            //设置布局的高度和宽度
            itemLayout.setLayoutParams(itemParam);
            waterfall_items.add(itemLayout);
            //把布局放入waterfall_container中
            waterfall_container.addView(itemLayout);



        }

    }

//    boolean is = false;
//    private TextWatcher watcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count,
//                                      int after) {
//        }
//
//        List<Car> list = null;
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before,
//                                  int count) {
//
//            String str = et.getText().toString();
////            if(str.equals("")){
////                return;
////            }
//            cars.clear();
//            for(Car car:pre_cars){
//                if(car.getName().contains(str)){
//                    cars.add(car);
//                }
//            }
//
//            Log.i("tag",cars.toString());
//
//            listItems.clear();
//            for(Car car:cars){
//                listItem = new HashMap<String, String>();
//                listItem.put("name",car.getName());
//                listItem.put("desc",car.getDesc());
//                listItem.put("vid",car.getVid());
////                    Log.i("tag",car.getDesc());
//                listItems.add(listItem);
//            }
//
//            adapter.notifyDataSetChanged();
//
//        }
//
//        @Override
//        public void afterTextChanged(Editable arg0) {
//
//        }
//    };
}

package com.prd.testtv.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.prd.testtv.CarActivity;
import com.prd.testtv.R;
import com.prd.testtv.application.PrdApplication;
import com.prd.testtv.bean.Car;
import com.prd.testtv.request.GJsonObjectRequest;
import com.prd.testtv.utils.ImageLoaderTask;
import com.prd.testtv.widget.LazyScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：李富 on 2015/9/18.
 * 邮箱：lifuzz@163.com
 */
public class HBFragment extends Fragment implements View.OnFocusChangeListener {

    private int column_count = 3; //显示列数
    private int row_count = 3;//显示行数
    private int itemWidth; //每列的宽度；
    private int itemHeight;//每行高度

    private int page_count = 15;//每次加载多少张图片
    private int current_page = 0;//目前的页数

    private LazyScrollView waterfall_scroll;
    private LinearLayout waterfall_container;
    private List<LinearLayout> waterfall_items;
    private Display display;
    private List<Car> cars;

    private int count = 0;

    private SharedPreferences share;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.hb_layout, container, false);
        return layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        PrdApplication prd = (PrdApplication) getActivity().getApplication();

        share = getActivity().getSharedPreferences("login", Activity.MODE_PRIVATE);

        initLazyScroll(view);

        Map<String,String> params = new HashMap<>();

        params.put("ogid",share.getString("ogid",""));

        GJsonObjectRequest<Car[]> gJsonObjectRequest = new GJsonObjectRequest<Car[]>(Request.Method.POST,getResources().getString(R.string.url_hb),
                params, Car[].class, new Response.Listener<Car[]>() {
            @Override
            public void onResponse(Car[] mycars) {

                AssetManager assetManager = getActivity().getAssets();

                boolean flag = true;

                cars = new ArrayList<>();

                for (Car car:mycars) {
                    Log.i("tag", car.toString());
                    car.setAssetManager(assetManager);


                    if (flag) {
                        car.setPicFile("images/yzy_jc1.jpg");
                        car.setDesc("离线");
                        flag = false;
                    } else {
                        car.setPicFile("images/yzy_jc2.png");
                        car.setDesc("在线");
                        flag = true;
                    }

                    cars.add(car);
                }

                addItemToContainer(current_page, page_count);



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();

            }
        });

        prd.getQueue().add(gJsonObjectRequest);

    }

    private void initLazyScroll(View view) {
        display = getActivity().getWindowManager().getDefaultDisplay();

        itemWidth = display.getWidth() / column_count * 5 / 7;
        itemHeight = display.getHeight() / row_count;

        waterfall_scroll = (LazyScrollView) view.findViewById(R.id.hb_waterfall_scroll);

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

        waterfall_container = (LinearLayout) view.findViewById(R.id.hb_waterfall_container);
//        waterfall_scroll.setFocusable(true);
//        waterfall_container.setFocusable(true);

        //这个list容器放的是LinearLayout，这些Linearlayout里主要放ImageView
        waterfall_items = new ArrayList<>();
        //根据列数确定放到waterfall_container里的LinearLayout数量，即每个LinearLayout都充当一列
        for (int i = 0; i < column_count; i++) {

            //定义一个LinearLayout布局
            LinearLayout itemLayout = new LinearLayout(getActivity());
            //定义布局的高度和宽度
            LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(itemWidth,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

//            itemLayout.setFocusable(true);

//            itemLayout.setPadding(3,3,3,3);

            //设置线性布局的方向，这里设置是垂直方向
            itemLayout.setOrientation(LinearLayout.VERTICAL);

            //设置布局的高度和宽度
            itemLayout.setLayoutParams(itemParam);
            waterfall_items.add(itemLayout);
            //把布局放入waterfall_container中
            waterfall_container.addView(itemLayout);


        }

    }


    private void addItemToContainer(int pageIndex, int page_count) {
        int j = 0;
        int imageCount = cars.size();

        for (int i = pageIndex * page_count; i < page_count * (pageIndex + 1) && i < imageCount; i++) {
            //判断这张图片在一行的什么位置
            j = j >= column_count ? 0 : j;
            addItem(cars.get(i), j++);
        }

    }

    private void addItem(final Car car, int columnIndex) {
        final LinearLayout item = (LinearLayout) LayoutInflater.from(getActivity())
                .inflate(R.layout.xhb_waterfall_item, null);
        item.setFocusable(true);

//        item.setPadding(4,4,4,4);

        count = count  + 1;

        item.setId(count);



        ImageView xhb_iv = (ImageView) item.findViewById(R.id.xhb_waterfall_pic);
        TextView xhb_name = (TextView) item.findViewById(R.id.xhb_waterfall_name);
        TextView xhb_zt = (TextView) item.findViewById(R.id.xhb_waterfall_zt);
        xhb_name.setText(car.getName());
        xhb_zt.setText("机车状态：" + car.getDesc());

//        LinearLayout item2 = new LinearLayout(getActivity());
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemWidth, 3);
//        item2.setLayoutParams(params);
//        item2.setBackgroundColor(Color.GRAY);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemWidth,itemHeight);
        params.setMargins(8,6,8,6);
        item.setLayoutParams(params);

        waterfall_items.get(columnIndex).addView(item);

        item.setOnFocusChangeListener(this);



//        waterfall_items.get(columnIndex).addView(item2);

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), CarActivity.class);

                it.putExtra("vid", car.getVid());

                startActivity(it);
            }
        });

        ImageLoaderTask task = new ImageLoaderTask(xhb_iv);
        task.execute(car);


    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
//                    item.setBackgroundColor(getResources().getColor(R.color.orange));

            v.setBackgroundResource(R.drawable.shape_focus);

            int id = v.getId();

            int index = id % 3 == 0 ? (id /3) :(id / 3 + 1);

//            Log.i("tag",index + "");

            if (index % 5 == 0) {
                addItemToContainer(++current_page, page_count);
            }

        }else  {

            v.setBackgroundColor(getResources().getColor(R.color.white));
        }


    }
}



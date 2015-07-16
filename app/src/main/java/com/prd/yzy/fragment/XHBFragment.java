package com.prd.yzy.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.prd.yzy.CarInfo;
import com.prd.yzy.LoginActivity;
import com.prd.yzy.R;
import com.prd.yzy.bean.Car;
import com.prd.yzy.utils.HttpUrls;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 李富 on 2015/7/7.
 */
public class XHBFragment extends Fragment {

    private ListView xhb_list;
    private EditText et;

    private AsyncHttpClient client;
    private RequestParams params;

    private SharedPreferences shared;

    private  String suid;

    private List<Car> cars;

    private List<Map<String,String>> listItems;
    private Map<String,String> listItem;

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

        suid = shared.getString("suid","");

        cars = new ArrayList<Car>();
        pre_cars = new ArrayList<Car>();

        xhb_list = (ListView) view.findViewById(R.id.xhb_list);
        et = (EditText) view.findViewById(R.id.num);
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et.setText("");
                } else {
                    et.setText("请输入车牌号");
                }
            }
        });

        et.addTextChangedListener(watcher);

        if("".equals(suid)){
            startActivity(new Intent(getActivity(), LoginActivity.class));
        } else {
            params.put("sgid",suid);
        }

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
                        cars.add(car);
                        pre_cars.add(car);
                        car = null;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                listItems = new ArrayList<Map<String, String>>();

                for(Car car:cars){
                    listItem = new HashMap<String, String>();
                    listItem.put("name",car.getName());
                    listItem.put("desc",car.getDesc());
                    listItem.put("vid",car.getVid());
//                    Log.i("tag",car.getDesc());
                    listItems.add(listItem);
                }

               adapter = new SimpleAdapter(getActivity(),listItems,R.layout.xhb_item,
                        new String[]{"name","desc","vid"},
                        new int[]{R.id.xhb_cph,R.id.xhb_zt,R.id.xhb_id});

                xhb_list.setAdapter(adapter);

            }
        });


        xhb_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


//                Toast.makeText(getActivity(),cars.get(position).getName(),Toast.LENGTH_SHORT).show();
                Intent it = new Intent(getActivity(), CarInfo.class);

                it.putExtra("vid",cars.get(position).getVid());

                startActivity(it);
            }
        });

    }

    boolean is = false;
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        List<Car> list = null;

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

            String str = et.getText().toString();
//            if(str.equals("")){
//                return;
//            }
            cars.clear();
            for(Car car:pre_cars){
                if(car.getName().contains(str)){
                    cars.add(car);
                }
            }

            Log.i("tag",cars.toString());

            listItems.clear();
            for(Car car:cars){
                listItem = new HashMap<String, String>();
                listItem.put("name",car.getName());
                listItem.put("desc",car.getDesc());
                listItem.put("vid",car.getVid());
//                    Log.i("tag",car.getDesc());
                listItems.add(listItem);
            }

            adapter.notifyDataSetChanged();

        }

        @Override
        public void afterTextChanged(Editable arg0) {

        }
    };
}

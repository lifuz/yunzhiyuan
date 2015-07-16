package com.prd.yzy.gj;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.prd.yzy.BaseActivity;
import com.prd.yzy.R;
import com.prd.yzy.bean.Customer;
import com.prd.yzy.utils.HttpUrls;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李富 on 2015/7/16.
 */
public class EmployeeActivity extends BaseActivity  {

    private List<Customer> customers;

    private SharedPreferences share;

    private AsyncHttpClient client;
    private RequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.emp_layout);

        initViews();

    }

    public void initViews(){
        share = getSharedPreferences("login", Activity.MODE_PRIVATE);

        customers = new ArrayList<>();

        client = new AsyncHttpClient();
        params = new RequestParams();

        String sgid = share.getString("suid","");

//        Log.i("tag", "sgid" + sgid);

        if (sgid.equals("")){
            finish();
        }

       getEmpInfo(sgid);

    }

    public void getEmpInfo(String sgid){
        params.remove("sgid");
        params.put("sgid", sgid);
        client.post(HttpUrls.http_customer,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                for (int i = 0 ; i<response.length();i++){
                    try {
                        JSONObject object = response.getJSONObject(i);

                        Customer customer = new Customer();
                        customer.setCid(object.getString("cid"));
                        customer.setCname(object.getString("cname"));
                        customer.setCphone(object.getString("cphone"));
                        customer.setDriverOperId(object.getString("driverOperId"));
                        customer.setSex(object.getString("sex"));
                        customers.add(customer);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Log.i("tag",customers.toString());

            }
        });
    }
}

package com.prd.yzy.gj.sc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.inqbarna.tablefixheaders.TableFixHeaders;
import com.inqbarna.tablefixheaders.adapters.BaseTableAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.prd.yzy.BaseActivity;
import com.prd.yzy.R;
import com.prd.yzy.bean.Customer;
import com.prd.yzy.gj.add.AddMenuActivity;
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
public class NXActivity extends BaseActivity  implements View.OnClickListener {

    private List<Customer> customers;

    private SharedPreferences share;

    private AsyncHttpClient client;
    private RequestParams params;

    private TableFixHeaders tfh;

    private EditText emp_search;
    private static List<Customer> customerList;

    private MyAdapter adapter;

    private View emp_back;
    private TextView gj_title;
    private View gj_add;

    private TextView gj_back_title;
    private ImageView gj_add_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.emp_layout);

        initViews();

    }

    public void initViews(){
        share = getSharedPreferences("login", Activity.MODE_PRIVATE);

        customers = new ArrayList<>();
        customerList = new ArrayList<>();

        client = new AsyncHttpClient();
        params = new RequestParams();

        gj_title = (TextView) findViewById(R.id.gj_title);
        gj_title.setText("能效");

        tfh = (TableFixHeaders) findViewById(R.id.yg_table);
        tfh.setVisibility(View.GONE);

        emp_back = findViewById(R.id.emp_search);
        emp_back.setOnClickListener(this);

        emp_search = (EditText) findViewById(R.id.emp_search);
        emp_search.addTextChangedListener(watcher);

        gj_add = findViewById(R.id.gj_add);
        gj_add_iv = (ImageView) findViewById(R.id.gj_add_iv);
        gj_add_iv.setVisibility(View.GONE);

        gj_back_title = (TextView) findViewById(R.id.gj_back_title);
        gj_back_title.setText("生产");

        String sgid = share.getString("ogid","");

//        Log.i("tag", "sgid" + sgid);

        if (sgid.equals("")){
            finish();
        }

       getEmpInfo(sgid);

    }

    public void getEmpInfo(String sgid){
        params.remove("ogid");
        params.put("ogid", sgid);
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
                        customerList.add(customer);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Log.i("tag", "已经获取到了数据");

                adapter = new MyAdapter(getApplicationContext());
                tfh.setAdapter(adapter);
                tfh.setVisibility(View.VISIBLE);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.emp_back:
                finish();
                break;

            case R.id.gj_add:

                startActivity(new Intent(NXActivity.this, AddMenuActivity.class));
                break;
        }
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }



        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

            String str = emp_search.getText().toString();
//            if(str.equals("")){
//                return;
//            }
            customers.clear();
            for(Customer customer:customerList){
                if(customer.getCname().contains(str)){
                   customers.add(customer);
                }
            }



            adapter.notifyDataSetChanged();

        }

        @Override
        public void afterTextChanged(Editable arg0) {

        }
    };


    public class MyAdapter extends BaseTableAdapter {

        private final Context context;
        private final LayoutInflater inflater;

        private final int width;
        private final int height;

        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);

            //从配置文件获取高度和宽度
            Resources rs = context.getResources();

            width = rs.getDimensionPixelSize(R.dimen.table_width);
            height = rs.getDimensionPixelSize(R.dimen.table_height);


        }

        public Context getContext() {
            return context;
        }

        public LayoutInflater getInflater() {
            return inflater;
        }


        /**
         * 设置行数
         *
         * @return
         */
        @Override
        public int getRowCount() {
            return customers.size();
        }

        /**
         * 设置列数
         *
         * @return
         */
        @Override
        public int getColumnCount() {
            return 4;
        }

        /**
         * 为每个View设置布局和值
         *
         * @param row       The row of the item within the adapter's data table of the
         *                  item whose view we want. If the row is <code>-1</code> it is
         *                  the header.
         * @param column    The column of the item within the adapter's data table of the
         *                  item whose view we want. If the column is <code>-1</code> it
         *                  is the header.
         * @param view
         * @param viewGroup
         * @return
         */
        @Override
        public View getView(final int row, final int column, View view, ViewGroup viewGroup) {

            if (view == null) {
                view = inflater.inflate(getLayoutResource(row, column), viewGroup, false);
            }

            ((TextView) view.findViewById(R.id.item_tv)).setText(getCellString(row, column));


            if (row >= 0) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(NXActivity.this, customers.get(row).getCname(), Toast.LENGTH_SHORT).show();
                    }
                });
            }


            return view;
        }

        /**
         * 设置每个View的值
         *
         * @param row
         * @param column
         * @return
         */

        public String getCellString(int row, int column) {

            if (row <0) {
                switch (column){
                    case -1:
                        return "机台名称";
                    case 0:
                        return "机台编号";
                    case 1:
                        return "日期";
                    case 2:
                        return "标准能耗";
                    case 3:
                        return "实际能耗";

                }

            } else {
                switch (column) {
                    case -1:
                        return customers.get(row).getCid();
                    case 0:
                        return customers.get(row).getCname();
                    case 1:
                        return customers.get(row).getCphone();
                    case 2:
                        return customers.get(row).getSex();

                    case 3:
                        return customers.get(row).getDriverOperId();

                }
            }

            return "Lorem (" + row + ", " + column + ")";
        }

        /**
         * 设置每个View的宽度
         *
         * @param i
         * @return
         */
        @Override
        public int getWidth(int i) {
            return width;
        }

        /**
         * 设置每个View的高度
         *
         * @param i
         * @return
         */
        @Override
        public int getHeight(int i) {
            return height;
        }

        /**
         * 根据行数确定view的类型
         *
         * @param row    The row of the item within the adapter's data table of the
         *               item whose view we want. If the row is <code>-1</code> it is
         *               the header.
         * @param column The column of the item within the adapter's data table of the
         *               item whose view we want. If the column is <code>-1</code> it
         *               is the header.
         * @return
         */
        @Override
        public int getItemViewType(int row, int column) {
            if (row < 0) {
                return 0;
            } else {
                return 1;
            }

        }

        /**
         * 根据view的类型选择相应的布局
         *
         * @param row
         * @param column
         * @return
         */
        public int getLayoutResource(int row, int column) {
            final int layoutResource;
            switch (getItemViewType(row, column)) {
                case 0:
                    layoutResource = R.layout.table_header_item;
                    break;
                case 1:
                    layoutResource = R.layout.table_item;
                    break;
                default:
                    throw new RuntimeException("wtf?");
            }
            return layoutResource;
        }


        /**
         * 设置View的类型总数
         *
         * @return
         */
        @Override
        public int getViewTypeCount() {
            return 2;
        }
    }
}

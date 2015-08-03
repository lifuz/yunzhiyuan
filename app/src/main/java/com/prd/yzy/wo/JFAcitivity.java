package com.prd.yzy.wo;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.prd.yzy.BaseActivity;
import com.prd.yzy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 李富 on 2015/8/3.
 */
public class JFAcitivity extends BaseActivity implements View.OnClickListener{

    private TextView jf_dq;

    private View jfgl_back;

    private List<Map<String,String>> list;

    private ListView jf_list;
    private Map<String,String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jifen_layout);

        initViews();

    }

    public void initViews(){

        jf_dq = (TextView) findViewById(R.id.jf_dq);
        jf_dq.setText("当前积分:" + 20);

        jfgl_back = findViewById(R.id.jfgl_back);
        jfgl_back.setOnClickListener(this);

        jf_list = (ListView) findViewById(R.id.jf_list);

        list = new ArrayList<>();
        for (int i = 0;i < 5 ;i ++){
            map = new HashMap<>();
            map.put("date","2015/8/" +(5-i));
            map.put("event","分享新闻");
            map.put("jf",2+"");
            list.add(map);

            map = new HashMap<>();
            map.put("date","2015/8/" +(5-i));
            map.put("event","分享活动");
            map.put("jf",2+"");
            list.add(map);


        }

        SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.jf_item,
                new String[]{"date","event","jf"},
                new int[] {R.id.jf_date,R.id.jf_event,R.id.jf_jf});

        jf_list.setAdapter(adapter);



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.jfgl_back:

                finish();

                break;
        }

    }
}

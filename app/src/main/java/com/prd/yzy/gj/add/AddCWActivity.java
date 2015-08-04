package com.prd.yzy.gj.add;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.prd.yzy.BaseActivity;
import com.prd.yzy.R;

/**
 * Created by 李富 on 2015/7/31.
 */
public class AddCWActivity extends BaseActivity implements View.OnClickListener {


    private View menu_back;
    private TextView menu_back_title,menu_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.addmenu_layout);

        initViews();

    }

    public void initViews(){

        menu_back = findViewById(R.id.menu_back);
        menu_back.setOnClickListener(this);

        menu_back_title = (TextView) findViewById(R.id.menu_back_title);
        menu_back_title.setText("财务");

        menu_title = (TextView) findViewById(R.id.menu_title);
        menu_title.setText("添加财务");



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.menu_back:

                finish();

                break;

        }
    }
}

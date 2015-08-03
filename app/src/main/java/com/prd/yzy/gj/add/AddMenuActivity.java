package com.prd.yzy.gj.add;

import android.os.Bundle;
import android.view.View;

import com.prd.yzy.BaseActivity;
import com.prd.yzy.R;

/**
 * Created by 李富 on 2015/7/31.
 */
public class AddMenuActivity extends BaseActivity implements View.OnClickListener {


    private View menu_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.addmenu_layout);

        initViews();

    }

    public void initViews(){

        menu_back = findViewById(R.id.menu_back);
        menu_back.setOnClickListener(this);



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

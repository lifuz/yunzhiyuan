package com.prd.yzy.gj.add;

import android.os.Bundle;
import android.view.View;

import com.prd.yzy.BaseActivity;
import com.prd.yzy.R;

/**
 * Created by 李富 on 2015/7/31.
 */
public class InOutAcitivity extends BaseActivity implements View.OnClickListener{

    private View kc_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.out_in_layout);

        initViews();

    }

    public void initViews(){

        kc_back = findViewById(R.id.kc_back);
        kc_back.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.kc_back:

                finish();

                break;

        }
    }
}

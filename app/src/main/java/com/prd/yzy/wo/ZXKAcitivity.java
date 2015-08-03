package com.prd.yzy.wo;

import android.os.Bundle;
import android.view.View;

import com.prd.yzy.BaseActivity;
import com.prd.yzy.R;

/**
 * Created by 李富 on 2015/8/3.
 */
public class ZXKAcitivity extends BaseActivity implements View.OnClickListener {

    private View zxk_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.zsk_layout);

        initViews();

    }

    public void initViews(){

        zxk_back = findViewById(R.id.zxk_back);
        zxk_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.zxk_back:

                finish();

                break;
        }

    }
}

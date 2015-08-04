package com.prd.yzy.gj.add;

import android.os.Bundle;
import android.view.View;

import com.prd.yzy.BaseActivity;
import com.prd.yzy.R;

/**
 * Created by 李富 on 2015/7/30.
 */
public class AddBOMAcitivity extends BaseActivity implements View.OnClickListener {

    private View bom_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.addbom_layout);

    }

    public void initViews(){

        bom_back = findViewById(R.id.bom_back);
        bom_back.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bom_back:

                finish();

                break;

        }

    }
}

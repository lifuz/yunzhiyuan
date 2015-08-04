package com.prd.yzy.gj.add;

import android.os.Bundle;
import android.view.View;

import com.prd.yzy.BaseActivity;
import com.prd.yzy.R;

/**
 * Created by 李富 on 2015/7/30.
 */
public class AddEmpActivity extends BaseActivity implements View.OnClickListener {

    private View yg_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.addemp_layout);

        initViews();

    }

    public void initViews() {

        yg_back = findViewById(R.id.yg_back);
        yg_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.yg_back:

                finish();

                break;

        }

    }
}

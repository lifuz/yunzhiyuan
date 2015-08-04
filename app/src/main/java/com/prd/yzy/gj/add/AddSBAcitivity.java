package com.prd.yzy.gj.add;

import android.os.Bundle;
import android.view.View;

import com.prd.yzy.BaseActivity;
import com.prd.yzy.R;

/**
 * Created by 李富 on 2015/8/4.
 */
public class AddSBAcitivity extends BaseActivity implements View.OnClickListener {

    private View shebei_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.shebei_layout);

        initViews();

    }

    public void initViews(){
        shebei_back = findViewById(R.id.shebei_back);
        shebei_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shebei_back:

                finish();

                break;
        }
    }
}

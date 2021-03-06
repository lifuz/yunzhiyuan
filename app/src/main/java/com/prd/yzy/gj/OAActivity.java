package com.prd.yzy.gj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.prd.yzy.BaseActivity;
import com.prd.yzy.R;
import com.prd.yzy.gj.oa.PZBBActivity;
import com.prd.yzy.gj.oa.SBGLBBActivity;
import com.prd.yzy.gj.oa.SCBBActivity;
import com.prd.yzy.gj.oa.XSBBActivity;

/**
 * Created by 李富 on 2015/7/30.
 */
public class OAActivity extends BaseActivity implements View.OnClickListener {

    private View oa_back;
    private View oa_xsbb,oa_scbb,oa_sbglbb,oa_pzbb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.oa_layout);

        initViews();

    }

    public void initViews(){

        oa_back = findViewById(R.id.oa_back);
        oa_back.setOnClickListener(this);

        oa_xsbb = findViewById(R.id.oa_xsbb);
        oa_xsbb.setOnClickListener(this);

        oa_scbb = findViewById(R.id.oa_scbb);
        oa_scbb.setOnClickListener(this);

        oa_sbglbb = findViewById(R.id.oa_sbglbb);
        oa_sbglbb.setOnClickListener(this);

        oa_pzbb = findViewById(R.id.oa_pzbb);
        oa_pzbb.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.oa_back:

                finish();
                break;

            case R.id.oa_xsbb:

                startActivity(new Intent(OAActivity.this, XSBBActivity.class));

                break;

            case R.id.oa_scbb:
                startActivity(new Intent(OAActivity.this, SCBBActivity.class));
                break;

            case R.id.oa_pzbb:
                startActivity(new Intent(OAActivity.this, PZBBActivity.class));
                break;

            case R.id.oa_sbglbb:
                startActivity(new Intent(OAActivity.this, SBGLBBActivity.class));
                break;
        }

    }
}

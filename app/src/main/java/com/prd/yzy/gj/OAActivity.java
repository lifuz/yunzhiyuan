package com.prd.yzy.gj;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.prd.yzy.BaseActivity;
import com.prd.yzy.R;

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
                Toast.makeText(getApplication(), "点击销售报表", Toast.LENGTH_SHORT).show();
                break;

            case R.id.oa_scbb:
                Toast.makeText(getApplication(),"点击生产报表",Toast.LENGTH_SHORT).show();
                break;

            case R.id.oa_pzbb:
                Toast.makeText(getApplication(),"点击品质报表",Toast.LENGTH_SHORT).show();
                break;

            case R.id.oa_sbglbb:
                Toast.makeText(getApplication(),"点击设备管理报表",Toast.LENGTH_SHORT).show();
                break;
        }

    }
}

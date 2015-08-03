package com.prd.yzy.wo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.prd.yzy.BaseActivity;
import com.prd.yzy.LoginActivity;
import com.prd.yzy.R;

/**
 * Created by 李富 on 2015/8/3.
 */
public class SZAcitivity extends BaseActivity implements View.OnClickListener{

    private View sz_back;

    private Button login_out;
    SharedPreferences share;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.shezhi_layout);

        initViews();

    }

    public void initViews() {

        sz_back = findViewById(R.id.sz_back);
        sz_back.setOnClickListener(this);

        login_out = (Button) findViewById(R.id.login_out);
        login_out.setOnClickListener(this);


        share = getSharedPreferences("login", Activity.MODE_PRIVATE);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sz_back:

                finish();

                break;

            case R.id.login_out:

                //把用户注销
                editor.putString("suid", "");
                editor.commit();

                startActivity(new Intent(SZAcitivity.this, LoginActivity.class));

                break;
        }

    }
}

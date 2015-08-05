package com.prd.yzy.gj.add;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.prd.yzy.BaseActivity;
import com.prd.yzy.R;

/**
 * Created by 李富 on 2015/7/31.
 */
public class AddMenuActivity extends BaseActivity implements View.OnClickListener {


    private View menu_back;

    private Spinner spinner_cp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.addmenu_layout);

        initViews();

    }

    public void initViews(){

        menu_back = findViewById(R.id.menu_back);
        menu_back.setOnClickListener(this);

        spinner_cp = (Spinner) findViewById(R.id.spinner_cp);

        String[] arr = { "空气阀","开关阀","电磁阀","平衡车" };
        //设置spinner的样式和下拉的值
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,arr);

        spinner_cp.setAdapter(adapter);



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

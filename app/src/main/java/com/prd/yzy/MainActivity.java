package com.prd.yzy;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;

import com.prd.yzy.fragment.GuanJiaFragment;
import com.prd.yzy.fragment.MiShuFragment;
import com.prd.yzy.fragment.WoFragment;
import com.prd.yzy.fragment.XHBFragment;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    //各个分块的页面
    private GuanJiaFragment gj;
    private MiShuFragment ms;
    private XHBFragment xhb;
    private WoFragment wo;

    //
    private View guanjiaLayout,xhbLayout,mishuLayout,woLayout;

    // 用于对Fragment进行管理
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
    }


    @Override
    public void onClick(View v) {

    }
}

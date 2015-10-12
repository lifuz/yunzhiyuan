package com.prd.testtv;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.prd.testtv.fragment.GJFragment;
import com.prd.testtv.fragment.GRFragment;
import com.prd.testtv.fragment.HBFragment;
import com.prd.testtv.fragment.MSFragment;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnFocusChangeListener,View.OnKeyListener{


    private FrameLayout main_pager;
    private View tab_gj, tab_xh, tab_ms, tab_gr;
    private List<Fragment> fragments;

    private boolean tab_flag = false;

    private SharedPreferences share;

    private GJFragment gjFragment;
    private HBFragment hbFragment;
    private MSFragment msFragment;
    private GRFragment grFragment;

    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar bar = getSupportActionBar();
        bar.hide();

        setContentView(R.layout.activity_main);



        // 将对象注册到事件总线中， ****** 注意要在onDestory中进行注销 ****
        EventBus.getDefault().register(this);

        share = getSharedPreferences("login",MODE_PRIVATE);

        String ogid = share.getString("ogid","");

        fm = getSupportFragmentManager();

        if (ogid.equals("")){
            startActivity(new Intent(MainActivity.this,LoginAcitivity.class));
        } else  {
//            if (!isTaskRoot()) {
//                finish();
//                return;
//            }
        }

        initView();
    }

    private void initView() {

        tab_gj = findViewById(R.id.tab_gj);
        tab_gr = findViewById(R.id.tab_gr);
        tab_ms = findViewById(R.id.tab_ms);
        tab_xh = findViewById(R.id.tab_hb);

        tab_gj.setFocusable(true);
//        tab_gj.setNextFocusUpId(0);
        tab_gr.setFocusable(true);
        tab_ms.setFocusable(true);
        tab_xh.setFocusable(true);

        tab_gj.setOnFocusChangeListener(this);
        tab_gr.setOnFocusChangeListener(this);
        tab_xh.setOnFocusChangeListener(this);
        tab_ms.setOnFocusChangeListener(this);

        tab_gj.setOnClickListener(this);
        tab_gr.setOnClickListener(this);
        tab_ms.setOnClickListener(this);
        tab_xh.setOnClickListener(this);

        tab_gj.setNextFocusUpId(R.id.tab_gj);

        tab_gj.setNextFocusRightId(R.id.wv_text);
//        tab_xh.setNextFocusRightId(R.id.tab_hb);
        tab_ms.setNextFocusRightId(R.id.tab_ms);

        tab_gj.setOnKeyListener(this);
        tab_gr.setOnKeyListener(this);
        tab_ms.setOnKeyListener(this);
        tab_xh.setOnKeyListener(this);


        main_pager = (FrameLayout) findViewById(R.id.main_pager);

        setTabSelection(0);


//        fragments = new ArrayList<>();
//
//        fragments.add(new GJFragment());
//        fragments.add(new HBFragment());
//        fragments.add(new MSFragment());
//        fragments.add(new GRFragment());




    }


    private void setTabSelection(int index) {
        clearColor();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragments(transaction);
        switch (index){
            case 0:

                tab_gj.setBackgroundColor(getResources().getColor(R.color.orange));
                if (gjFragment == null) {
                    gjFragment = new GJFragment();
                    transaction.add(R.id.main_pager,gjFragment);
                } else  {
                    transaction.show(gjFragment);
                }

                break;

            case 1:

                tab_xh.setBackgroundColor(getResources().getColor(R.color.orange));
                if (hbFragment == null) {
                    hbFragment = new HBFragment();
                    transaction.add(R.id.main_pager,hbFragment);
                } else  {
                    transaction.show(hbFragment);
                }

                break;

            case 2:

                tab_ms.setBackgroundColor(getResources().getColor(R.color.orange));
                if (msFragment == null) {
                    msFragment = new MSFragment();
                    transaction.add(R.id.main_pager,msFragment);
                } else  {
                    transaction.show(msFragment);
                }

                break;

            case 3:

                tab_gr.setBackgroundColor(getResources().getColor(R.color.orange));
                if (grFragment == null) {
                    grFragment = new GRFragment();
                    transaction.add(R.id.main_pager,grFragment);
                } else  {
                    transaction.show(grFragment);
                }

                break;
        }

        transaction.commit();
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction
     *            用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (gjFragment != null) {
            transaction.hide(gjFragment);
        }
        if (hbFragment != null) {
            transaction.hide(hbFragment);
        }
        if (msFragment != null) {
            transaction.hide(msFragment);
        }

        if (grFragment != null) {
            transaction.hide(grFragment);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.wv_btn:
//
//                String str = wv_text.getText().toString();
//
//                if(str.equals("")) {
//                    Toast.makeText(this,"请输入网址",Toast.LENGTH_SHORT).show();
//
//                    break;
//                }

//                if (!str.startsWith("http://")){
//                    str = "http://" + str;
//                }

//                Log.i("tag",str);
//
//                wv_web.loadUrl(str);

//                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {



        if (hasFocus) {
            clearColor();
            switch (v.getId()) {
                case R.id.tab_gj:
                    setTabSelection(0);

                    if (tab_flag) {
                        tab_xh.setFocusable(true);
                        tab_ms.setFocusable(true);
                        tab_gr.setFocusable(true);
                        tab_flag = false;
                    }

                    break;
                case R.id.tab_gr:

                    setTabSelection(3);

                    if (tab_flag) {
                        tab_xh.setFocusable(true);
                        tab_ms.setFocusable(true);
                        tab_gj.setFocusable(true);
                        tab_flag = false;
                    }

                    break;
                case R.id.tab_hb:
                    setTabSelection(1);

                    if (tab_flag) {
                        tab_gj.setFocusable(true);
                        tab_ms.setFocusable(true);
                        tab_gr.setFocusable(true);

                        tab_flag = false;
                    }

                    break;
                case R.id.tab_ms:
                    setTabSelection(2);

                    if (tab_flag) {
                        tab_xh.setFocusable(true);
                        tab_gj.setFocusable(true);
                        tab_gr.setFocusable(true);
                        tab_flag = false;
                    }

                    break;

            }
        }

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {


        switch (v.getId()) {

            case R.id.tab_gj:

                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {

//                    Log.i("tag", "点击右键");

                    tab_gj.setBackgroundColor(getResources().getColor(R.color.notice3));

                    tab_flag = true;

                    tab_xh.setFocusable(false);
                    tab_ms.setFocusable(false);
                    tab_gr.setFocusable(false);


                }

                break;

            case R.id.tab_gr:

                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {

                    Log.i("tag", "点击右键");

                    tab_gr.setBackgroundColor(getResources().getColor(R.color.notice3));

                    tab_flag = true;

                    tab_xh.setFocusable(false);
                    tab_ms.setFocusable(false);
                    tab_gj.setFocusable(false);

                }

                break;

            case R.id.tab_hb:

                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {

//                    Log.i("tag", "点击右键");

                    tab_xh.setBackgroundColor(getResources().getColor(R.color.notice3));

                    tab_flag = true;

                    tab_gj.setFocusable(false);
                    tab_ms.setFocusable(false);
                    tab_gr.setFocusable(false);

                }

                break;
//
//            case R.id.tab_ms:
//
//                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
//
////                    Log.i("tag", "点击右键");
//
//                    tab_ms.setBackgroundColor(getResources().getColor(R.color.notice3));
//
//                    tab_flag = true;
//
//                    tab_xh.setFocusable(false);
//                    tab_gj.setFocusable(false);
//                    tab_gr.setFocusable(false);
//
//                }
//
//                break;

        }


        return false;
    }


    private void clearColor(){


        tab_gj.setBackgroundColor(0);
        tab_gr.setBackgroundColor(0);
        tab_ms.setBackgroundColor(0);
        tab_xh.setBackgroundColor(0);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("提示");
                builder.setMessage("你确定要退出吗？");

                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        if (arg1 == DialogInterface.BUTTON_POSITIVE) {
                            arg0.cancel();
                        } else if (arg1 == DialogInterface.BUTTON_NEGATIVE) {

                            Log.i("tag", "退出");
                            EventBus.getDefault().post(new String(), "csuicide");

                        }
                    }
                };
                builder.setPositiveButton("取消", dialog);
                builder.setNegativeButton("确定", dialog);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // ****** 不要忘了进行注销 ****
        EventBus.getDefault().unregister(this);
    }


    //订阅事件
    @Subscriber(tag = "csuicide")
    private void csuicideMyself(String msg) {
        finish();

    }
}

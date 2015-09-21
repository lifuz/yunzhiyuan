package com.prd.testtv;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import com.prd.testtv.adapter.FragmentVerTicalPagerAdapter;
import com.prd.testtv.fragment.GJFragment;
import com.prd.testtv.fragment.GRFragment;
import com.prd.testtv.fragment.HBFragment;
import com.prd.testtv.fragment.MSFragment;
import com.prd.testtv.widget.VerticalViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnFocusChangeListener,View.OnKeyListener{


    private VerticalViewPager main_pager;
    private View tab_gj, tab_xh, tab_ms, tab_gr;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar bar = getSupportActionBar();
        bar.hide();

        setContentView(R.layout.activity_main);

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

        tab_gj.setNextFocusUpId(R.id.tab_gj);

        tab_gj.setNextFocusRightId(R.id.wv_text);

        tab_gj.setOnKeyListener(this);


        main_pager = (VerticalViewPager) findViewById(R.id.main_pager);
//        main_pager.set


        fragments = new ArrayList<>();

        fragments.add(new GJFragment());
        fragments.add(new HBFragment());
        fragments.add(new MSFragment());
        fragments.add(new GRFragment());

        main_pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), fragments));

//        tab_gj.setBackgroundColor(getResources().getColor(R.color.orange));
        main_pager.setCurrentItem(0);
//
//        main_pager.setOnPageChangeListener(new MyOnPageChangeListener());


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
                    tab_gj.setBackgroundColor(getResources().getColor(R.color.orange));
                    main_pager.setCurrentItem(0);

                    break;
                case R.id.tab_gr:

                    tab_gr.setBackgroundColor(getResources().getColor(R.color.orange));
                    main_pager.setCurrentItem(1);
                    break;
                case R.id.tab_hb:
                    tab_xh.setBackgroundColor(getResources().getColor(R.color.orange));
                    main_pager.setCurrentItem(2);
                    break;
                case R.id.tab_ms:
                    tab_ms.setBackgroundColor(getResources().getColor(R.color.orange));
                    main_pager.setCurrentItem(3);

                    break;

            }
        }

    }

    private class MyPagerAdapter extends FragmentVerTicalPagerAdapter {

        private List<Fragment> fragments;

        public MyPagerAdapter(FragmentManager manager, List<Fragment> fragments) {
            super(manager);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return (fragments == null || fragments.size() == 0) ? null
                    : fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments == null ? 0 : fragments.size();
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {


        switch (v.getId()) {

            case R.id.tab_gj:

                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {

//                    Log.i("tag", "点击右键");

                    tab_gj.setBackgroundColor(getResources().getColor(R.color.notice3));

                }

                break;

            case R.id.tab_gr:

                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {

//                    Log.i("tag", "点击右键");

                    tab_gr.setBackgroundColor(getResources().getColor(R.color.notice3));

                }

                break;

            case R.id.tab_hb:

                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {

//                    Log.i("tag", "点击右键");

                    tab_xh.setBackgroundColor(getResources().getColor(R.color.notice3));

                }

                break;

            case R.id.tab_ms:

                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {

//                    Log.i("tag", "点击右键");

                    tab_ms.setBackgroundColor(getResources().getColor(R.color.notice3));

                }

                break;



        }


        return false;
    }


    private void clearColor(){


        tab_gj.setBackgroundColor(0);
        tab_gr.setBackgroundColor(0);
        tab_ms.setBackgroundColor(0);
        tab_xh.setBackgroundColor(0);

    }
}

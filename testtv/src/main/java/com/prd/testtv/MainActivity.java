package com.prd.testtv;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.prd.testtv.adapter.FragmentVerTicalPagerAdapter;
import com.prd.testtv.fragment.GJFragment;
import com.prd.testtv.fragment.GRFragment;
import com.prd.testtv.fragment.HBFragment;
import com.prd.testtv.fragment.MSFragment;
import com.prd.testtv.widget.VerticalViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

//    private EditText wv_text;
//    private Button wv_btn;
//    private WebView wv_web;

    private VerticalViewPager main_pager;
    private View tab_gj,tab_xh,tab_ms,tab_gr;
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

        main_pager = (VerticalViewPager) findViewById(R.id.main_pager);


        fragments = new ArrayList<>();

        fragments.add(new GJFragment());
        fragments.add(new HBFragment());
        fragments.add(new MSFragment());
        fragments.add(new GRFragment());

        main_pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), fragments));

        tab_gj.setBackgroundColor(getResources().getColor(R.color.orange));
        main_pager.setCurrentItem(0);

        main_pager.setOnPageChangeListener(new MyOnPageChangeListener());


//        wv_btn = (Button) findViewById(R.id.wv_btn);
//        wv_text = (EditText) findViewById(R.id.wv_text);
//        wv_web = (WebView) findViewById(R.id.wv_web);
//
//        wv_btn.setOnClickListener(this);
//
//        wv_btn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    String str = wv_text.getText().toString();
//                    wv_web.loadUrl(str);
//                }
//            }
//        });
//
//        wv_web.setWebViewClient(new MyWebViewClient());
//
//        WebSettings webSettings = wv_web.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setSupportZoom(true);


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

    private class MyOnPageChangeListener implements VerticalViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            switch (position) {
                case 0:

                    tab_gj.setBackgroundColor(getResources().getColor(R.color.orange));
                    tab_xh.setBackgroundColor(0);
                    tab_ms.setBackgroundColor(0);
                    tab_gr.setBackgroundColor(0);

                    break;

                case 1:

                    tab_gj.setBackgroundColor(0);
                    tab_xh.setBackgroundColor(getResources().getColor(R.color.orange));
                    tab_ms.setBackgroundColor(0);
                    tab_gr.setBackgroundColor(0);

                    break;

                case 2:

                    tab_gj.setBackgroundColor(0);
                    tab_xh.setBackgroundColor(0);
                    tab_ms.setBackgroundColor(getResources().getColor(R.color.orange));
                    tab_gr.setBackgroundColor(0);

                    break;

                case 3:

                    tab_gj.setBackgroundColor(0);
                    tab_xh.setBackgroundColor(0);
                    tab_ms.setBackgroundColor(0);
                    tab_gr.setBackgroundColor(getResources().getColor(R.color.orange));

                    break;
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private class MyPagerAdapter extends FragmentVerTicalPagerAdapter {

        private List<Fragment> fragments;

        public MyPagerAdapter(FragmentManager manager,List<Fragment> fragments) {
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


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
            return true;
        }
    }
}

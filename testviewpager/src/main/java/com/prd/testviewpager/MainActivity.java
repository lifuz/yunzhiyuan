package com.prd.testviewpager;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.prd.testviewpager.fragment.GuanjiaFragment;
import com.prd.testviewpager.fragment.MiShuFragment;
import com.prd.testviewpager.fragment.WoFragment;
import com.prd.testviewpager.fragment.XHBFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试ViewPager
 */
public class MainActivity extends ActionBarActivity implements View.OnClickListener {


    private ViewPager pager;

    private View gj, xhb, ms, wo;

    private List<Fragment> fragments;

    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号

    /**
     * 页卡总数 *
     */
    private static final int pageSize = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //隐藏标题，这种为Android5.0的新特性：如果直接继承Activity则自动隐藏标题
        //如果继承ActionBarActivity则需使用如下方式，二不能使用以前的方法
        //以前的方法：requestWindowFeature(Window.FEATURE_NO_TITLE);
        ActionBar bar = getSupportActionBar();
        bar.hide();
        setContentView(R.layout.activity_main);


        initViews();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bottomMenu_guanjiaLayout:

                pager.setCurrentItem(0);
                break;

            case R.id.bottomMenu_xhbLayout:

                pager.setCurrentItem(1);
                break;

            case R.id.bottomMenu_mishuLayout:

                pager.setCurrentItem(2);
                break;

            case R.id.bottomMenu_woLayout:

                pager.setCurrentItem(3);
                break;
        }

    }

    public void initViews() {

        //初始化点击组件并给其添加点击事件
        gj = findViewById(R.id.bottomMenu_guanjiaLayout);
        gj.setOnClickListener(this);

        xhb = findViewById(R.id.bottomMenu_xhbLayout);
        xhb.setOnClickListener(this);

        ms = findViewById(R.id.bottomMenu_mishuLayout);
        ms.setOnClickListener(this);

        wo = findViewById(R.id.bottomMenu_woLayout);
        wo.setOnClickListener(this);

        //初始化ViewPager
        pager = (ViewPager) findViewById(R.id.main_pager);

        //初始化List，并把要使用的fragment放入list中
        fragments = new ArrayList<Fragment>();
        fragments.add(new GuanjiaFragment());
        fragments.add(new XHBFragment());
        fragments.add(new MiShuFragment());
        fragments.add(new WoFragment());
        //给ViewPager添加适配器
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), fragments));
        //指定进入页面首先显示的页面
        gj.setBackgroundColor(Color.parseColor("#45be45"));
        pager.setCurrentItem(0);
        //给ViewPager添加页面改变的监听事件
        pager.setOnPageChangeListener(new MyOnPageChangeListener());

    }

    /**
     * 为选项卡绑定监听器
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        public void onPageScrollStateChanged(int index) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageSelected(int index) {

            //切换页面时给tab进行相应的改变
            switch (index) {
                case 0:

                    gj.setBackgroundColor(Color.parseColor("#45be45"));
                    xhb.setBackgroundColor(Color.parseColor("#efefef"));
                    ms.setBackgroundColor(Color.parseColor("#efefef"));
                    wo.setBackgroundColor(Color.parseColor("#efefef"));
                    break;

                case 1:

                    gj.setBackgroundColor(Color.parseColor("#efefef"));
                    xhb.setBackgroundColor(Color.parseColor("#45be45"));
                    ms.setBackgroundColor(Color.parseColor("#efefef"));
                    wo.setBackgroundColor(Color.parseColor("#efefef"));
                    break;

                case 2:

                    gj.setBackgroundColor(Color.parseColor("#efefef"));
                    xhb.setBackgroundColor(Color.parseColor("#efefef"));
                    ms.setBackgroundColor(Color.parseColor("#45be45"));
                    wo.setBackgroundColor(Color.parseColor("#efefef"));
                    break;

                case 3:

                    gj.setBackgroundColor(Color.parseColor("#efefef"));
                    xhb.setBackgroundColor(Color.parseColor("#efefef"));
                    ms.setBackgroundColor(Color.parseColor("#efefef"));
                    wo.setBackgroundColor(Color.parseColor("#45be45"));
                    break;


            }

        }
    }


    /**
     * 定义适配器
     */
    class MyPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;

        public MyPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        /**
         * 得到每个页面
         */
        @Override
        public Fragment getItem(int arg0) {
            return (fragmentList == null || fragmentList.size() == 0) ? null
                    : fragmentList.get(arg0);
        }

        /**
         * 每个页面的title
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

        /**
         * 页面的总个数
         */
        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }
    }


}

package com.prd.yzy;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.prd.yzy.fragment.GuanJiaFragment;
import com.prd.yzy.fragment.MiShuFragment;
import com.prd.yzy.fragment.WoFragment;
import com.prd.yzy.fragment.XHBFragment;

import org.simple.eventbus.EventBus;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    //定义用于显示的fragment页面
    private GuanJiaFragment gj;
    private MiShuFragment ms;
    private XHBFragment xhb;
    private WoFragment wo;

    private View content;

    int realIndex = 0;

    float mPositionX;

    //
    private View guanjiaLayout, xhbLayout, mishuLayout, woLayout;

    // 用于对Fragment管理
    private FragmentManager fm;

    private static final int MOVE_DISTANCE = 40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //去除页面标题
//        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        //初始化组件
        initViews();
        // 第一次启动时选中第0个tab
        setTabSelection(0);
    }

    //对页面组件进行初始化
    public void initViews() {

        //初始化组件
        guanjiaLayout = findViewById(R.id.bottomMenu_guanjiaLayout);
        xhbLayout = findViewById(R.id.bottomMenu_xhbLayout);
        mishuLayout = findViewById(R.id.bottomMenu_mishuLayout);
        woLayout = findViewById(R.id.bottomMenu_woLayout);

        //给有点击事件的组件，添加点击事件
        guanjiaLayout.setOnClickListener(this);
        xhbLayout.setOnClickListener(this);
        mishuLayout.setOnClickListener(this);
        woLayout.setOnClickListener(this);

        content = findViewById(R.id.bottomMenu_content);
        content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.bottomMenu_content) {
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN :
                            mPositionX = event.getX();
                            break;
                        case MotionEvent.ACTION_MOVE :
                            final float currentX = event.getX();
//                            // 向左边滑动
//                            if (currentX - mPositionX <= -MOVE_DISTANCE ) {
//                                if(realIndex != 0){
//                                    realIndex = realIndex - 1;
//                                    setTabSelection(realIndex);
//                                }
//                                Toast.makeText(MainActivity.this,"向左滑动",Toast.LENGTH_SHORT).show();
//                            } else if (currentX - mPositionX >= MOVE_DISTANCE ) {
//                                if(realIndex != 3){
//                                    realIndex = realIndex - 1;
//                                    setTabSelection(realIndex);
//                                }
//                                Toast.makeText(MainActivity.this,"向右滑动",Toast.LENGTH_SHORT).show();
//                            }
                            break;
                    }
                    return true;
                }
                return false;
            }
        });

        //初始化fragment管理器
        fm = getFragmentManager();

    }

    /**
     * 将所有的fragment都置为隐藏状态
     *
     * @param transaction 对fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (gj != null) {
            transaction.hide(gj);
        }

        if (xhb != null) {
            transaction.hide(xhb);
        }

        if (ms != null) {
            transaction.hide(ms);
        }

        if (wo != null) {
            transaction.hide(wo);
        }
    }

    /**
     * 处理点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            //点击管家tab，选中第零个tab
            case R.id.bottomMenu_guanjiaLayout:

                setTabSelection(0);
                break;

            //点击小伙伴tab，选中第1个tab
            case R.id.bottomMenu_xhbLayout:

                setTabSelection(1);
                break;

            //点击秘书tab，选中第2个tab
            case R.id.bottomMenu_mishuLayout:

                setTabSelection(2);
                break;

            //点击我tab，选中第3个tab
            case R.id.bottomMenu_woLayout:

                setTabSelection(3);
                break;
        }

    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index 每个tab页对应的下标。0表示管家，1表示小伙伴，2表示秘书，3表示我。
     */
    private void setTabSelection(int index) {

        // 开启一个Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        //清除选中状态
        clearSelection();

        switch (index) {
            case 0:

                guanjiaLayout.setBackgroundColor(Color.parseColor("#45be45"));

                if (gj == null) {

                    //如果gj为空，则创建一个并添加到界面
                    gj = new GuanJiaFragment();
                    transaction.add(R.id.bottomMenu_content, gj);
                } else {
                    //如果gj不为空，则直接显示出来

                    transaction.show(gj);

                }

                realIndex = 0;
                break;

            case 1:
                xhbLayout.setBackgroundColor(Color.parseColor("#45be45"));
                if (xhb == null) {

                    //如果xhb为空，则创建一个并添加到界面
                    xhb = new XHBFragment();
                    transaction.add(R.id.bottomMenu_content, xhb);
                } else {
                    //如果xhb不为空，则直接显示出来
                    transaction.show(xhb);
                }

                realIndex = 1;
                break;

            case 2:
                mishuLayout.setBackgroundColor(Color.parseColor("#45be45"));
                if (ms == null) {

                    //如果ms为空，则创建一个并添加到界面
                    ms = new MiShuFragment();
                    transaction.add(R.id.bottomMenu_content, ms);
                } else {
                    //如果ms不为空，则直接显示出来
                    transaction.show(ms);
                }

                realIndex = 2;
                break;

            case 3:
                woLayout.setBackgroundColor(Color.parseColor("#45be45"));
                if (wo == null) {

                    //如果wo为空，则创建一个并添加到界面
                    wo = new WoFragment();
                    transaction.add(R.id.bottomMenu_content, wo);
                } else {
                    //如果wo不为空，则直接显示出来
                    transaction.show(wo);
                }

                realIndex = 3;
                break;

        }

        transaction.commit();

    }

    /**
     * 清除锁定状态
     */
    private void clearSelection(){
        guanjiaLayout.setBackgroundColor(Color.parseColor("#efefef"));
        xhbLayout.setBackgroundColor(Color.parseColor("#efefef"));
        mishuLayout.setBackgroundColor(Color.parseColor("#efefef"));
        woLayout.setBackgroundColor(Color.parseColor("#efefef"));
    }

    /**
     * 用于退出主页面
     *
     * @param keyCode
     * @param event
     * @return
     */

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
                            EventBus.getDefault().post(new String(),"csuicide");
                            MainActivity.this.finish();
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
}

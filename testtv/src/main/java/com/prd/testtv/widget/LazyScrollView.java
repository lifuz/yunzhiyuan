package com.prd.testtv.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;


/**
 * 瀑布流的组件
 *
 * 作者：李富 on 2015/8/10.
 * 邮箱：lifuzz@163.com
 */
public class LazyScrollView extends ScrollView {

    private View view;
    private Handler handler;


    public LazyScrollView(Context context) {
        super(context);
    }

    public LazyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LazyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 获取参考的View,主要是为了它的MeasuredHeight，然后和滚动条ScrollY+getHeight作比较
     */
    public void getView() {

        this.view = getChildAt(0);
        if(view != null) {
            init();
        }

    }

    private void init() {

        this.setOnTouchListener(onTouchListener);

        handler = new Handler() {

            /**
             * 收到滑动的消息，根据滑动的距离，方向滑动图片
             * @param msg
             */
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);


                switch (msg.what) {

                    case 1:
                        if(view.getMeasuredHeight() <= getScrollY()+getHeight() + 200){
                            if(onScrollListener != null) {
                                onScrollListener.onBotom();
                            }
                        } else if(getScrollY() == 0 ){
                            if(onScrollListener != null) {
                                onScrollListener.onTop();
                            }
                        } else {
                            if(onScrollListener != null) {
                                onScrollListener.onScroll();
                            }
                        }

                }
            }
        };

    }

    OnTouchListener onTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    break;
                //每当滑动接收，延迟发送消息让图片延迟滑动
                case MotionEvent.ACTION_UP:
                    if(view != null && onScrollListener != null) {
                        //延迟0.2s发送消息1
                        handler.sendMessageDelayed(handler.obtainMessage(1),200);
                    }

                    break;
            }

            return false;
        }
    };

    /**
     * 监听滑动的位置
     */
    public interface OnScrollListener {
        void onBotom();
        void onTop();
        void onScroll();
    }

    private  OnScrollListener onScrollListener;

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

}

package com.prd.testontouch;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {


    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.lv);

        List<String> list = new ArrayList<>();

        for(int i =0;i<20;i++){
            list.add("item"+i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.list_item,list);

        lv.setAdapter(adapter);

        lv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    //当手离开屏幕
                    case MotionEvent.ACTION_UP:

                        Toast.makeText(MainActivity.this,"MotionEvent.ACTION_UP",Toast.LENGTH_SHORT).show();

                        break;

                    //当手点击屏幕
                    case MotionEvent.ACTION_DOWN:
                        Toast.makeText(MainActivity.this,"MotionEvent.ACTION_DOWN",Toast.LENGTH_SHORT).show();
                        break;

                    //当手在屏幕上移动
                    case MotionEvent.ACTION_MOVE:
                        Toast.makeText(MainActivity.this,"MotionEvent.ACTION_MOVE",Toast.LENGTH_SHORT).show();
                        break;
                }

                return true;
            }
        });

    }


}

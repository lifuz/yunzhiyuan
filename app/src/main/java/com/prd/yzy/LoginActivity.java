package com.prd.yzy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.prd.yzy.utils.HttpUrls;
import com.prd.yzy.utils.MyUtil;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;

/**
 * 用户登录界面
 * <p/>
 * Created by 李富 on 2015/7/7.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText username, password;
    private TextView login;

    //定义访问异步访问网络的的对象和设置参数的对象
    private AsyncHttpClient client;
    private RequestParams params;

    //使用sharepreferences存储登录信息
    SharedPreferences share;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        //初始化组件
        init();

        String id = share.getString("suid", "");
        if (!id.equals("")) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }

    }

    /**
     * 初始化组件
     */
    public void init() {

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (TextView) findViewById(R.id.login);

        //设置登录按钮的点击事件
        login.setOnClickListener(this);

        //初始化异步访问网络对象和参数对象
        client = new AsyncHttpClient();

        params = new RequestParams();

        //获取sharepreferences

        share = getSharedPreferences("login", Activity.MODE_PRIVATE);
        editor = share.edit();

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login:
                //判断输入框是否为空，如果为空则用Toast提示用户，并直接返回，反之则进行下面步骤
                if (username.getText().toString().equals("") || password.getText().toString().equals("")) {
                    Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                //设置登录的两个参数，username，password
                params.put("username", username.getText().toString());


                Log.i("tag", username.getText().toString());
                try {
                    params.put("password", MyUtil.encrypt(password.getText().toString()));
                    Log.i("tag", MyUtil.encrypt(password.getText().toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //使用post提交访问接口，并用json的方式接收返回的json对象
                client.post(HttpUrls.http_login, params, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {

                            //获取suid的值
                            String suid = response.getString("suid");
                            //如果suid的值为负数的话，则是服务器出现问题。
                            if (suid.equals("-1")) {
                                //Toast告诉用户服务器出现问题
                                Toast.makeText(LoginActivity.this, "服务器出现错误", Toast.LENGTH_SHORT).show();
                                //如果suid的值为0则用户不存在或者用户名或密码错误
                            } else if (suid.equals("0")) {

                                Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                                //其他则登录成功，然后跳转到主页面
                            } else {
                                //把登录信息放入sharepreferences文件中
                                editor.putString("suid", suid);
                                editor.commit();
                                //跳转到mainActivity
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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

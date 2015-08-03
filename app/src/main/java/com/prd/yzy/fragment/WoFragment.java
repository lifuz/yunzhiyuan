package com.prd.yzy.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prd.yzy.LoginActivity;
import com.prd.yzy.R;
import com.prd.yzy.wo.JFAcitivity;

/**
 * Created by 李富 on 2015/7/7.
 */
public class WoFragment extends Fragment implements View.OnClickListener{

    private View wo_shezhi;

    SharedPreferences shared;
    SharedPreferences.Editor editor;

    private View wo_jfgl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate( R.layout.wo_layout,container,false);
        return layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        wo_shezhi = view.findViewById(R.id.wo_shezhi);
        wo_shezhi.setOnClickListener(this);

        wo_jfgl = view.findViewById(R.id.wo_jfgl);
        wo_jfgl.setOnClickListener(this);

        shared = getActivity().getSharedPreferences("login", Activity.MODE_PRIVATE);
        editor = shared.edit();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            //点击设置注销用户，注销之后跳转到登录页面
            case R.id.wo_shezhi:

                //把用户注销
                editor.putString("suid","");
                editor.commit();

                startActivity(new Intent(getActivity(), LoginActivity.class));

                break;

            case R.id.wo_jfgl:

                startActivity(new Intent(getActivity(), JFAcitivity.class));
        }

    }
}

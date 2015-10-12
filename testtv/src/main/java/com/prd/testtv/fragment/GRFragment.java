package com.prd.testtv.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.prd.testtv.LoginAcitivity;
import com.prd.testtv.R;

/**
 * 作者：李富 on 2015/9/18.
 * 邮箱：lifuzz@163.com
 */
public class GRFragment extends Fragment implements View.OnClickListener,View.OnFocusChangeListener {

    private Button login_out;

    private SharedPreferences share;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.gr_layout,container,false);
        return layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        login_out = (Button) view.findViewById(R.id.gr_login_out);

        login_out.setOnFocusChangeListener(this);

        login_out.setOnClickListener(this);

        share = getActivity().getSharedPreferences("login",getActivity().MODE_PRIVATE);

        editor = share.edit();

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            switch (v.getId()) {
                case R.id.gr_login_out:

                    login_out.setBackgroundColor(getResources().getColor(R.color.orange));

                    break;

            }
        } else {

            switch (v.getId()) {
                case R.id.gr_login_out:

                    login_out.setBackgroundColor(getResources().getColor(R.color.notice1));
            }

        }
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){

            case R.id.gr_login_out:

                editor.putString("sgid", "");
                editor.putString("username", "");
                editor.putString("password", "");
                editor.putString("opid", "");
                editor.putString("ogid", "");
                editor.commit();


                startActivity(new Intent(getActivity(), LoginAcitivity.class));
//                getActivity().finish();

                break;

        }

    }
}

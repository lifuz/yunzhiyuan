package com.prd.testtv.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prd.testtv.R;

/**
 * 作者：李富 on 2015/9/18.
 * 邮箱：lifuzz@163.com
 */
public class HBFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.hb_layout,container,false);
        return layout;
    }

}



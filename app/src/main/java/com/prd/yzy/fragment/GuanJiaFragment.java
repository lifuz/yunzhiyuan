package com.prd.yzy.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prd.yzy.R;

/**
 * Created by 李富 on 2015/7/7.
 */
public class GuanJiaFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate( R.layout.guanjia_layout,container,false);
        return layout;
    }
}
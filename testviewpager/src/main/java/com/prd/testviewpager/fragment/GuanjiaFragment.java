package com.prd.testviewpager.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prd.testviewpager.R;

/**
 * Created by 李富 on 2015/7/9.
 */
public class GuanjiaFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.guanjia_layout,container,false);

        return layout;

    }
}

package com.prd.yzy.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prd.yzy.R;

/**
 * Created by 李富 on 2015/7/7.
 */
public class WoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate( R.layout.wo_layout,container,false);
        return layout;
    }
}

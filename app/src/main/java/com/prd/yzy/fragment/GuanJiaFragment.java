package com.prd.yzy.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.prd.yzy.R;

/**
 * Created by 李富 on 2015/7/7.
 */
public class GuanJiaFragment extends Fragment implements View.OnClickListener {

    //定义相关组件
    private View gj_yg,gj_bom,gj_sb,gj_xs,gj_sc;
    private View gj_oa,gj_cw,gj_kc,gj_cg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate( R.layout.guanjia_layout,container,false);
        return layout;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //初始化组件，并给相应的组件，添加点击事件
        gj_yg = view.findViewById(R.id.gj_yg);
        gj_yg.setOnClickListener(this);

        gj_bom = view.findViewById(R.id.gj_bom);
        gj_bom.setOnClickListener(this);

        gj_sb = view.findViewById(R.id.gj_sb);
        gj_sb.setOnClickListener(this);

        gj_xs = view.findViewById(R.id.gj_xs);
        gj_xs.setOnClickListener(this);

        gj_sc = view.findViewById(R.id.gj_sc);
        gj_sc.setOnClickListener(this);

        gj_oa = view.findViewById(R.id.gj_oa);
        gj_oa.setOnClickListener(this);

        gj_cw = view.findViewById(R.id.gj_cw);
        gj_cw.setOnClickListener(this);

        gj_kc = view.findViewById(R.id.gj_kc);
        gj_kc.setOnClickListener(this);

        gj_cg = view.findViewById(R.id.gj_cg);
        gj_cg.setOnClickListener(this);


    }

    /**
     * 点击事件的处理
     * @param v
     */

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.gj_yg:

                Toast.makeText(getActivity(),"你点击了员工",Toast.LENGTH_SHORT).show();
                break;

            case R.id.gj_bom:

                Toast.makeText(getActivity(),"你点击了BOM",Toast.LENGTH_SHORT).show();
                break;

            case R.id.gj_sb:

                Toast.makeText(getActivity(),"你点击了设备",Toast.LENGTH_SHORT).show();
                break;

            case R.id.gj_xs:

                Toast.makeText(getActivity(),"你点击了销售",Toast.LENGTH_SHORT).show();
                break;

            case R.id.gj_sc:

                Toast.makeText(getActivity(),"你点击了生产",Toast.LENGTH_SHORT).show();
                break;

            case R.id.gj_oa:

                Toast.makeText(getActivity(),"你点击了OA",Toast.LENGTH_SHORT).show();
                break;

            case R.id.gj_cw:

                Toast.makeText(getActivity(),"你点击了财务",Toast.LENGTH_SHORT).show();
                break;

            case R.id.gj_kc:

                Toast.makeText(getActivity(),"你点击了库存",Toast.LENGTH_SHORT).show();
                break;

            case R.id.gj_cg:

                Toast.makeText(getActivity(),"你点击了采购",Toast.LENGTH_SHORT).show();
                break;
        }

    }
}

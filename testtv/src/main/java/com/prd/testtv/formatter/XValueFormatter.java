package com.prd.testtv.formatter;

import com.github.mikephil.charting.formatter.XAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * 作者：李富 on 2015/10/12.
 * 邮箱：lifuzz@163.com
 */
public class XValueFormatter implements XAxisValueFormatter {



//    public String getXValue(float value) {
//        return mFormat.format(value) + "次";
//    }

    @Override
    public String getXValue(String s, int i, ViewPortHandler viewPortHandler) {

        viewPortHandler.fitScreen();

        return "第 "+s + " 次";

    }
}

package com.prd.testtv.formatter;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;

import java.text.DecimalFormat;

/**
 * 作者：李富 on 2015/10/12.
 * 邮箱：lifuzz@163.com
 */
public class YValueFormatter implements YAxisValueFormatter {

    private DecimalFormat mFormat;

    public YValueFormatter(){
        mFormat = new DecimalFormat("###,###,###,##0");
    }

    @Override
    public String getFormattedValue(float v, YAxis yAxis) {
        return mFormat.format(v) + " s";
    }
}

package com.prd.yzy.widget;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.DatePicker;
import android.widget.EditText;

import com.prd.yzy.R;

import java.util.Calendar;

/**
 * Created by 李富 on 2015/8/3.
 */
public class DateEditText extends EditText {

    private Drawable date_pick;
    private Calendar calendar;

    public DateEditText(Context context) {
        this(context, null);
        init();
    }

    public DateEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
        init();
    }

    public DateEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        calendar =Calendar.getInstance();

        date_pick = getCompoundDrawables()[2];
        if (date_pick == null) {
            date_pick = getResources().getDrawable(R.drawable.date_button);
        }

        date_pick.setBounds(0, 0, date_pick.getIntrinsicWidth(), 50);

        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], date_pick,
                getCompoundDrawables()[3]);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));

                if (touchable) {
                    new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {

                            //更新EditText控件日期 小于10加0
                           setText(new StringBuilder().append(year).append("-")
                                   .append((month + 1) < 10 ? 0 + (month + 1) : (month + 1))
                                   .append("-")
                                   .append((day < 10) ? 0 + day : day).toString());
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH) ).show();
                }

            }
        }

        return super.onTouchEvent(event);
    }
}

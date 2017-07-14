package com.example.android.pick;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by zengzhi on 2017/7/14.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {


    private CallBacks callBacks;
    public interface CallBacks {
        void setDate(Calendar date);
    }

    public void setDateDialogFragmentListener(CallBacks callBacks) {

        this.callBacks = callBacks;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        //Date date = new GregorianCalendar()

        Calendar newDate = Calendar.getInstance();
        //设置每天的最大小时
        newDate.set(Calendar.YEAR, i);
        //设置每小时最大分钟
        newDate.set(Calendar.MONTH, i1);
        newDate.set(Calendar.DAY_OF_MONTH, i2);
        callBacks.setDate(newDate);
    }
}

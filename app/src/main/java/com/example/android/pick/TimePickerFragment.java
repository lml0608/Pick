package com.example.android.pick;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by zengzhi on 2017/7/14.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{


    private CallBacks callBacks;
    public interface CallBacks {
        void setTime(Calendar date);
    }

    public void setTimeDialogFragmentListener(CallBacks callBacks) {

        this.callBacks = callBacks;
    }

    public TimePickerFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user

        Calendar newDate = Calendar.getInstance();
        //设置每天的最大小时
        newDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
        //设置每小时最大分钟
        newDate.set(Calendar.MINUTE, minute);
        callBacks.setTime(newDate);
        Toast.makeText(getActivity(), "时间：" + "小时=" + hourOfDay + "分钟=" + minute, Toast.LENGTH_SHORT).show();
    }
}

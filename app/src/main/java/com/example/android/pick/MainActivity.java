package com.example.android.pick;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements TimePickerFragment.CallBacks,
        DatePickerFragment.CallBacks{

    private Button mTimeBtn;
    private Button mDateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTimeBtn = (Button)findViewById(R.id.time_pick);
        mDateBtn = (Button)findViewById(R.id.date_pick);
    }

    public void showTimePickDialog(View view) {

        TimePickerFragment newFragment = new TimePickerFragment();

        newFragment.setTimeDialogFragmentListener(this);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View view) {

        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setDateDialogFragmentListener(this);

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void setTime(Calendar date) {


        Date date1 = date.getTime();

        int hour24 = date.get(Calendar.HOUR_OF_DAY);   // HOUR-12小时制
        //int hour12 = date.get(Calendar.HOUR);
        // 当前分
        int minute = date.get(Calendar.MINUTE);   // 当前秒
        //int second = date.get(Calendar.SECOND);

        mTimeBtn.setText(hour24+":"+minute);


    }

    @Override
    public void setDate(Calendar date) {

        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH) + 1;
        int day = date.get(Calendar.DAY_OF_MONTH);
        mDateBtn.setText(year+":"+month+":"+day);

    }
}

package com.unioncloud.newpay.presentation.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.esummer.android.dialog.UniversalDialog;
import com.unioncloud.newpay.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by cwj on 16/8/29.
 */
public class DatePickerDialog extends UniversalDialog implements View.OnClickListener {

    public static DatePickerDialog newInstance(String title, Date initDate, Date minDate) {
        DatePickerDialog dialog = new DatePickerDialog();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(initDate.getTime());
        dialog.setInitYear(calendar.get(Calendar.YEAR));
        dialog.setInitMonth(calendar.get(Calendar.MONTH));
        dialog.setInitDay(calendar.get(Calendar.DAY_OF_MONTH));
        if (minDate != null) {
            dialog.setMinDate(minDate.getTime());
        }
        dialog.setTitle(title);
        return dialog;
    }

    private DatePicker datePicker;
    private TextView titleTv;
    private Button okBtn;
    private Button cancelBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_date_picker, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        datePicker = (DatePicker) view.findViewById(R.id.dialog_datePicker);
        initDatePicker();
        titleTv = (TextView) view.findViewById(R.id.dialog_title);
        titleTv.setText(getTitle());
        cancelBtn = (Button) view.findViewById(R.id.dialog_negative);
        cancelBtn.setOnClickListener(this);
        cancelBtn.setVisibility(View.GONE);
        okBtn = (Button) view.findViewById(R.id.dialog_positive);
        okBtn.setOnClickListener(this);
    }

    private void initDatePicker() {
        long minDate = getMinDate();
        if (minDate != -1) {
            datePicker.setMinDate(minDate);
        }
        datePicker.init(getInitYear(), getInitMonth(), getInitDay(), null);
    }

    public void setMinDate(long minDate) {
        getArguments().putLong("minDate", minDate);
    }

    public long getMinDate() {
        return getArguments().getLong("minDate", -1);
    }

    public void setTitle(String title) {
        getArguments().putString("title", title);
    }

    private String getTitle() {
        return getArguments().getString("title");
    }

    private void setInitYear(int year) {
        getArguments().putInt("year", year);
    }

    private int getInitYear() {
        return getArguments().getInt("year" , -1);
    }

    private void setInitMonth(int month) {
        getArguments().putInt("month", month);
    }

    private int getInitMonth() {
        return getArguments().getInt("month", -1);
    }

    private void setInitDay(int day) {
        getArguments().putInt("day", day);
    }

    private int getInitDay() {
        return getArguments().getInt("day", -1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_negative:
                returnResult(true);
                break;
            case R.id.dialog_positive:
                returnResult(true);
                break;
        }
    }

    private void returnResult(boolean hasChanged) {
        dismiss();
        Fragment target = getTargetFragment();
        if (target != null) {
            Intent intent = new Intent();
            intent.putExtra("year", datePicker.getYear());
            intent.putExtra("month", datePicker.getMonth() + 1);
            intent.putExtra("day", datePicker.getDayOfMonth());
            intent.putExtra("time", getTime(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth()));
            int resultCode = hasChanged ? Activity.RESULT_OK : Activity.RESULT_CANCELED;
            target.onActivityResult(getTargetRequestCode(), resultCode, intent);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    private long getTime(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTimeInMillis();
    }

//    private boolean dateChanged() {
//        int initYear = getInitYear();
//        int initMonth = getInitMonth();
//        int initDay = getInitDay();
//        if (datePicker.getYear() == initYear &&
//            datePicker.getMonth() == initMonth &&
//            datePicker.getDayOfMonth() == initDay) {
//            return false;
//        }
//        return true;
//    }
}

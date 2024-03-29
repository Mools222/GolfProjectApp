package com.example.golfprojectapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private EditText etLane, etDate, etTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String patternDate = "dd-MM-yyyy", patternTime = "HH:mm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();
        setupLocale();
        setupDateTime();
    }

    private void setupViews() {
        etLane = findViewById(R.id.et_lane);
        etDate = findViewById(R.id.et_date);
        etTime = findViewById(R.id.et_time);
    }

    private void setupLocale() {
        Locale locale = new Locale("da");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config, null);
    }

    private void setupDateTime() {
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        SimpleDateFormat simpleDateFormatDate = new SimpleDateFormat(patternDate, new Locale("da", "DK"));

        SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat(patternTime, new Locale("da", "DK"));

        Date tempDateTime = calendar.getTime();

        etDate.setText(simpleDateFormatDate.format(tempDateTime));
        etTime.setText(simpleDateFormatTime.format(tempDateTime));
    }

    public void chooseDate(View v) {
        setupDateTime();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                GregorianCalendar gregorianCalendar = new GregorianCalendar(year, monthOfYear, dayOfMonth, mHour, mMinute);
                Date chosenDate = gregorianCalendar.getTime();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(patternDate, new Locale("da", "DK"));
                etDate.setText(simpleDateFormat.format(chosenDate));

                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void chooseTime(View v) {
        setupDateTime();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                GregorianCalendar gregorianCalendar = new GregorianCalendar(mYear, mMonth, mDay, hourOfDay, minute);
                Date chosenTime = gregorianCalendar.getTime();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(patternTime, new Locale("da", "DK"));
                etTime.setText(simpleDateFormat.format(chosenTime));

                mHour = hourOfDay;
                mMinute = minute;
            }
        }, mHour, mMinute, true);

        timePickerDialog.show();
    }

    public void start(View view) {
        String courseNumber = etLane.getText().toString();
        GregorianCalendar chosenDateTime = new GregorianCalendar(mYear, mMonth, mDay, mHour, mMinute);

        if (!courseNumber.isEmpty()) {
            Intent intent = new Intent(this, ScoreActivity.class);
            intent.putExtra("Course", courseNumber);
            intent.putExtra("DateTime", chosenDateTime);
            startActivity(intent);
        } else
            Toast.makeText(this, "Vælg venligst bane-nummer", Toast.LENGTH_SHORT).show();
    }
}
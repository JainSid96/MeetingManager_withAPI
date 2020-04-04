package com.example.meeting_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Date;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Schedule extends AppCompatActivity {

    Button meetDate, meetTimestart, meetTimeend, meetSubmit, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        meetDate = findViewById(R.id.meetDate);
        meetTimeend = findViewById(R.id.meetTimeend);
        meetTimestart = findViewById(R.id.meetTimestart);
        meetSubmit = findViewById(R.id.meetSubmit);
        back = findViewById(R.id.back);

        Intent intent = getIntent();
        String date=intent.getStringExtra("date");
        meetDate.setText(date);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        meetTimestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mCurrentTime = Calendar.getInstance();
                int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Schedule.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        meetTimestart.setText( String.format("%02d:%02d", selectedHour, selectedMinute) );
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Start Time");
                mTimePicker.show();
            }
        });

        meetTimeend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Schedule.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        meetTimeend.setText( String.format("%02d:%02d", selectedHour, selectedMinute) );
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select End Time");
                mTimePicker.show();
            }
        });

        meetDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int mYear, mMonth, mDay;
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Schedule.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                meetDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        meetSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (meetTimestart.getText().toString().compareTo(meetTimeend.getText().toString()) >0) {
                    Toast.makeText(getApplicationContext(),"Start time ig greater than end time",Toast.LENGTH_LONG).show();
                    return;
                }
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Api.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                Api api = retrofit.create(Api.class);
                Call<List<Meeting>> call = api.getMeetings(meetDate.getText().toString());
                call.enqueue(new Callback<List<Meeting>>() {
                    @Override
                    public void onResponse(Call<List<Meeting>> call, Response<List<Meeting>> response) {
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat dt = new SimpleDateFormat("HH:mm");

                        List<Meeting> meetingList = response.body();
                        boolean available=false;
                        assert meetingList != null;
                        for (Meeting object : meetingList) {
                            try {
                                Date dst = dt.parse( object.getStart_time());
                                Date det = dt.parse( object.getEnd_time());
                                Date stime = dt.parse( meetTimestart.getText().toString());
                                Date etime = dt.parse( meetTimeend.getText().toString());

                                if((stime.after(dst) && stime.before(det))||(etime.after(dst) && etime.before(det))||stime.equals(dst)||etime.equals(det)){
                                    Toast.makeText(getApplicationContext(),"Slot Not Available",Toast.LENGTH_LONG).show();
                                    available=false;
                                    break;
                                }
                                else
                                {
                                    if ((dst.after(stime) && dst.before(etime))||(det.after(stime) && det.before(etime))) {
                                        Toast.makeText(getApplicationContext(),"Slot Not Available",Toast.LENGTH_LONG).show();
                                        available=false;
                                        break;
                                    }
                                    else
                                        available=true;
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        if(available)
                            Toast.makeText(getApplicationContext(),"Meeting Scheduled.",Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onFailure(Call<List<Meeting>> call, Throwable t) {
                        Log.e("temp", "onFailure: ");
                    }
                });
            }
        });
    }
}

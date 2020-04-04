package com.example.meeting_manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView date;
    Button prev,next,schedule;
    Date selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        date = findViewById(R.id.date);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        schedule = findViewById(R.id.schedule);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        selectedDate = Calendar.getInstance().getTime();
        final String strDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate);
        date.setText(strDate.replace("/", "-"));

        getMeetingsData(strDate);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                c.setTime(selectedDate);
                c.add(Calendar.DATE, 1);
                final Date nextDate = c.getTime();

                String strDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(nextDate);
                getMeetingsData(strDate);
                selectedDate=nextDate;
                date.setText(strDate.replace("/", "-"));
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                c.setTime(selectedDate);
                c.add(Calendar.DATE, -1);
                final Date previousDate = c.getTime();

                String strDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(previousDate);
                getMeetingsData(strDate);
                selectedDate=previousDate;
                date.setText(strDate.replace("/", "-"));
            }

        });

        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this ,Schedule.class);
                intent.putExtra("date",strDate);
                startActivity(intent);
            }
        });
    }

    private void getMeetingsData(String date)  {


        String strDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        if (date.compareTo(strDate) ==0) {
            schedule.setEnabled(true);

        }
        else if(date.compareTo(strDate) >0) {
            schedule.setEnabled(true);
        }
        else{
            schedule.setEnabled(false);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        Call<List<Meeting>> call = api.getMeetings(date);


        call.enqueue(new Callback<List<Meeting>>() {
            @Override
            public void onResponse(Call<List<Meeting>> call, Response<List<Meeting>> response) {
                final SimpleDateFormat dt = new SimpleDateFormat("HH:mm");
                Log.e("oop", "onResponse: "+call.request());
                List<Meeting> meetingList = response.body();
                Collections.sort(meetingList, new Comparator<Meeting>() {
                    @Override
                    public int compare(Meeting o1, Meeting o2) {

                        Date dst = null;
                        Date dst2 = null;
                        try {
                            dst = dt.parse( o1.getStart_time());
                            dst2 = dt.parse( o2.getStart_time());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return (dst.compareTo(dst2));
                    }


                });
                recyclerView.setAdapter(new Linker(meetingList, R.layout.meeting_tile, getApplicationContext()));
            }

            @Override
            public void onFailure(Call<List<Meeting>> call, Throwable t) {
                Log.e("Temp", "onFailure: ");
            }
        });
    }
}

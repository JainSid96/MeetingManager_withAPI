package com.example.meeting_manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Linker extends RecyclerView.Adapter<Linker.MovieViewHolder> {

    private List<Meeting> meetingList;
    private int rowLayout;



    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView description;

        public MovieViewHolder(View v) {
            super(v);
            time = (TextView) v.findViewById(R.id.tvTimeSlot);
            description = (TextView) v.findViewById(R.id.tvDescription);
        }
    }

    public Linker(List<Meeting> meetingList, int rowLayout, Context context) {
        this.meetingList = meetingList;
        this.rowLayout = rowLayout;

    }

    @Override
    public Linker.MovieViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        String startTime = meetingList.get(position).getStart_time();
        String endTime = meetingList.get(position).getEnd_time();

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            final Date dateObj = sdf.parse(startTime);

            startTime =new SimpleDateFormat("K:mm a").format(dateObj);
            final Date dateObj2 = sdf.parse(endTime);

            endTime =new SimpleDateFormat("K:mm a").format(dateObj2);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        holder.time.setText(startTime+" - "+endTime);
        holder.description.setText(meetingList.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return meetingList.size();
    }
}


package com.example.meeting_manager;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {


    String BASE_URL = "http://fathomless-shelf-5846.herokuapp.com/api/schedule?date=\"7/8/2015\"";

   @GET("schedule")
    Call<List<Meeting>> getMeetings(@Query("date") String data);
}
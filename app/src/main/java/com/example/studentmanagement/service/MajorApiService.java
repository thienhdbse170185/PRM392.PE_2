package com.example.studentmanagement.service;

import com.example.studentmanagement.model.Major;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MajorApiService {
    @GET("majors")
    Call<List<Major>> getMajors();

    @GET("majors/{id}")
    Call<Major> getMajorByID(@Path("id") String id);
}

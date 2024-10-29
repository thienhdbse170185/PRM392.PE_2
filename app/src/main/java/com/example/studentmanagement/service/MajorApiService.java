package com.example.studentmanagement.service;

import com.example.studentmanagement.model.Major;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MajorApiService {
    @GET("majors")
    Call<List<Major>> getMajors();

    @GET("majors/{id}")
    Call<Major> getMajorByID(@Path("id") String id);

    @POST("majors")
    Call<Major> addMajor(@Body Major major);

    @PUT("majors/{id}")
    Call<Major> updateMajor(@Path("id") String majorId, @Body Major major);

    @DELETE("majors/{id}")
    Call<Void> deleteMajor(@Path("id") String majorId);
}

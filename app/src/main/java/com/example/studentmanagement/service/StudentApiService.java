package com.example.studentmanagement.service;

import com.example.studentmanagement.model.Student;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface StudentApiService {
    @GET("students")
    Call<List<Student>> getStudents();

    @POST("students")
    Call<Student> addStudent(@Body Student student);

    @PUT("students/{id}")
    Call<Student> updateStudent(@Path("id") String studentId, @Body Student student);

    @DELETE("majors/{majorId}/students/{studentId}")
    Call<Void> deleteStudent(@Path("majorId") String majorId, @Path("studentId") String studentId);
}
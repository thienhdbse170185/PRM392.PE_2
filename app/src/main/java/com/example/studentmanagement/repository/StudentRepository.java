package com.example.studentmanagement.repository;

import com.example.studentmanagement.model.Major;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.service.MajorApiService;
import com.example.studentmanagement.service.StudentApiService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentRepository {
    private final StudentApiService studentApiService;
    private final MajorApiService majorApiService;

    public StudentRepository() {
        this.studentApiService = ApiClient.getClient().create(StudentApiService.class);
        this.majorApiService = ApiClient.getClient().create(MajorApiService.class);
    }

    public void getStudents(final StudentCallback<List<Student>> callback) {
        studentApiService.getStudents().enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Student> students = response.body();
                    fetchMajorsForStudents(students, callback);
                } else {
                    callback.onFailure(new Exception("Failed to retrieve students"));
                }
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    private void fetchMajorsForStudents(List<Student> students, final StudentCallback<List<Student>> callback) {
        // Create a HashMap to hold majorId to Major mapping to avoid multiple API calls for the same major
        HashMap<String, Major> majorCache = new HashMap<>();

        // Counter to keep track of remaining API calls
        final int[] totalMajorsToFetch = {0};

        for (Student student : students) {
            String majorId = student.getMajorId();
            if (majorCache.containsKey(majorId)) {
                // Set the major directly from the cache
                student.setMajor(majorCache.get(majorId));
            } else {
                // Fetch the major based on majorId
                totalMajorsToFetch[0]++;
                majorApiService.getMajorByID(majorId).enqueue(new Callback<Major>() {
                    @Override
                    public void onResponse(Call<Major> call, Response<Major> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Major major = response.body();
                            majorCache.put(majorId, major);
                            student.setMajor(major);
                        } else {
                            // Handle failure to fetch major
                            student.setMajor(null); // Null for major if failed
                        }
                        // Check if all major requests are completed
                        if (--totalMajorsToFetch[0] == 0) {
                            callback.onSuccess(students);
                        }
                    }

                    @Override
                    public void onFailure(Call<Major> call, Throwable t) {
                        // Handle failure to fetch major
                        student.setMajor(null); // Null for major if failed
                        // Check if all major requests are completed
                        if (--totalMajorsToFetch[0] == 0) {
                            callback.onSuccess(students);
                        }
                    }
                });
            }
        }

        // If there are no majors to fetch, immediately return the students
        if (totalMajorsToFetch[0] == 0) {
            callback.onSuccess(students);
        }
    }

    public void addStudent(Student student, final StudentCallback<Student> callback) {
        studentApiService.addStudent(student).enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("Failed to add student"));
                }
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public void updateStudent(String studentId, Student student, final StudentCallback<Student> callback) {
        studentApiService.updateStudent(studentId, student).enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("Failed to update student"));
                }
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public void deleteStudent(String majorId, String studentId, final StudentCallback<Void> callback) {
        studentApiService.deleteStudent(majorId, studentId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onFailure(new Exception("Failed to delete student"));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public interface StudentCallback<T> {
        void onSuccess(T result);
        void onFailure(Throwable t);
    }
}

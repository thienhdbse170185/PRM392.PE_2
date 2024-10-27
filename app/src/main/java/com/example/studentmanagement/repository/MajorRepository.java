package com.example.studentmanagement.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.studentmanagement.model.Major;
import com.example.studentmanagement.service.MajorApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MajorRepository {
    private MajorApiService apiService;
    private MutableLiveData<List<Major>> majors = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public MajorRepository() {
        apiService = ApiClient.getClient().create(MajorApiService.class);
    }

    public MutableLiveData<List<Major>> getMajors() {
        Call<List<Major>> call = apiService.getMajors();
        call.enqueue(new Callback<List<Major>>() {
            @Override
            public void onResponse(Call<List<Major>> call, Response<List<Major>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    majors.setValue(response.body());
                } else {
                    errorMessage.setValue("Failed to retrieve the list of majors");
                }
            }

            @Override
            public void onFailure(Call<List<Major>> call, Throwable t) {
                errorMessage.setValue("Error: " + t.getMessage());
            }
        });
        return majors;
    }

    public MutableLiveData<Major> getMajorByID(String id) {
        MutableLiveData<Major> majorData = new MutableLiveData<>();
        Call<Major> call = apiService.getMajorByID(id);
        call.enqueue(new Callback<Major>() {
            @Override
            public void onResponse(Call<Major> call, Response<Major> response) {
                if (response.isSuccessful() && response.body() != null) {
                    majorData.setValue(response.body());
                } else {
                    errorMessage.setValue("Failed to retrieve major by ID");
                }
            }

            @Override
            public void onFailure(Call<Major> call, Throwable t) {
                errorMessage.setValue("Error: " + t.getMessage());
            }
        });
        return majorData;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }
}

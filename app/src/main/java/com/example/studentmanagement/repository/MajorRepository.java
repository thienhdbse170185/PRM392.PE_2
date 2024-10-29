package com.example.studentmanagement.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.studentmanagement.model.Major;
import com.example.studentmanagement.service.MajorApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MajorRepository {
    private final MajorApiService apiService;

    public MajorRepository() {
        apiService = ApiClient.getClient().create(MajorApiService.class);
    }

    public void getMajors(final MajorCallback<List<Major>> callback) {
        apiService.getMajors().enqueue(new Callback<List<Major>>() {
            @Override
            public void onResponse(Call<List<Major>> call, Response<List<Major>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Major> majors = response.body();
                    callback.onSuccess(majors);
                } else {
                    callback.onFailure(new Exception("Failed to retrieve major list"));
                }
            }

            @Override
            public void onFailure(Call<List<Major>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public void addMajor(Major major, MajorCallback<Major> callback) {
        // Giả sử bạn có một instance `apiService` để thực hiện API calls
        apiService.addMajor(major).enqueue(new Callback<Major>() {
            @Override
            public void onResponse(@NonNull Call<Major> call, @NonNull Response<Major> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Throwable("Failed to add major"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Major> call, @NonNull Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public void updateMajor(String majorId, Major major, final MajorCallback<Major> callback) {
        apiService.updateMajor(majorId, major).enqueue(new Callback<Major>() {
            @Override
            public void onResponse(Call<Major> call, Response<Major> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("Failed to update major"));
                }
            }

            @Override
            public void onFailure(Call<Major> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public void deleteMajor(String majorId, final MajorCallback<Void> callback) {
        apiService.deleteMajor(majorId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onFailure(new Exception("Failed to delete major"));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public interface MajorCallback<T> {
        void onSuccess(T result);
        void onFailure(Throwable t);
    }
}

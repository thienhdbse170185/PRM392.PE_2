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
    private final MutableLiveData<List<Major>> majors = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public MajorRepository() {
        apiService = ApiClient.getClient().create(MajorApiService.class);
    }

    public MutableLiveData<List<Major>> getMajors() {
        Call<List<Major>> call = apiService.getMajors();
        call.enqueue(new Callback<List<Major>>() {
            @Override
            public void onResponse(@NonNull Call<List<Major>> call, @NonNull Response<List<Major>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    majors.setValue(response.body());
                } else {
                    errorMessage.setValue("Failed to retrieve the list of majors");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Major>> call, @NonNull Throwable t) {
                errorMessage.setValue("Error: " + t.getMessage());
            }
        });
        return majors;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
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

    public interface MajorCallback<T> {
        void onSuccess(T result);
        void onFailure(Throwable t);
    }
}

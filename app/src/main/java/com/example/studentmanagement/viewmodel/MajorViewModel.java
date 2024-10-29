package com.example.studentmanagement.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.studentmanagement.model.Major;
import com.example.studentmanagement.repository.MajorRepository;

import java.util.List;

public class MajorViewModel extends ViewModel {
    private MutableLiveData<List<Major>> majors;
    private final MajorRepository majorRepository;
    private final MutableLiveData<String> successMessage;
    private final MutableLiveData<String> errorMessage;

    public MajorViewModel() {
        majorRepository = new MajorRepository();
        majors = majorRepository.getMajors();
        successMessage = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    public LiveData<String> getSuccessMessage() {
        return successMessage;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<List<Major>> getMajors() {
        return majors;
    }

    public void addMajor(Major major) {
        majorRepository.addMajor(major, new MajorRepository.MajorCallback<Major>() {
            @Override
            public void onSuccess(Major result) {
                successMessage.setValue("Major added successfully!");
            }

            @Override
            public void onFailure(Throwable t) {
                errorMessage.setValue(t.getMessage());
            }
        });
    }
}

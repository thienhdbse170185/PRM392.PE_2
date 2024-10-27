package com.example.studentmanagement.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.studentmanagement.model.Major;
import com.example.studentmanagement.repository.MajorRepository;

import java.util.List;

public class MajorViewModel extends ViewModel {
    private MutableLiveData<List<Major>> majors;
    private MajorRepository majorRepository;

    public MajorViewModel() {
        majorRepository = new MajorRepository();
        majors = majorRepository.getMajors();
    }

    public LiveData<List<Major>> getMajors() {
        return majors;
    }
}

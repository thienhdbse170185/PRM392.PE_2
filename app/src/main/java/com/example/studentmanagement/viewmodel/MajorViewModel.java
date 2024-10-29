package com.example.studentmanagement.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.studentmanagement.model.Major;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.MajorRepository;
import com.example.studentmanagement.repository.StudentRepository;

import java.util.List;

public class MajorViewModel extends ViewModel {
    private MutableLiveData<List<Major>> majors;
    private final MajorRepository majorRepository;
    private final MutableLiveData<String> successMessage;
    private final MutableLiveData<String> errorMessage;
    private final MutableLiveData<Major> majorLiveData;
    private final MutableLiveData<Boolean> isLoading;
    private final MutableLiveData<Boolean> updateSuccess;
    private final StudentRepository studentRepository;

    public MajorViewModel() {
        this.majorRepository = new MajorRepository();
        this.majors = new MutableLiveData<>();
        this.successMessage = new MutableLiveData<>();
        this.errorMessage = new MutableLiveData<>();
        this.majorLiveData = new MutableLiveData<>();
        this.isLoading = new MutableLiveData<>(false);
        this.updateSuccess = new MutableLiveData<>();
        studentRepository = new StudentRepository();
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

    public LiveData<Major> getMajor() { return majorLiveData; }

    public LiveData<Boolean> getIsLoading() { return isLoading; }

    public LiveData<Boolean> getUpdateSuccess() { return updateSuccess; }

    public void loadMajors() {
        isLoading.setValue(true);
        majorRepository.getMajors(new MajorRepository.MajorCallback<List<Major>>() {
            @Override
            public void onSuccess(List<Major> result) {
                majors.setValue(result);
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Throwable t) {
                errorMessage.setValue(t.getMessage());
                isLoading.setValue(false);
            }
        });
    }

    public void addMajor(Major major) {
        isLoading.setValue(true);
        majorRepository.addMajor(major, new MajorRepository.MajorCallback<Major>() {
            @Override
            public void onSuccess(Major result) {
                loadMajors();
                successMessage.setValue("Major added successfully!");
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Throwable t) {
                errorMessage.setValue(t.getMessage());
                isLoading.setValue(false);
            }
        });
    }

    public void updateMajor(String majorId, Major major) {
        isLoading.setValue(true);
        majorRepository.updateMajor(majorId, major, new MajorRepository.MajorCallback<Major>() {
            @Override
            public void onSuccess(Major result) {
                List<Major> currentMajors = majors.getValue();
                if (currentMajors != null) {
                    for (int i = 0; i < currentMajors.size(); i++) {
                        if (currentMajors.get(1).getId().equals(majorId)) {
                            currentMajors.set(i, result);
                            break;
                        }
                    }
                    majors.setValue(currentMajors);
                }
                successMessage.setValue("Major update successfully!");
                isLoading.setValue(false);
                updateSuccess.setValue(true);
            }

            @Override
            public void onFailure(Throwable t) {
                errorMessage.setValue(t.getMessage());
                isLoading.setValue(false);
                updateSuccess.setValue(false);
            }
        });
    }

    public void deleteMajor(String majorId) {
        isLoading.setValue(true);
        studentRepository.getStudents(new StudentRepository.StudentCallback<List<Student>>() {
            @Override
            public void onSuccess(List<Student> result) {
                boolean flag = false;
                for (Student student : result) {
                    if (student.getMajor().getId().equals(majorId)) {
                        errorMessage.setValue("Student include this majorId exists! Please update their major before delete.");
                        isLoading.setValue(false);
                        updateSuccess.setValue(false);
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    majorRepository.deleteMajor(majorId, new MajorRepository.MajorCallback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            loadMajors();
                            successMessage.setValue("Major delete successfully!");
                            isLoading.setValue(false);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            errorMessage.setValue(t.getMessage());
                            isLoading.setValue(false);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Throwable t) {
                errorMessage.setValue(t.getMessage());
                isLoading.setValue(false);
                updateSuccess.setValue(false);
            }
        });
    }
}

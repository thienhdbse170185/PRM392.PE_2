package com.example.studentmanagement.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class StudentViewModel extends ViewModel {
    private final StudentRepository studentRepository;
    private final MutableLiveData<List<Student>> students;
    private final MutableLiveData<Student> studentLiveData;
    private final MutableLiveData<String> errorMessage;
    private final MutableLiveData<Boolean> isLoading;
    private final MutableLiveData<String> successMessage;
    private final MutableLiveData<Boolean> updateSuccess;

    public StudentViewModel() {
        this.studentRepository = new StudentRepository();
        this.students = new MutableLiveData<>();
        this.studentLiveData = new MutableLiveData<>();
        this.errorMessage = new MutableLiveData<>();
        this.isLoading = new MutableLiveData<>(false);
        this.successMessage = new MutableLiveData<>();
        updateSuccess = new MutableLiveData<>();
    }

    public LiveData<List<Student>> getStudents() {
        return students;
    }

    public LiveData<Student> getStudent() {
        return studentLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading; // Expose isLoading LiveData
    }

    public LiveData<String> getSuccessMessage() {
        return successMessage;
    }

    public LiveData<Boolean> getUpdateSuccess() {
        return updateSuccess;
    }

    public void loadStudents() {
        isLoading.setValue(true); // Set loading to true when fetching data
        studentRepository.getStudents(new StudentRepository.StudentCallback<List<Student>>() {
            @Override
            public void onSuccess(List<Student> result) {
                // Convert date format for each student
                List<Student> formattedStudents = new ArrayList<>();
                for (Student student : result) {
                    // Assuming the date field is called 'dateOfBirth'
                    student.setDate(DateUtils.formatDateString(student.getDate()));
                    formattedStudents.add(student);
                }
                students.setValue(formattedStudents);
                isLoading.setValue(false); // Set loading to false after fetching
            }

            @Override
            public void onFailure(Throwable t) {
                errorMessage.setValue(t.getMessage());
                isLoading.setValue(false); // Set loading to false on failure
            }
        });
    }

    public void addStudent(Student student) {
        isLoading.setValue(true); // Set loading to true when adding a student
        // Convert the date to ISO format before adding
        student.setDate(DateUtils.convertToISO8601(student.getDate()));

        studentRepository.addStudent(student, new StudentRepository.StudentCallback<Student>() {
            @Override
            public void onSuccess(Student result) {
                loadStudents(); // Reload students after adding
                successMessage.setValue("Student added successfully!");
                isLoading.setValue(false); // Set loading to false after the operation
            }

            @Override
            public void onFailure(Throwable t) {
                errorMessage.setValue(t.getMessage());
                isLoading.setValue(false); // Set loading to false on failure
            }
        });
    }

    public void updateStudent(String studentId, Student student) {
        isLoading.setValue(true);
        student.setDate(DateUtils.convertToISO8601(student.getDate()));

        studentRepository.updateStudent(studentId, student, new StudentRepository.StudentCallback<Student>() {
            @Override
            public void onSuccess(Student result) {
                List<Student> currentStudents = students.getValue();
                if (currentStudents != null) {
                    for (int i = 0; i < currentStudents.size(); i++) {
                        if (currentStudents.get(i).getId().equals(studentId)) {
                            currentStudents.set(i, result);
                            break;
                        }
                    }
                    students.setValue(currentStudents);
                }
                successMessage.setValue("Student updated successfully!");
                isLoading.setValue(false);
                updateSuccess.setValue(true); // Báo hiệu cập nhật thành công
            }

            @Override
            public void onFailure(Throwable t) {
                errorMessage.setValue(t.getMessage());
                isLoading.setValue(false);
                updateSuccess.setValue(false); // Báo hiệu cập nhật thất bại
            }
        });
    }


    public void deleteStudent(String majorId, String studentId) {
        isLoading.setValue(true); // Set loading to true when deleting a student
        studentRepository.deleteStudent(majorId, studentId, new StudentRepository.StudentCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                loadStudents(); // Reload students after deleting
                successMessage.setValue("Student delete successfully!");
                isLoading.setValue(false); // Set loading to false after the operation
            }

            @Override
            public void onFailure(Throwable t) {
                errorMessage.setValue(t.getMessage());
                isLoading.setValue(false); // Set loading to false on failure
            }
        });
    }
}

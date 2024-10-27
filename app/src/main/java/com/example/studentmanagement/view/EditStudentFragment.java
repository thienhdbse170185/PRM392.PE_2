package com.example.studentmanagement.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.studentmanagement.R;
import com.example.studentmanagement.databinding.FragmentEditStudentBinding;
import com.example.studentmanagement.model.Major;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.utils.DialogUtils;
import com.example.studentmanagement.utils.InputValidator;
import com.example.studentmanagement.viewmodel.MajorViewModel;
import com.example.studentmanagement.viewmodel.StudentViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditStudentFragment extends Fragment {

    private FragmentEditStudentBinding binding;
    private StudentViewModel studentViewModel;
    private MajorViewModel majorViewModel;
    private List<Major> majorList;
    private Student student;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditStudentBinding.inflate(inflater, container, false);

        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);
        majorViewModel = new ViewModelProvider(this).get(MajorViewModel.class);

        setupGenderSpinner();
        setupMajorSpinner();
        setupDateClickListener();
        setupSaveButtonClickListener();

        // Retrieve the student object passed from the previous fragment
        if (getArguments() != null) {
            student = (Student) getArguments().getSerializable("student");
            if (student != null) {
                bindStudentToUI();
            }
        }

        return binding.getRoot();
    }

    private void bindStudentToUI() {
        binding.etStudentName.setText(student.getName());
        binding.tvStudentDate.setText(student.getDate());
        binding.etStudentEmail.setText(student.getEmail());
        binding.etStudentAddress.setText(student.getAddress());

        // Set the gender spinner selection
        int genderPosition = student.getGender() ? 0 : 1; // Assuming 0 is for Male, 1 for Female
        binding.spinnerGender.setSelection(genderPosition);

        // Set the major spinner selection
        String majorId = student.getMajorId();
        majorViewModel.getMajors().observe(getViewLifecycleOwner(), majors -> {
            majorList = majors;
            List<String> majorNames = new ArrayList<>();
            for (Major major : majors) {
                majorNames.add(major.getName());
            }
            ArrayAdapter<String> majorAdapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_item, majorNames);
            binding.spinnerMajor.setAdapter(majorAdapter);

            for (int i = 0; i < majorList.size(); i++) {
                if (majorList.get(i).getId().equals(majorId)) {
                    binding.spinnerMajor.setSelection(i);
                    break;
                }
            }
        });
    }

    private void setupGenderSpinner() {
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.gender_array, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerGender.setAdapter(genderAdapter);
    }

    private void setupMajorSpinner() {
        majorViewModel.getMajors().observe(getViewLifecycleOwner(), majors -> {
            majorList = majors;

            List<String> majorNames = new ArrayList<>();
            for (Major major : majors) {
                majorNames.add(major.getName());
            }
            ArrayAdapter<String> majorAdapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_item, majorNames);
            majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinnerMajor.setAdapter(majorAdapter);
        });
    }

    private void setupDateClickListener() {
        binding.tvStudentDate.setOnClickListener(v -> DialogUtils.showDatePickerDialog(getContext(), binding.tvStudentDate));
    }

    private void setupSaveButtonClickListener() {
        binding.btnSave.setOnClickListener(v -> {
            Student updatedStudent = bindStudent();
            String validationError = InputValidator.validateStudent(updatedStudent);
            if (validationError != null) {
                Snackbar.make(v, validationError, Snackbar.LENGTH_LONG).show();
                return;
            }
            // Update the student and navigate back
            studentViewModel.updateStudent(updatedStudent.getId(), updatedStudent);
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_editStudentFragment_to_studentFragment);
        });
    }

    private Student bindStudent() {
        String id = student.getId(); // Keep the same ID for the existing student
        String name = binding.etStudentName.getText().toString().trim();
        String date = binding.tvStudentDate.getText().toString().trim(); // Get date from TextView
        boolean gender = binding.spinnerGender.getSelectedItem().toString().equals("Male");
        String email = binding.etStudentEmail.getText().toString().trim();
        String address = binding.etStudentAddress.getText().toString().trim();
        String selectedMajorName = binding.spinnerMajor.getSelectedItem().toString();

        String majorId = null;
        for (Major major : majorList) {
            if (major.getName().equals(selectedMajorName)) {
                majorId = major.getId();
                break;
            }
        }

        return new Student(id, name, date, gender, email, address, majorId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

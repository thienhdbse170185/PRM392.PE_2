package com.example.studentmanagement.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.studentmanagement.R;
import com.example.studentmanagement.databinding.FragmentAddStudentBinding;
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

public class AddStudentFragment extends Fragment {

    private FragmentAddStudentBinding binding;
    private StudentViewModel studentViewModel;
    private MajorViewModel majorViewModel;
    private List<Major> majorList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddStudentBinding.inflate(inflater, container, false);

        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);
        majorViewModel = new ViewModelProvider(this).get(MajorViewModel.class);
        majorViewModel.loadMajors();

        setupGenderSpinner();
        setupMajorSpinner();

        // Set up the click listener for the TextInputEditText to open the date picker
        binding.tvStudentDate.setOnClickListener(v -> DialogUtils.showDatePickerDialog(getContext(), binding.tvStudentDate));

        binding.btnSave.setOnClickListener(v -> {
            Student student = bindStudent();
            String validationError = InputValidator.validateStudent(student);
            if (validationError != null) {
                Snackbar.make(v, validationError, Snackbar.LENGTH_LONG).show();
                return;
            }
            // Save the student and navigate back
            studentViewModel.addStudent(student);
//            NavHostFragment.findNavController(this)
//                    .navigate(R.id.action_addStudentFragment_to_studentFragment);
            NavHostFragment.findNavController(this)
                    .popBackStack();
        });

        return binding.getRoot();
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

    private Student bindStudent() {
        String id = String.valueOf(System.currentTimeMillis());
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

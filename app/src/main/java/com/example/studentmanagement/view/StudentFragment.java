package com.example.studentmanagement.view;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.studentmanagement.R;
import com.example.studentmanagement.databinding.FragmentItemListBinding;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.view.adapter.MyStudentRecyclerViewAdapter;
import com.example.studentmanagement.viewmodel.StudentViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.List;

public class StudentFragment extends Fragment implements MyStudentRecyclerViewAdapter.OnStudentClickListener {
    private FragmentItemListBinding binding;
    private StudentViewModel studentViewModel;
    private MyStudentRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentItemListBinding.inflate(inflater, container, false);

        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);
        adapter = new MyStudentRecyclerViewAdapter(this);

        // Setup RecyclerView
        binding.list.setLayoutManager(new LinearLayoutManager(binding.list.getContext()));
        binding.list.setAdapter(adapter);

        // Setup OnClickListener for the add student button
        binding.btnAddStudent.setOnClickListener(v -> navigateToAddStudentFragment());

        // Observe student data from ViewModel
        studentViewModel.getStudents().observe(getViewLifecycleOwner(), this::updateStudentList);

        // Observe loading state
        studentViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.swipeRefreshLayout.setRefreshing(isLoading); // Update the SwipeRefreshLayout status
        });

        // Observe error messages for failure notifications
        studentViewModel.getErrorMessage().observe(getViewLifecycleOwner(), this::showErrorMessage);

        // Observe success operations to show a success Snackbar
        studentViewModel.getSuccessMessage().observe(getViewLifecycleOwner(), this::showSuccessMessage);

        // Setup SwipeRefreshLayout
        binding.swipeRefreshLayout.setOnRefreshListener(this::refreshStudentList);

        return binding.getRoot();
    }

    private void refreshStudentList() {
        studentViewModel.loadStudents(); // Call fetchStudents to refresh data
    }

    private void updateStudentList(List<Student> students) {
        if (students != null) {
            adapter.setStudentList(students);
        }
    }

    private void navigateToAddStudentFragment() {
        NavHostFragment.findNavController(StudentFragment.this)
                .navigate(R.id.action_studentFragment_to_addStudentFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Prevent memory leaks
    }

    @Override
    public void onEditClick(Student student) {
        // Navigate to edit screen
        Bundle bundle = new Bundle();
        bundle.putSerializable("student", (Serializable) student); // Pass student data
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_studentFragment_to_editStudentFragment, bundle);
    }

    @Override
    public void onDeleteClick(String majorId, String studentId) {
        // Create AlertDialog for confirmation
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this student?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // If confirmed, call ViewModel to delete the student
                    studentViewModel.deleteStudent(majorId, studentId);
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss()) // Close dialog if not confirmed
                .show();
    }

    private void showErrorMessage(String message) {
        if (message != null && !message.isEmpty()) {
            Snackbar snackbar = Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(requireContext(), com.google.android.material.R.color.design_default_color_error));
            snackbar.show();
        }
    }

    private void showSuccessMessage(String message) {
        if (message != null && !message.isEmpty()) {
            Snackbar snackbar = Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(requireContext(), com.google.android.material.R.color.design_default_color_on_primary));
            snackbar.show();
        }
    }
}

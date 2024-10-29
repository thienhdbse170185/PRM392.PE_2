package com.example.studentmanagement.view;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.studentmanagement.R;
import com.example.studentmanagement.databinding.FragmentMajorBinding;
import com.example.studentmanagement.model.Major;
import com.example.studentmanagement.view.adapter.MyMajorRecyclerViewAdapter;
import com.example.studentmanagement.viewmodel.MajorViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MajorFragment extends Fragment implements MyMajorRecyclerViewAdapter.OnMajorClickListener {
    private FragmentMajorBinding binding;
    private MajorViewModel majorViewModel;
    private MyMajorRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMajorBinding.inflate(inflater, container, false);

        majorViewModel = new ViewModelProvider(this).get(MajorViewModel.class);
        adapter = new MyMajorRecyclerViewAdapter(this);

        binding.majorList.setLayoutManager(new LinearLayoutManager(binding.majorList.getContext()));
        binding.majorList.setAdapter(adapter);

        binding.btnAddMajor.setOnClickListener(v -> showAddMajorDialog());

        majorViewModel.getMajors().observe(getViewLifecycleOwner(), this::updateMajorList);

        majorViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.swipeRefreshLayout.setRefreshing(isLoading); // Update the SwipeRefreshLayout status
        });

        // Observe error messages for failure notifications
        majorViewModel.getErrorMessage().observe(getViewLifecycleOwner(), this::showErrorMessage);

        // Observe success operations to show a success Snackbar
        majorViewModel.getSuccessMessage().observe(getViewLifecycleOwner(), this::showSuccessMessage);

        majorViewModel.getSuccessMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });

        majorViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Snackbar.make(binding.getRoot(), error, Snackbar.LENGTH_LONG).show();
            }
        });

        // Setup SwipeRefreshLayout
        binding.swipeRefreshLayout.setOnRefreshListener(this::refreshMajorList);

        majorViewModel.loadMajors();

        return binding.getRoot();
    }

    private void refreshMajorList() {
        majorViewModel.loadMajors();
    }

    private void updateMajorList(List<Major> majors) {
        if (majors != null) {
            adapter.setMajorList(majors);
        }
    }

    private void showAddMajorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_major, null);
        builder.setView(dialogView);

        final EditText etMajorName = dialogView.findViewById(R.id.etMajorName);

        builder.setTitle("Add New Major")
                .setPositiveButton("Save", (dialog, which) -> {
                    String majorName = etMajorName.getText().toString().trim();
                    if (!majorName.isEmpty()) {
                        Major major = new Major(majorName);
                        majorViewModel.addMajor(major);
                    } else {
                        Snackbar.make(binding.getRoot(), "Please enter a major name", Snackbar.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onEditClick(Major major) {
        showEditMajorDialog(major);
    }

    private void showEditMajorDialog(Major major) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_major, null);
        builder.setView(dialogView);

        final EditText etMajorName = dialogView.findViewById(R.id.etMajorName);
        etMajorName.setText(major.getName()); // Fill the EditText with the existing major name

        builder.setTitle("Edit Major")
                .setPositiveButton("Save", (dialog, which) -> {
                    String majorName = etMajorName.getText().toString().trim();
                    if (!majorName.isEmpty()) {
                        // Update the major with new name
                        major.setName(majorName);
                        majorViewModel.updateMajor(major.getId(), major); // Assumes updateMajor method exists in ViewModel
                    } else {
                        Snackbar.make(binding.getRoot(), "Please enter a major name", Snackbar.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onDeleteClick(String majorId) {
        // Create AlertDialog for confirmation
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this major?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // If confirmed, call ViewModel to delete the student
                    majorViewModel.deleteMajor(majorId);
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss()) // Close dialog if not confirmed
                .show();
    }

    private void showErrorMessage(String message) {
        if (message != null && !message.isEmpty()) {
            Snackbar snackbar = Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    private void showSuccessMessage(String message) {
        if (message != null && !message.isEmpty()) {
            Snackbar snackbar = Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
}
package com.example.studentmanagement.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentmanagement.databinding.FragmentItemMajorBinding;
import com.example.studentmanagement.model.Major;

import java.util.ArrayList;
import java.util.List;

public class MyMajorRecyclerViewAdapter extends RecyclerView.Adapter<MyMajorRecyclerViewAdapter.ViewHolder> {
    private final OnMajorClickListener listener;
    private List<Major> majors;

    public interface OnMajorClickListener {
        void onEditClick(Major major);
        void onDeleteClick(String majorId);
    }

    public MyMajorRecyclerViewAdapter(OnMajorClickListener listener) {
        this.listener = listener;
        this.majors = new ArrayList<>();
    }

    public void setMajorList(List<Major> newMajors) {
        this.majors = newMajors;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FragmentItemMajorBinding binding = FragmentItemMajorBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Major major = majors.get(position);
        holder.bind(major);
    }

    @Override
    public int getItemCount() {
        return majors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView majorName;
        public final ImageButton buttonEdit;
        public final ImageButton buttonDelete;
        public ViewHolder(FragmentItemMajorBinding binding) {
            super(binding.getRoot());
            this.majorName = binding.tvMajorName;
            this.buttonEdit = binding.btnEdit;
            this.buttonDelete = binding.btnDelete;
        }

        public void bind(Major major) {
            majorName.setText(major.getName());
            buttonEdit.setOnClickListener(v -> listener.onEditClick(major));
            buttonDelete.setOnClickListener(v -> listener.onDeleteClick(major.getId()));
        }
    }
}

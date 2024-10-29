package com.example.studentmanagement.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentmanagement.databinding.FragmentItemStudentBinding;
import com.example.studentmanagement.model.Student;

import java.util.ArrayList;
import java.util.List;

public class MyStudentRecyclerViewAdapter extends RecyclerView.Adapter<MyStudentRecyclerViewAdapter.ViewHolder> {
    private List<Student> students;
    private final OnStudentClickListener listener;

    public MyStudentRecyclerViewAdapter(OnStudentClickListener listener) {
        this.students = new ArrayList<>();
        this.listener = listener;
    }

    public interface OnStudentClickListener {
        void onEditClick(Student student);
        void onDeleteClick(String majorId, String studentId);
    }

    public void setStudentList(List<Student> newStudents) {
        this.students = newStudents;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FragmentItemStudentBinding binding = FragmentItemStudentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student student = students.get(position);
        holder.bind(student);
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView studentName;
        public final TextView studentEmail;
        public final TextView studentDate;
        public final TextView studentGender;
        public final TextView studentAddress;
        public final TextView studentMajor;
        public final ImageButton buttonEdit;
        public final ImageButton buttonDelete;

        public ViewHolder(FragmentItemStudentBinding binding) {
            super(binding.getRoot());
            this.studentEmail = binding.tvStudentEmail;
            this.studentName = binding.tvStudentName;
            this.studentDate = binding.tvStudentDate;
            this.studentGender = binding.tvStudentGender;
            this.studentAddress = binding.tvStudentAddress;
            this.studentMajor = binding.tvMajor;
            this.buttonEdit = binding.btnEdit;
            this.buttonDelete = binding.btnDelete;
        }

        public void bind(Student student) {
            studentName.setText(student.getName());
            studentEmail.setText(student.getEmail());
            studentDate.setText("DOB: " + student.getDate());
            if (student.getGender()) {
                studentGender.setText("Gender: Male");
            } else {
                studentGender.setText("Gender: Female");
            }
            studentAddress.setText(student.getAddress());
            studentMajor.setText("Major: " + student.getMajor().getName());
            buttonEdit.setOnClickListener(v -> listener.onEditClick(student));
            buttonDelete.setOnClickListener(v -> listener.onDeleteClick(student.getMajorId(), student.getId()));
        }
    }
}

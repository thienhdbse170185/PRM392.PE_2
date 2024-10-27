package com.example.studentmanagement.utils;

import com.example.studentmanagement.model.Student;

public class InputValidator {
    public static String validateStudent(Student student) {
        if (student.getName().isEmpty()) {
            return "Name cannot be empty.";
        }
        if (student.getDate().isEmpty()) {
            return "Date of birth cannot be empty.";
        }
//        if (student.getGender().isEmpty()) {
//            return "Gender cannot be empty.";
//        }
        if (student.getEmail().isEmpty() || !isValidEmail(student.getEmail())) {
            return "Please enter a valid email address.";
        }
        if (student.getAddress().isEmpty()) {
            return "Address cannot be empty.";
        }
        return null; // No validation errors
    }

    private static boolean isValidEmail(String email) {
        // Basic email validation regex
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }
}

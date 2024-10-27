package com.example.studentmanagement.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.TextView;

import java.util.Calendar;

public class DialogUtils {

    public static void showDatePickerDialog(Context context, TextView dateTextView) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format the selected date as needed (day/month/year)
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    dateTextView.setText(date); // Set the selected date to TextView
                },
                year, month, day);
        datePickerDialog.show();
    }
}

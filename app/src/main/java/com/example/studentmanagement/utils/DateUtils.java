package com.example.studentmanagement.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String formatDateString(String isoDate) {
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date date = isoFormat.parse(isoDate);
            SimpleDateFormat readableFormat = new SimpleDateFormat("dd/MM/yyyy");
            return readableFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return isoDate; // Trả về giá trị gốc nếu có lỗi
        }
    }

    public static String convertToISO8601(String date) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date parsedDate = inputFormat.parse(date);
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            return isoFormat.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return date; // Trả về giá trị gốc nếu có lỗi
        }
    }
}

package com.example.facturas.data.database.converter;

import androidx.room.TypeConverter;

import java.time.LocalDate;

public class DateConverter {

    private DateConverter() {
        // Private constructor to hide the implicit public one
    }

    @TypeConverter
    public static LocalDate toDate(String dateString) {
        return dateString == null ? null : LocalDate.parse(dateString);
    }

    @TypeConverter
    public static String toTimestamp(LocalDate date) {
        return date == null ? null : date.toString();
    }
}

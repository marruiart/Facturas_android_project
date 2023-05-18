package com.example.facturas.data.database.converter;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {

    private DateConverter() {
        // Private constructor to hide the implicit public one
    }

    @TypeConverter
    public static Date toDate(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}

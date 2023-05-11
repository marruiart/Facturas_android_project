package com.example.facturas.utils;

import androidx.room.TypeConverter;

import java.util.Date;

public class Converters {

    private Converters() {
        // Private constructor to hide the implicit public one
    }

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}

package com.yoonbae.planting.planner.data;

import androidx.room.TypeConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Converters {

    private Converters() {}

    @TypeConverter
    public static LocalDate toDate(String dateString) {
        if (dateString == null) {
            return null;
        }
        return LocalDate.parse(dateString);
    }

    @TypeConverter
    public static String toDateString(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.toString();
    }

    @TypeConverter
    public static LocalDateTime toDateTime(String dateString) {
        if (dateString == null) {
            return null;
        }
        return LocalDateTime.parse(dateString);
    }

    @TypeConverter
    public static String toDateTimeString(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        return date.toString();
    }
}

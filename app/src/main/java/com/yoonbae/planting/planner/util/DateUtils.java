package com.yoonbae.planting.planner.util;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.threeten.bp.LocalDate;

import java.time.YearMonth;

public abstract class DateUtils {
    public static LocalDate getLastDayOfTheMonth(CalendarDay date) {
        YearMonth yearMonth = YearMonth.from(convert2LocalDate(date));
        return LocalDate.ofEpochDay(yearMonth.atEndOfMonth().toEpochDay());
    }

    private static java.time.LocalDate convert2LocalDate(CalendarDay date) {
        return java.time.LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
    }
}

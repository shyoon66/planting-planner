package com.yoonbae.planting.planner.util;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.time.LocalDate;
import java.time.YearMonth;

public abstract class DateUtils {
    public static LocalDate convert2LocalDate(CalendarDay date) {
        return LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
    }

    public static LocalDate getLastDayOfTheMonth(CalendarDay date) {
        YearMonth yearMonth = YearMonth.from(convert2LocalDate(date));
        return yearMonth.atEndOfMonth();
    }
}

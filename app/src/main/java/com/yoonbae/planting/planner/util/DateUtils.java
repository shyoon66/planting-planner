package com.yoonbae.planting.planner.util;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.threeten.bp.LocalDate;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;

public abstract class DateUtils {
    public static LocalDate getFirstDayOfTheMonth(CalendarDay date) {
        return LocalDate.of(date.getYear(), date.getMonth(), 1);
    }

    public static LocalDate getLastDayOfTheMonth(CalendarDay date) {
        YearMonth yearMonth = YearMonth.from(convert2LocalDate(date));
        return LocalDate.ofEpochDay(yearMonth.atEndOfMonth().toEpochDay());
    }

    private static java.time.LocalDate convert2LocalDate(CalendarDay date) {
        return java.time.LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
    }

    public static long getAlarmTimeInMillis(LocalDateTime alarmDateTime, int alarmPeriod) {
        LocalDateTime now = LocalDateTime.now();
        while (now.isEqual(alarmDateTime) || now.isAfter(alarmDateTime)) {
            alarmDateTime = alarmDateTime.plusDays(alarmPeriod);
        }
        return alarmDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static long getAlarmPeriodInterval(int alarmPeriod) {
        return alarmPeriod * 24 * 60 * 60 * 1000;
    }
}

package com.yoonbae.planting.planner.data;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.time.LocalTime;

public class PlantEvent {
    private final Integer id;
    private final String name;
    private final String alarmMessage;
    private final CalendarDay calendarDay;

    public PlantEvent(Integer id, String name, LocalTime alarmTime, CalendarDay calendarDay) {
        this.id = id;
        this.name = name;
        this.alarmMessage = alarmTime + " 물주기 알람";
        this.calendarDay = calendarDay;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAlarmMessage() {
        return alarmMessage;
    }

    public CalendarDay getCalendarDay() {
        return calendarDay;
    }
}

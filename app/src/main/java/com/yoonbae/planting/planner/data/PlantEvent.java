package com.yoonbae.planting.planner.data;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.time.LocalTime;

public class PlantEvent {
    private Long id;
    private String name;
    private String alarmMessage;
    private CalendarDay calendarDay;

    public PlantEvent(Long id, String name, LocalTime alarmTime, CalendarDay calendarDay) {
        this.id = id;
        this.name = name;
        this.alarmMessage = alarmTime + " 물주기 알람";
        this.calendarDay = calendarDay;
    }

    public Long getId() {
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

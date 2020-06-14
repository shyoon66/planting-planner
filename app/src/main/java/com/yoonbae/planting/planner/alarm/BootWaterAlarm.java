package com.yoonbae.planting.planner.alarm;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.yoonbae.planting.planner.data.Plant;
import com.yoonbae.planting.planner.data.PlantDao;
import com.yoonbae.planting.planner.data.PlantDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BootWaterAlarm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWaterAlarm();
    }

    private void initWaterAlarm() {
        PlantDatabase plantDatabase = PlantDatabase.getDatabase(this);
        PlantDao plantDao = plantDatabase.plantDao();
        plantDao.findAll().observe(this, plants -> {
            for (Plant plant : plants) {
                String alarmDate = plant.getAlaramDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String alarmTime = plant.getAlaramDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));
                int alarmPeriod = plant.getAlarmPeriod();
                String name = plant.getName();
                int alarmId = plant.getId().intValue();
                setAlarm(alarmDate, alarmTime, alarmPeriod, name, alarmId);
            }
        });
    }

    private void setAlarm(String alarmDate, String alarmTime, int period, String name, int alarmId) {
        Map<String, Integer> alarmDateMap = getAlarmDate(alarmDate);
        Map<String, Integer> alarmTimeMap = getAlarmTime(alarmTime);
        if(!alarmDateMap.isEmpty() && !alarmTimeMap.isEmpty()) {
            int year = alarmDateMap.get("year");
            int month = alarmDateMap.get("month");
            int dayOfMonth = alarmDateMap.get("dayOfMonth");
            int hourOfDay = alarmTimeMap.get("hourOfDay");
            int minute = alarmTimeMap.get("minute");

            LocalDateTime alarmDateTime = LocalDateTime.of(year, month, dayOfMonth, hourOfDay, minute);
            LocalDateTime nowDateTime = LocalDateTime.now();
            while(alarmDateTime.isBefore(nowDateTime) || alarmDateTime.isEqual(nowDateTime))
                alarmDateTime.plusDays(period);

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth, hourOfDay, minute);
            long alarmTimeInMillis = calendar.getTimeInMillis();
            long intervalMillis = period * 24 * 60 * 60 * 1000;
            AlarmService.INSTANCE.registeringAnAlarm(getApplicationContext(), alarmTimeInMillis, intervalMillis, name, alarmId);
        }
    }

    private Map<String, Integer> getAlarmDate(String alarmDate) {
        String[] alarmDateArr = alarmDate.split("-");
        int year = Integer.parseInt(alarmDateArr[0]);
        int month = Integer.parseInt(alarmDateArr[1]);
        int dayOfMonth = Integer.parseInt(alarmDateArr[2]);
        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("year", year);
        resultMap.put("month", month);
        resultMap.put("dayOfMonth", dayOfMonth);
        return resultMap;
    }

    private Map<String, Integer> getAlarmTime(String alarmTime) {
        int hourOfDay = Integer.parseInt(alarmTime.substring(0, alarmTime.indexOf(":")));
        int minute = Integer.parseInt(alarmTime.substring(alarmTime.indexOf(":") + 2, alarmTime.length() - 1));
        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("hourOfDay", hourOfDay);
        resultMap.put("minute", minute);
        return resultMap;
    }
}

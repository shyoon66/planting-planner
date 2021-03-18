package com.yoonbae.planting.planner.alarm;

import android.content.Intent;

import androidx.lifecycle.LifecycleService;

import com.yoonbae.planting.planner.data.Plant;
import com.yoonbae.planting.planner.util.DateUtils;
import com.yoonbae.planting.planner.viewmodel.PlantRepository;

public class BootWaterAlarmService extends LifecycleService {
    private PlantRepository plantRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        plantRepository = new PlantRepository(getApplication());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initWaterAlarms();
        return super.onStartCommand(intent, flags, startId);
    }

    private void initWaterAlarms() {
        plantRepository.findPlantsWithWateringAlarmSet().observe(this, plants -> {
            for (Plant plant : plants) {
                initWaterAlarm(plant);
            }
        });
    }

    private void initWaterAlarm(Plant plant) {
        AlarmService.INSTANCE.cancelAlarm(getApplicationContext(), plant.getId());
        long alarmTimeInMillis = DateUtils.getAlarmTimeInMillis(plant.getAlarmDateTime(), plant.getAlarmPeriod());
        long intervalMillis = DateUtils.getAlarmPeriodInterval(plant.getAlarmPeriod());
        AlarmService.INSTANCE.registeringAnAlarm(getApplicationContext(), alarmTimeInMillis, intervalMillis, plant.getName(), plant.getId());
    }
}

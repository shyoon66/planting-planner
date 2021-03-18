package com.yoonbae.planting.planner.alarm;

import android.app.Application;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.yoonbae.planting.planner.data.Plant;
import com.yoonbae.planting.planner.util.DateUtils;
import com.yoonbae.planting.planner.viewmodel.PlantRepository;

public class BootWaterAlarmService extends AppCompatActivity {
    private PlantRepository plantRepository;

    public BootWaterAlarmService() {}

    public BootWaterAlarmService(Application application) {
        plantRepository = new PlantRepository(application);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWaterAlarms();
    }

    private void initWaterAlarms() {
        plantRepository.findPlantsWithWateringAlarmSet().observe(this, plants -> {
            for (Plant plant : plants) {
                initWaterAlarm(plant);
            }
        });
    }

    private void initWaterAlarm(Plant plant) {
        long alarmTimeInMillis = DateUtils.getAlarmTimeInMillis(plant.getAlarmDateTime(), plant.getAlarmPeriod());
        long intervalMillis = DateUtils.getAlarmPeriodInterval(plant.getAlarmPeriod());
        AlarmService.INSTANCE.registeringAnAlarm(getApplicationContext(), alarmTimeInMillis, intervalMillis, plant.getName(), plant.getId());
    }
}

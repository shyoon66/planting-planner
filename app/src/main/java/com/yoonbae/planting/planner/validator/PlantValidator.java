package com.yoonbae.planting.planner.validator;

import com.yoonbae.planting.planner.data.Plant;

import java.time.LocalDate;
import java.time.LocalTime;

public class PlantValidator implements Validator {
    @Override
    public <T> String validate(T object) {
        Plant plant = (Plant) object;

        String imagePath = plant.getImagePath();
        if (imagePath == null || imagePath.equals("")) {
            return "식물 사진을 등록해 주세요.";
        }

        String plantName = plant.getName();
        if (plantName == null || plantName.equals("")) {
            return "식물 이름을 입력해 주세요.";
        }

        boolean alarm = plant.isAlarm();
        if (alarm) {
            LocalDate alarmDate = plant.getAlarmDate();
            if (alarmDate == null) {
                return "알람 시작일을 설정해 주세요.";
            }

            LocalTime alarmTime = plant.getAlarmTime();
            if (alarmTime == null) {
                return "알람 시각을 설정해 주세요.";
            }

            int alarmPeriod = plant.getAlarmPeriod();
            if (alarmPeriod == 0) {
                return "알람주기를 설정해 주세요.";
            }
        }

        return "";
    }
}

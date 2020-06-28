package com.yoonbae.planting.planner.validator;

import com.yoonbae.planting.planner.data.Plant;

import java.time.LocalDateTime;

public class PlantValidator implements Validator {
    @Override
    public <T> String validate(T object) {
        Plant plant = (Plant) object;

        String imagePath = plant.getImagePath();
        if (imagePath == null || imagePath.equals("")) {
            return "식물 사진은 필수 입력입니다.";
        }

        String plantName = plant.getName();
        if (plantName == null || plantName.equals("")) {
            return "식물 이름은 필수 입력입니다.";
        }

        boolean alarm = plant.isAlarm();
        if (alarm) {
            LocalDateTime alarmDateTime = plant.getAlarmDateTime();
            if (alarmDateTime == null) {
                return "알람시작일과 알람 시각은 필수 입력입니다.";
            }

            int alarmPeriod = plant.getAlarmPeriod();
            if (alarmPeriod == 0) {
                return "알람주기는 필수 입력입니다.";
            }
        }

        return "";
    }
}

package com.yoonbae.planting.planner.validator;

import com.yoonbae.planting.planner.data.Plant;

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

        if (plantName.length() > 50) {
            return "식물 이름은 50자 이하로 입력할 수 있습니다.";
        }

        String desc = plant.getDesc();
        if (desc.length() > 100) {
            return "식물 설명은 100자 이하로 입력할 수 있습니다.";
        }

        if (plant.isAlarm()) {
            if (plant.getAlarmDate() == null) {
                return "알람 시작일을 설정해 주세요.";
            }
            if (plant.getAlarmTime() == null) {
                return "알람 시각을 설정해 주세요.";
            }
            if (plant.getAlarmPeriod() == 0) {
                return "알람주기를 설정해 주세요.";
            }
        }

        return "";
    }
}

package com.yoonbae.planting.planner.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity(tableName = "PLANT")
public class Plant {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private String name;

    private String desc;

    private LocalDate adoptionDate;

    private boolean alarm;

    private LocalDateTime alarmDateTime;

    private int alarmPeriod;

    private String imagePath;

    private int alarmId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public LocalDate getAdoptionDate() {
        return adoptionDate;
    }

    public void setAdoptionDate(LocalDate adoptionDate) {
        this.adoptionDate = adoptionDate;
    }

    public boolean isAlarm() {
        return alarm;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }

    public LocalDateTime getAlarmDateTime() {
        return alarmDateTime;
    }

    public void setAlarmDateTime(LocalDateTime alarmDateTime) {
        this.alarmDateTime = alarmDateTime;
    }

    public int getAlarmPeriod() {
        return alarmPeriod;
    }

    public void setAlarmPeriod(int alarmPeriod) {
        this.alarmPeriod = alarmPeriod;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public LocalDate getAlarmDate() {
        return this.alarmDateTime.toLocalDate();
    }

    public LocalTime getAlarmTime() {
        return this.alarmDateTime.toLocalTime();
    }

    @NonNull
    @Override
    public String toString() {
        return "Plant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", adoptionDate=" + adoptionDate +
                ", alarm=" + alarm +
                ", alarmDateTime=" + alarmDateTime +
                ", alarmPeriod=" + alarmPeriod +
                ", imagePath='" + imagePath + '\'' +
                ", alarmId=" + alarmId +
                '}';
    }
}

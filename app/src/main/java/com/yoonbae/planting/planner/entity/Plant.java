package com.yoonbae.planting.planner.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(tableName = "PLANT")
public class Plant {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private String name;

    private String desc;

    private LocalDate adoptionDate;

    @ColumnInfo(name = "ALARM_YN")
    private boolean isAlarm;

    private LocalDateTime alaramDateTime;

    private String alarmPeriod;

    private String imagePath;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public LocalDateTime getAlaramDateTime() {
        return alaramDateTime;
    }

    public void setAlaramDateTime(LocalDateTime alaramDateTime) {
        this.alaramDateTime = alaramDateTime;
    }

    public String getAlarmPeriod() {
        return alarmPeriod;
    }

    public void setAlarmPeriod(String alarmPeriod) {
        this.alarmPeriod = alarmPeriod;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @NonNull
    @Override
    public String toString() {
        return "Plant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", adoptionDate=" + adoptionDate +
                ", alaramDateTime=" + alaramDateTime +
                ", alarmPeriod='" + alarmPeriod + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}

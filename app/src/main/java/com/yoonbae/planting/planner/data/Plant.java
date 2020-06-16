package com.yoonbae.planting.planner.data;

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
    private boolean alarm;

    private LocalDateTime alarmDateTime;

    private int alarmPeriod;

    private String imagePath;

    private int alarmId;

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

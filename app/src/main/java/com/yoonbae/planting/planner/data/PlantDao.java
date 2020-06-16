package com.yoonbae.planting.planner.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlantDao {

    @Insert
    void insert(Plant plant);

    @Update
    void update(Plant plant);

    @Delete
    void delete(Plant plant);

    @Query("SELECT * FROM PLANT p WHERE p.id = :id LIMIT 1")
    LiveData<Plant> findById(Long id);

    @Query("SELECT p.id FROM PLANT p ORDER BY p.id DESC LIMIT 1")
    Long findLatestPlantId();

    @Query("SELECT * FROM PLANT")
    LiveData<List<Plant>> findAll();

    @Query("SELECT * FROM PLANT P where p.ALARM_YN = 'Y'")
    List<Plant> findPlantsWithWateringAlarmSet();
}

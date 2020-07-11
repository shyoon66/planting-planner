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
    Long insert(Plant plant);

    @Update
    void update(Plant plant);

    @Delete
    void delete(Plant plant);

    @Query("SELECT * FROM PLANT p WHERE p.id = :id LIMIT 1")
    LiveData<Plant> findById(Integer id);

    @Query("SELECT * FROM PLANT")
    LiveData<List<Plant>> findAll();

    @Query("SELECT * FROM PLANT P where p.ALARM = 1")
    LiveData<List<Plant>> findPlantsWithWateringAlarmSet();
}

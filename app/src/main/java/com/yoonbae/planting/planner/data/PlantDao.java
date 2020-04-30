package com.yoonbae.planting.planner.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.yoonbae.planting.planner.data.Plant;

@Dao
public interface PlantDao {

    @Insert
    void insert(Plant plant);

    @Update
    void update(Plant plant);

    @Delete
    void delete(Plant plant);

    @Query("SELECT * FROM Plant p WHERE p.id = :id LIMIT 1")
    LiveData<Plant> findById(Long id);
}

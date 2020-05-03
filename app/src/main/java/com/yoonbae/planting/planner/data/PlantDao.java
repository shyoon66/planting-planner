package com.yoonbae.planting.planner.data;

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

    @Query("SELECT * FROM Plant p WHERE p.id = :id LIMIT 1")
    Plant findById(Long id);

    @Query("SELECT * FROM Plant p")
    List<Plant> findAll();
}

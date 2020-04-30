package com.yoonbae.planting.planner.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.yoonbae.planting.planner.entity.Plant;

import java.util.Optional;

@Dao
public interface PlantDao {

    @Insert
    void insert(Plant plant);

    @Update
    void update(Plant plant);

    @Delete
    void delete(Plant plant);

    @Query("SELECT * FROM Plant p WHERE p.id = :id")
    Optional<Plant> findById(int id);
}

package com.yoonbae.planting.planner.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.yoonbae.planting.planner.data.Plant;
import com.yoonbae.planting.planner.data.PlantDao;
import com.yoonbae.planting.planner.data.PlantDatabase;

import java.util.List;

import static com.yoonbae.planting.planner.data.PlantDatabase.databaseWriteExecutor;

public class PlantRepository {
    private PlantDao plantDao;
    private LiveData<List<Plant>> allPlants;
    private LiveData<List<Plant>> plantsWithWateringAlarmSet;

    public PlantRepository(Application application) {
        PlantDatabase plantDatabase = PlantDatabase.getDatabase(application);
        plantDao = plantDatabase.plantDao();
        allPlants = plantDao.findAll();
        plantsWithWateringAlarmSet = plantDao.findPlantsWithWateringAlarmSet();
    }

    public void insert(Plant plant) {
        databaseWriteExecutor.execute(() -> plantDao.insert(plant));
    }

    public void update(Plant plant) {
        databaseWriteExecutor.execute(() -> plantDao.update(plant));
    }

    public void delete(Plant plant) {
        databaseWriteExecutor.execute(() -> plantDao.delete(plant));
    }

    public LiveData<Plant> findById(Long id) {
        return plantDao.findById(id);
    }

    public LiveData<Long> findLatestPlantId() {
        return plantDao.findLatestPlantId();
    }

    public LiveData<List<Plant>> findAll() {
        return allPlants;
    }

    public LiveData<List<Plant>> findPlantsWithWateringAlarmSet() {
        return plantsWithWateringAlarmSet;
    }
}

package com.yoonbae.planting.planner.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.yoonbae.planting.planner.data.PlantDatabase.databaseWriteExecutor;

public class PlantRepository {
    private PlantDao plantDao;
    private LiveData<List<Plant>> allPlants;
    private LiveData<List<Plant>> plantsWithWateringAlarmSet;

    PlantRepository(Application application) {
        PlantDatabase plantDatabase = PlantDatabase.getDatabase(application);
        plantDao = plantDatabase.plantDao();
        allPlants = plantDao.findAll();
        plantsWithWateringAlarmSet = plantDao.findPlantsWithWateringAlarmSet();
    }

    void insert(Plant plant) {
        databaseWriteExecutor.execute(() -> plantDao.insert(plant));
    }

    void update(Plant plant) {
        databaseWriteExecutor.execute(() -> plantDao.update(plant));
    }

    void delete(Plant plant) {
        databaseWriteExecutor.execute(() -> plantDao.delete(plant));
    }

    LiveData<Plant> findById(Long id) {
        return plantDao.findById(id);
    }

    LiveData<Long> findLatestPlantId() throws ExecutionException, InterruptedException {
        return plantDao.findLatestPlantId();
    }

    LiveData<List<Plant>> findAll() {
        return allPlants;
    }

    LiveData<List<Plant>> findPlantsWithWateringAlarmSet() {
        return plantsWithWateringAlarmSet;
    }
}

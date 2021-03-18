package com.yoonbae.planting.planner.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.yoonbae.planting.planner.data.Plant;
import com.yoonbae.planting.planner.data.PlantDao;
import com.yoonbae.planting.planner.data.PlantDatabase;

import java.util.List;
import java.util.concurrent.Future;

import static com.yoonbae.planting.planner.data.PlantDatabase.databaseWriteExecutor;

public class PlantRepository {
    private final PlantDao plantDao;
    private final LiveData<List<Plant>> allPlants;
    private final LiveData<List<Plant>> plantsWithWateringAlarmSet;

    public PlantRepository(Application application) {
        PlantDatabase plantDatabase = PlantDatabase.getDatabase(application);
        plantDao = plantDatabase.plantDao();
        allPlants = plantDao.findAll();
        plantsWithWateringAlarmSet = plantDao.findPlantsWithWateringAlarmSet();
    }

    public Future<Long> insert(Plant plant) {
        return databaseWriteExecutor.submit(() -> plantDao.insert(plant));
    }

    public void update(Plant plant) {
        databaseWriteExecutor.execute(() -> plantDao.update(plant));
    }

    public void delete(Plant plant) {
        databaseWriteExecutor.execute(() -> plantDao.delete(plant));
    }

    public LiveData<Plant> findById(Integer id) {
        return plantDao.findById(id);
    }

    public LiveData<List<Plant>> findAll() {
        return allPlants;
    }

    public LiveData<List<Plant>> findPlantsWithWateringAlarmSet() {
        return plantsWithWateringAlarmSet;
    }

    public LiveData<List<Plant>> findByName(String name) {
        return plantDao.findByName(name);
    }
}

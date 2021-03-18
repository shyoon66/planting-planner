package com.yoonbae.planting.planner.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.yoonbae.planting.planner.data.Plant;

import java.util.List;
import java.util.concurrent.Future;

public class PlantViewModel extends AndroidViewModel {
    private final PlantRepository repository;
    private final LiveData<List<Plant>> allPlants;
    private final LiveData<List<Plant>> plantsWithWateringAlarmSet;

    public PlantViewModel(Application application) {
        super(application);
        repository = new PlantRepository(application);
        allPlants = repository.findAll();
        plantsWithWateringAlarmSet = repository.findPlantsWithWateringAlarmSet();
    }

    public Future<Long> insert(Plant plant) {
        return repository.insert(plant);
    }

    public void update(Plant plant) {
        repository.update(plant);
    }

    public void delete(Plant plant) {
        repository.delete(plant);
    }

    public LiveData<Plant> findById(Integer id) {
        return repository.findById(id);
    }

    public LiveData<List<Plant>> findAll() {
        return allPlants;
    }

    public LiveData<List<Plant>> findPlantsWithWateringAlarmSet() {
        return plantsWithWateringAlarmSet;
    }

    public LiveData<List<Plant>> findByName(String name) {
        return repository.findByName(name);
    }
}

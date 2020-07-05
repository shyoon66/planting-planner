package com.yoonbae.planting.planner.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.yoonbae.planting.planner.data.Plant;

import java.util.List;

public class PlantViewModel extends AndroidViewModel {
    private PlantRepository repository;
    private LiveData<List<Plant>> allPlants;
    private LiveData<List<Plant>> plantsWithWateringAlarmSet;

    public PlantViewModel(Application application) {
        super(application);
        repository = new PlantRepository(application);
        allPlants = repository.findAll();
        plantsWithWateringAlarmSet = repository.findPlantsWithWateringAlarmSet();
    }

    public void insert(Plant plant) {
        repository.insert(plant);
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

    public LiveData<Integer> findLatestPlantId() {
        return repository.findLatestPlantId();
    }

    public LiveData<List<Plant>> findAll() {
        return allPlants;
    }

    public LiveData<List<Plant>> findPlantsWithWateringAlarmSet() {
        return plantsWithWateringAlarmSet;
    }
}

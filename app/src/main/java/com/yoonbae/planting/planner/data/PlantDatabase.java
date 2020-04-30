package com.yoonbae.planting.planner.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Plant.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class PlantDatabase extends RoomDatabase {
    public abstract PlantDao plantDao();

    public static volatile PlantDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static PlantDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PlantDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PlantDatabase.class, "plant_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

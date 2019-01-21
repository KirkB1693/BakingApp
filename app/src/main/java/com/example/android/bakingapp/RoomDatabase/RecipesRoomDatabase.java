package com.example.android.bakingapp.RoomDatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.android.bakingapp.Data.RecipeContract;

import timber.log.Timber;

@Database(entities = {Recipes.class}, version = 1, exportSchema = false)
public abstract class RecipesRoomDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = RecipeContract.RecipesTable.RECIPE_TABLE_NAME;
    private static volatile RecipesRoomDatabase sInstance;

    public abstract RecipesDao recipesDao();

    public static RecipesRoomDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (RecipesRoomDatabase.class) {
                if (sInstance == null) {
                    Timber.d("Creating new database instance");
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            RecipesRoomDatabase.class, DATABASE_NAME)
                            .build();
                }
            }
        }
        Timber.d("Getting the database instance");
        return sInstance;
    }


}

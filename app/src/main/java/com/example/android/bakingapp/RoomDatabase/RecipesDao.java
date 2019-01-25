package com.example.android.bakingapp.RoomDatabase;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.android.bakingapp.Data.RecipeContract;

import java.util.List;

@Dao
public interface RecipesDao {

    @Query("SELECT * FROM " + RecipeContract.RecipesTable.RECIPE_TABLE_NAME)
    LiveData<List<Recipes>> loadAllRecipes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipes(List<Recipes> recipeDataList);

    @Query("SELECT * FROM " + RecipeContract.RecipesTable.RECIPE_TABLE_NAME)
    List<Recipes> getAllRecipes();
}


package com.example.android.bakingapp.Utilities;

import com.example.android.bakingapp.RoomDatabase.Recipes;

import java.util.List;

public interface SyncServiceSupport {
    List<Recipes> getRecipes();
}

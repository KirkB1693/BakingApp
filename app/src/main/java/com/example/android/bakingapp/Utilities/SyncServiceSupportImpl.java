package com.example.android.bakingapp.Utilities;

import android.content.Context;

import com.example.android.bakingapp.RoomDatabase.Recipes;
import com.example.android.bakingapp.RoomDatabase.RecipesDao;
import com.example.android.bakingapp.RoomDatabase.RecipesRoomDatabase;

import java.util.List;


public class SyncServiceSupportImpl implements SyncServiceSupport {

    private RecipesDao recipesDao;

    public SyncServiceSupportImpl(Context context) {

        recipesDao = RecipesRoomDatabase.getInstance(context).recipesDao();
        // getDatabase() from where we get DB instance.
        // .. and ..
        // public abstract TimeDao iTimeDao();  <= defined abstract type in RoomDatabase abstract class
    }

    @Override
    public List<Recipes> getRecipes() {
        return recipesDao.getAllRecipes();
    }

}

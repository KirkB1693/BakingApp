package com.example.android.bakingapp.ViewModels;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.android.bakingapp.RoomDatabase.Recipes;
import com.example.android.bakingapp.RoomDatabase.RecipesDao;
import com.example.android.bakingapp.RoomDatabase.RecipesRoomDatabase;

import java.util.List;

public class RecipesRepository {
    private final RecipesDao mRecipesDao;
    private final LiveData<List<Recipes>> mAllRecipes;

    RecipesRepository(Application application) {
        RecipesRoomDatabase db = RecipesRoomDatabase.getInstance(application);
        mRecipesDao = db.recipesDao();
        mAllRecipes = mRecipesDao.loadAllRecipes();

    }

    public void insertAllRecipes (List<Recipes> recipes) {
        new InsertAsyncTask(mRecipesDao).execute(recipes);
    }

    public LiveData<List<Recipes>> getAllRecipes() {
        return mAllRecipes;
    }

    private static class InsertAsyncTask extends AsyncTask<List<Recipes>, Void, Void> {

        private final RecipesDao mAsyncRecipesDao;

        InsertAsyncTask (RecipesDao recipesDao) {mAsyncRecipesDao = recipesDao;}

        @Override
        protected Void doInBackground(final List<Recipes>... lists) {
            mAsyncRecipesDao.insertRecipes(lists[0]);
            return null;
        }
    }

}

package com.example.android.bakingapp.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.android.bakingapp.RoomDatabase.Recipes;

import java.util.List;

public class RecipesViewModel extends AndroidViewModel {

    private final RecipesRepository mRepository;

    private LiveData<List<Recipes>> mAllRecipes;


    public RecipesViewModel(Application application) {
        super(application);
        mRepository = new RecipesRepository(application);
        mAllRecipes = mRepository.getAllRecipes();
    }

    public LiveData<List<Recipes>> getAllRecipes() { return mAllRecipes; }

    public void insertAllRecipes(List<Recipes> recipesList) {mRepository.insertAllRecipes(recipesList);}


}

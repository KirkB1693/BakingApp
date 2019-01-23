package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.Adapters.RecipeStepsRecyclerAdapter;
import com.example.android.bakingapp.IdlingResources.EspressoIdlingResource;
import com.example.android.bakingapp.RoomDatabase.Ingredients;
import com.example.android.bakingapp.RoomDatabase.Recipes;

import java.util.List;

public class RecipeStepsFragment extends Fragment {
    private RecipeStepsRecyclerAdapter mStepsRecyclerAdapter;
    private Recipes mRecipe;

    public RecipeStepsFragment () {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_steps, container, false);
        TextView ingredientsTextView = view.findViewById(R.id.ingredients_tv);
        EspressoIdlingResource.increment();
        ingredientsTextView.setText(buildIngredientsList());
        setupRecyclerView(view);
        EspressoIdlingResource.decrement();
        return view;
    }

    private void setupRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recipe_steps_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mStepsRecyclerAdapter);
        recyclerView.setHasFixedSize(true);
    }

    public void setStepsAdapter(RecipeStepsRecyclerAdapter stepsAdapter) {
        mStepsRecyclerAdapter = stepsAdapter;
    }

    public void setRecipe (Recipes recipe) {
        mRecipe = recipe;
    }

    private String buildIngredientsList() {
        List<Ingredients> ingredientsList = mRecipe.getIngredientsList();
        StringBuilder ingredientsListBuilder = new StringBuilder();
        for (int i = 0; i < ingredientsList.size(); i++) {
            Ingredients currentIngredient = ingredientsList.get(i);
            ingredientsListBuilder.append(currentIngredient.getQuantity());
            ingredientsListBuilder.append(" ");
            ingredientsListBuilder.append(currentIngredient.getMeasure().toUpperCase());
            ingredientsListBuilder.append(" - ");
            String ingredientName = currentIngredient.getIngredientName();
            ingredientName = ingredientName.substring(0 , 1).toUpperCase() + ingredientName.substring(1).toLowerCase();
            ingredientsListBuilder.append(ingredientName);
            if (i != ingredientsList.size()-1) {
                ingredientsListBuilder.append("\n");
            }
        }
        return String.valueOf(ingredientsListBuilder);
    }
}


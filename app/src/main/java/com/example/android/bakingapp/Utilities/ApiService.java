package com.example.android.bakingapp.Utilities;

import com.example.android.bakingapp.Constants.BakingConstants;
import com.example.android.bakingapp.JsonResponseModels.RecipesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET(BakingConstants.RECIPE_URL_ENDPOINT)
    Call<List<RecipesResponse>> getRecipes();

}

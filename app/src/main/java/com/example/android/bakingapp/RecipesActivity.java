package com.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.android.bakingapp.Adapters.SelectRecipeRecyclerAdapter;
import com.example.android.bakingapp.IdlingResources.EspressoIdlingResource;
import com.example.android.bakingapp.JsonResponseModels.Ingredient;
import com.example.android.bakingapp.JsonResponseModels.RecipesResponse;
import com.example.android.bakingapp.JsonResponseModels.Step;
import com.example.android.bakingapp.RoomDatabase.Ingredients;
import com.example.android.bakingapp.RoomDatabase.Recipes;
import com.example.android.bakingapp.RoomDatabase.Steps;
import com.example.android.bakingapp.Utilities.ApiClient;
import com.example.android.bakingapp.Utilities.ApiService;
import com.example.android.bakingapp.Utilities.ConnectedToInternet;
import com.example.android.bakingapp.Utilities.GridSpacesItemDecoration;
import com.example.android.bakingapp.Utilities.GridUtils;
import com.example.android.bakingapp.ViewModels.RecipesViewModel;
import com.example.android.bakingapp.Widget.BakingAppWidgetProvider;
import com.example.android.bakingapp.Widget.BakingAppWidgetService;
import com.example.android.bakingapp.databinding.ActivityRecipesBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

public class RecipesActivity extends AppCompatActivity  implements SelectRecipeRecyclerAdapter.RecipeItemClickListener{

    private ApiService mRecipesApiService;
    private List<RecipesResponse> mRecipesResponseList;
    private RecipesViewModel mRecipesViewModel;
    private ActivityRecipesBinding mSelectRecipeBinding;
    private List<Recipes> mRecipesList;
    private SelectRecipeRecyclerAdapter mSelectRecipeAdapter;
    private RecyclerView mRecyclerView;
    private static final int SPACING = 15;
    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private boolean mConfigureWidget;

    // The Idling Resource which will be null in production.
    @Nullable
    private IdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link EspressoIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = EspressoIdlingResource.getIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        mSelectRecipeBinding = DataBindingUtil.setContentView(this, R.layout.activity_recipes);
        mRecipesViewModel = ViewModelProviders.of(this).get(RecipesViewModel.class);
        mSelectRecipeAdapter = new SelectRecipeRecyclerAdapter(this, new ArrayList<Recipes>());
        EspressoIdlingResource.increment();

        mRecyclerView = mSelectRecipeBinding.selectRecipeRv;
        int noOfColumns = GridUtils.calculateNoOfColumns(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, noOfColumns ,LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new GridSpacesItemDecoration(noOfColumns, SPACING, true, 0));
        mSelectRecipeAdapter.setClickListener(this);

        Intent widgetIntent = getIntent();
        Bundle widgetExtras = widgetIntent.getExtras();
        if (widgetExtras != null) {
            mAppWidgetId = widgetExtras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            setResult(RESULT_CANCELED);
            mConfigureWidget = mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID;
        }

        if (ConnectedToInternet.isConnectedToInternet(this)) {
            loadRecipesFromWeb();
        }

        setupLiveDataObserver();

        EspressoIdlingResource.decrement();
    }

    private void setupLiveDataObserver() {
        //setup recipe list observer
        mRecipesViewModel.getAllRecipes().observe(this , new Observer<List<Recipes>>() {
            @Override
            public void onChanged(@Nullable final List<Recipes> recipesList) {
                EspressoIdlingResource.increment();
                mRecipesList = recipesList;
                if (recipesList != null) {
                    if (recipesList.size() > 0) {
                        showRecyclerView();
                        mSelectRecipeAdapter.setRecipesData(mRecipesList);
                        mRecyclerView.setAdapter(mSelectRecipeAdapter);

                    } else {
                        showEmptyState();
                        mSelectRecipeBinding.emptySelectRecipeTv.setText(getString(R.string.error_no_recipes_in_db));
                    }
                } else {
                    showEmptyState();
                    mSelectRecipeBinding.emptySelectRecipeTv.setText(getString(R.string.error_recipe_list_is_null));
                }
                EspressoIdlingResource.decrement();

            }
        });
    }

    private void loadRecipesFromWeb() {
        Retrofit retrofit = ApiClient.getClient();
        mRecipesApiService = retrofit.create(ApiService.class);

        showProgressBar();

        try {
            EspressoIdlingResource.increment();
            Call <List<RecipesResponse>> call;
                call = callGetRecipesApi();

            call.enqueue(new Callback<List<RecipesResponse>>() {
                @Override
                public void onResponse(@NonNull Call<List<RecipesResponse>> call, @NonNull Response<List<RecipesResponse>> response) {
                    mRecipesResponseList = response.body();
                    if (mRecipesResponseList != null) {
                        showRecyclerView();
                        saveWebDataToDatabase();
                    } else {
                        showEmptyState();
                        mSelectRecipeBinding.emptySelectRecipeTv.setText(getString(R.string.no_recipes_found));
                    }
                    EspressoIdlingResource.decrement();
                }

                @Override
                public void onFailure(@NonNull Call<List<RecipesResponse>> call, @NonNull Throwable t) {
                    Timber.d("Error Fetching Data! %s", t.getMessage());

                }
            });
        } catch (Exception e) {
            Timber.d("Error with Retrofit Call! %s",e.getMessage());
        }
    }

    private void saveWebDataToDatabase() {
        List<Recipes> recipesToAdd = new ArrayList<>();
        EspressoIdlingResource.increment();
        if (mRecipesResponseList != null) {
            for (int i = 0; i < mRecipesResponseList.size(); i++) {
                RecipesResponse currentRecipeResponse = mRecipesResponseList.get(i);
                int recipeId = currentRecipeResponse.getId();
                String recipeName = currentRecipeResponse.getName();
                int recipeServings = currentRecipeResponse.getServings();
                String recipeImage = currentRecipeResponse.getImage();
                List<Ingredients> currentIngredients = null;
                if (currentRecipeResponse.getIngredients() != null) {
                     currentIngredients = getCurrentIngredientsFromRecipeResponse(currentRecipeResponse);
                }

                List<Steps> currentSteps = null;
                if (currentRecipeResponse.getSteps() != null) {
                    currentSteps = getCurrentStepsFromRecipeResponse(currentRecipeResponse);
                }

                Recipes currentRecipe = new Recipes(recipeId, recipeName, recipeServings, recipeImage, currentIngredients, currentSteps);
                recipesToAdd.add(currentRecipe);
            }
        }

        if (recipesToAdd.size() > 0) {
            mRecipesViewModel.insertAllRecipes(recipesToAdd);
        }
        EspressoIdlingResource.decrement();
    }

    private List<Steps> getCurrentStepsFromRecipeResponse(RecipesResponse currentRecipeResponse) {
        List<Steps> stepsList = new ArrayList<>();
        for (int i = 0; i < currentRecipeResponse.getSteps().size(); i++) {
            Step currentStepResponse = currentRecipeResponse.getSteps().get(i);
            int stepNumber = currentStepResponse.getId();
            String stepShortDescription = currentStepResponse.getShortDescription();
            String stepDescription = currentStepResponse.getDescription();
            String stepVideoUrl = currentStepResponse.getVideoURL();
            String stepThumbnailUrl = currentStepResponse.getThumbnailURL();
            Steps newStep = new Steps(stepNumber,stepShortDescription, stepDescription, stepVideoUrl, stepThumbnailUrl);
            stepsList.add(newStep);
        }
        return stepsList;
    }

    private List<Ingredients> getCurrentIngredientsFromRecipeResponse(RecipesResponse currentRecipeResponse) {
        List<Ingredients> ingredientsList = new ArrayList<>();
        for (int i = 0; i < currentRecipeResponse.getIngredients().size(); i++) {
            Ingredient currentIngredient = currentRecipeResponse.getIngredients().get(i);
            double quantity = currentIngredient.getQuantity();
            String measure = currentIngredient.getMeasure();
            String ingredientName = currentIngredient.getIngredient();
            Ingredients newIngredient = new Ingredients(quantity, measure, ingredientName);
            ingredientsList.add(newIngredient);
        }
        return ingredientsList;
    }

    private void showRecyclerView() {
        mSelectRecipeBinding.progress.setVisibility(View.INVISIBLE);
        mSelectRecipeBinding.emptySelectRecipeTv.setVisibility(View.INVISIBLE);
        mSelectRecipeBinding.selectRecipeRv.setVisibility(View.VISIBLE);
    }

    private void showProgressBar() {
        mSelectRecipeBinding.progress.setVisibility(View.VISIBLE);
        mSelectRecipeBinding.emptySelectRecipeTv.setVisibility(View.INVISIBLE);
        mSelectRecipeBinding.selectRecipeRv.setVisibility(View.INVISIBLE);
    }

    private void showEmptyState() {
        mSelectRecipeBinding.progress.setVisibility(View.INVISIBLE);
        mSelectRecipeBinding.emptySelectRecipeTv.setVisibility(View.VISIBLE);
        mSelectRecipeBinding.selectRecipeRv.setVisibility(View.INVISIBLE);
    }

    private Call<List<RecipesResponse>> callGetRecipesApi() {
        return mRecipesApiService.getRecipes();
    }

    @Override
    public void onRecipeItemClick(int position) {
        Recipes currentRecipe = mRecipesList.get(position);

        EspressoIdlingResource.increment();
        // Update widgets to show proper ingredients list
        updateRecipeWidget(this, currentRecipe);
        SharedPreferences settings = getSharedPreferences(BakingAppWidgetService.RECIPE_TO_USE_ACTION,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(BakingAppWidgetService.RECIPE_TO_USE, position);
        editor.commit();


        // Configure widget
        if (mConfigureWidget) {
            Intent widgetResult = new Intent();
            widgetResult.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, widgetResult);
            finish();
        } else {

            // Start RecipeStepsActivity to display selected recipe's steps and ingredients
            Intent intent = new Intent(this, RecipeStepListActivity.class);
            intent.putExtra(RecipeStepListActivity.RECIPE_DATA_KEY, currentRecipe);
            startActivity(intent);
        }
        EspressoIdlingResource.decrement();
    }

    public static void updateRecipeWidget(Context context, Recipes recipe) {
        Intent intent = new Intent(context, BakingAppWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context, BakingAppWidgetProvider.class));
        if (ids != null && ids.length > 0) {
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            context.sendBroadcast(intent);
            BakingAppWidgetProvider.updateBakingAppWidgets(context, appWidgetManager, recipe, ids);
            appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.widget_ingredients_lv);
        }

    }
}

package com.example.android.bakingapp.Widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RoomDatabase.Ingredients;
import com.example.android.bakingapp.RoomDatabase.Recipes;
import com.example.android.bakingapp.Utilities.SyncServiceSupportImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BakingAppWidgetService extends RemoteViewsService {
    public static final String RECIPE_TO_USE = "recipe_to_use";
    public static final String RECIPE_TO_USE_ACTION = "recipe_to_use_action";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BakingAppRemoteViewsFactory(this.getApplicationContext());
    }
}

class BakingAppRemoteViewsFactory extends BroadcastReceiver implements RemoteViewsService.RemoteViewsFactory {
    public static final String RECIPE_TO_USE = "recipe_to_use";
    private static final String RECIPE_TO_USE_ACTION = "recipe_to_use_action";
    private Context mContext;
    private Recipes mRecipe;
    private int mRecipeIndex;
    private static List<Recipes> mRecipeList = new ArrayList<>();
    private static SyncServiceSupportImpl iSyncService;


    public BakingAppRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
        
    }



    private static void getFromDB() {

        try {
            mRecipeList = new AsyncTask<Void, Void, List<Recipes>>() {

                @Override
                protected List<Recipes> doInBackground(Void... voids) {
                    return iSyncService.getRecipes();
                }

            }.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        //init service
        iSyncService = new SyncServiceSupportImpl(mContext);
        // initialize database

    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        getFromDB();
        SharedPreferences settings = mContext.getSharedPreferences(RECIPE_TO_USE_ACTION,
                Context.MODE_PRIVATE);
        mRecipeIndex = settings.getInt(RECIPE_TO_USE, -1);

        if (mRecipeList != null && mRecipeList.size() != 0 && mRecipeIndex != -1) {
            mRecipe = mRecipeList.get(mRecipeIndex);
        } else {
            mRecipe = null;
        }
    }

    @Override
    public void onDestroy() {
    }


    @Override
    public int getCount() {
        if (mRecipe != null) {
            return mRecipe.getIngredientsList().size();
        }
        else {
            return 0;
        }
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the ListView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {
        if (mRecipe == null || mRecipe.getIngredientsList().size() == 0) {return null;}
        Ingredients currentIngredient = mRecipe.getIngredientsList().get(position);
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        remoteViews.setTextViewText(R.id.widget_item_quantity_tv, Double.toString(currentIngredient.getQuantity()));
        remoteViews.setTextViewText(R.id.widget_item_measure_tv, currentIngredient.getMeasure().toUpperCase());
        String ingredientName = currentIngredient.getIngredientName().substring(0 ,1).toUpperCase() + currentIngredient.getIngredientName().substring(1).toLowerCase();
        remoteViews.setTextViewText(R.id.widget_item_ingredient_tv, ingredientName);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra(RECIPE_TO_USE)) {
            mRecipeIndex = intent.getExtras().getInt(RECIPE_TO_USE);
        }
    }
}

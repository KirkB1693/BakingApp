package com.example.android.bakingapp.Widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RecipeStepListActivity;
import com.example.android.bakingapp.RoomDatabase.Recipes;

public class BakingAppWidgetProvider extends AppWidgetProvider {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static void updateBakingAppWidget(Context context, AppWidgetManager appWidgetManager, Recipes recipe, int appWidgetId) {
        RemoteViews remoteViews = getRecipeListRemoteView(context, recipe);

        remoteViews.setEmptyView(R.id.widget_ingredients_lv, R.id.empty_widget);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_ingredients_lv);
    }

    public static void updateBakingAppWidgets(Context context, AppWidgetManager appWidgetManager,
                                          Recipes recipe, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateBakingAppWidget(context, appWidgetManager, recipe, appWidgetId);
        }
    }


    /**
     * Creates and returns the RemoteViews to be displayed in the GridView mode widget
     *
     * @param context The context
     * @return The RemoteViews for the ListView mode widget
     */
    private static RemoteViews getRecipeListRemoteView(Context context, Recipes recipe) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        views.setTextViewText(R.id.widget_ingredients_title_tv, recipe.getRecipeName());
        // Set the BakingAppWidgetService intent to act as the adapter for the ListView
        Intent intent = new Intent(context, BakingAppWidgetService.class);
        intent.putExtra(BakingAppWidgetService.RECIPE_TO_USE, recipe.getRecipeId()-1);
        views.setRemoteAdapter(R.id.widget_ingredients_lv, intent);
        // Set the RecipeStepListActivity intent to launch when clicked
        Intent appIntent = new Intent(context, RecipeStepListActivity.class);
        appIntent.putExtra(RecipeStepListActivity.RECIPE_DATA_KEY, recipe);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_wrapper_ll, appPendingIntent);
        return views;
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_ingredients_lv);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

}

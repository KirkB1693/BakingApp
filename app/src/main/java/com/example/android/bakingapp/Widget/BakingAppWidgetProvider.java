package com.example.android.bakingapp.Widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RecipeStepListActivity;
import com.example.android.bakingapp.RoomDatabase.Ingredients;
import com.example.android.bakingapp.RoomDatabase.Recipes;

import java.util.List;

public class BakingAppWidgetProvider extends AppWidgetProvider {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static void updateBakingAppWidget(Context context, AppWidgetManager appWidgetManager, Recipes recipe, int appWidgetId) {
        RemoteViews remoteViews = getRecipeRemoteView(context, recipe, appWidgetId);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    public static void updateBakingAppWidgets(Context context, AppWidgetManager appWidgetManager,
                                          Recipes recipe, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateBakingAppWidget(context, appWidgetManager, recipe, appWidgetId);
        }
    }

    private static RemoteViews getRecipeRemoteView(Context context, Recipes recipe, int appWidgetId) {
        Intent intent = new Intent(context, RecipeStepListActivity.class);
        intent.putExtra(RecipeStepListActivity.RECIPE_DATA_KEY, recipe);

        PendingIntent pendingIntent = TaskStackBuilder.create(context).addNextIntentWithParentStack(intent).getPendingIntent(appWidgetId, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        //update text with recipe name and ingredients
        remoteViews.setTextViewText(R.id.widget_ingredients_title_tv, recipe.getRecipeName());
        remoteViews.setTextViewText(R.id.widget_ingredients_tv, buildIngredientsList(recipe));
        remoteViews.setOnClickPendingIntent(R.id.widget_ingredients_tv, pendingIntent);
        return remoteViews;
    }

    private static CharSequence buildIngredientsList(Recipes recipe) {
        List<Ingredients> ingredientsList = recipe.getIngredientsList();
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

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}

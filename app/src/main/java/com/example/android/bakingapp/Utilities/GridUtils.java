package com.example.android.bakingapp.Utilities;

import android.content.Context;
import android.util.DisplayMetrics;

import com.example.android.bakingapp.R;

public class GridUtils {
    private static int remaining;
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int posterWidth = (int) (context.getResources().getDimension(R.dimen.recipe_card_width) / displayMetrics.density);
        int noOfColumns = (int) (dpWidth / posterWidth );
        if (noOfColumns == 0){noOfColumns=1;}
        remaining = (int) (displayMetrics.widthPixels - (noOfColumns * context.getResources().getDimension(R.dimen.recipe_card_width)));
        if (remaining / (noOfColumns + 1) < ((noOfColumns+1))) {
            noOfColumns--;
            remaining = (int) (displayMetrics.widthPixels - (noOfColumns * context.getResources().getDimension(R.dimen.recipe_card_width)));
        }
        return noOfColumns;
    }

    private static int gridItemWidthInPixels(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float pixelWidth = displayMetrics.widthPixels;
        int noOfColumns = calculateNoOfColumns(context);
        return (int) (pixelWidth/noOfColumns);
    }

    public static int gridItemHeightInPixelsFromWidth(Context context) {
        // based on a 4x3 ratio have height equal to .75 times width
        return (int) (gridItemWidthInPixels(context) * .75);
    }

}
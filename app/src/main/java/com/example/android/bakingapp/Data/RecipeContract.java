package com.example.android.bakingapp.Data;

import android.net.Uri;
import android.provider.BaseColumns;

public class RecipeContract {

    private static final String CONTENT_AUTHORITY = "com.example.android.bakingapp";

    /*
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider for BakingApp.
     */
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /* Inner class that defines the table contents of the recipes table */
    public static final class RecipesTable implements BaseColumns {

        /* The base CONTENT_URI used to query the Recipe table from the content provider */
        private static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .build();

        /* Used internally as the name of our recipes table. */
        public static final String RECIPE_TABLE_NAME = "recipes";

        /* Recipe ID as returned by API, used to identify the recipe */
        public static final String COLUMN_RECIPE_ID = "recipe_id";

        public static final String COLUMN_RECIPE_NAME = "recipe_name";

        public static final String COLUMN_RECIPE_INGREDIENTS = "recipe_ingredients";

        public static final String COLUMN_RECIPE_STEPS = "recipe_steps";

        public static final String COLUMN_RECIPE_SERVINGS = "recipe_servings";

        public static final String COLUMN_RECIPE_IMAGE_URL = "recipe_image_url";

    }


    /* Inner class that defines the table contents of the ingredients table */
    public static final class IngedientsTable implements BaseColumns {
        /* Used internally as the name of our ingredients table. */
        public static final String INGREDIENTS_TABLE_NAME = "ingredients";

        /* child Id for Foreign Key, used to identify the parent recipe */
        public static final String COLUMN_RECIPE_ID = "recipe_id";

        public static final String COLUMN_QUANTITY = "quantity";

        public static final String COLUMN_MEASURE = "measure";

        public static final String COLUMN_INGREDIENT_NAME = "ingredient_name";
    }



    /* Inner class that defines the table contents of the steps table */
    public static final class StepsTable implements BaseColumns {
        /* Used internally as the name of our steps table. */
        public static final String STEPS_TABLE_NAME = "steps";


        /* child Id for Foreign Key, used to identify the parent recipe */
        public static final String COLUMN_RECIPE_ID = "recipe_id";

        public static final String COLUMN_STEP_NUMBER = "step_number";

        public static final String COLUMN_STEP_SHORT_DESCRIPTION = "short_description";

        public static final String COLUMN_STEP_DESCRIPTION = "description";

        public static final String COLUMN_STEP_VIDEO_URL = "video_url";

        public static final String COLUMN_STEP_THUMBNAIL_URL = "thumbnail_url";

        public static final String COLUMN_STEP_VIDEO = "video";
    }
}

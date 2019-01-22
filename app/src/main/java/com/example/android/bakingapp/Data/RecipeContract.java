package com.example.android.bakingapp.Data;

import android.provider.BaseColumns;

public class RecipeContract {

    /* Inner class that defines the table contents of the recipes table */
    public static final class RecipesTable implements BaseColumns {

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


}

package com.example.android.bakingapp.Data;

import android.net.Uri;
import android.provider.BaseColumns;

public class RecipeContract {

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.example.android.bakingapp";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "plants" directory
    public static final String PATH_RECIPES = "recipes";

    public static final long INVALID_RECIPE_ID = -1;

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

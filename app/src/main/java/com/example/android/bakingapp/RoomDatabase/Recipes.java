package com.example.android.bakingapp.RoomDatabase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.example.android.bakingapp.Data.RecipeContract;

import java.util.List;


@Entity(tableName = RecipeContract.RecipesTable.RECIPE_TABLE_NAME)
@TypeConverters(RecipesTypeConverters.class)
public class Recipes implements Parcelable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = RecipeContract.RecipesTable.COLUMN_RECIPE_ID)
    private int recipeId;

    @ColumnInfo(name = RecipeContract.RecipesTable.COLUMN_RECIPE_NAME)
    private String recipeName;

    @ColumnInfo(name = RecipeContract.RecipesTable.COLUMN_RECIPE_SERVINGS)
    private int recipeServings;

    @ColumnInfo(name = RecipeContract.RecipesTable.COLUMN_RECIPE_IMAGE_URL)
    private String recipeImage;

    @ColumnInfo(name = RecipeContract.RecipesTable.COLUMN_RECIPE_INGREDIENTS)
    private List<Ingredients> ingredientsList;

    @ColumnInfo(name = RecipeContract.RecipesTable.COLUMN_RECIPE_STEPS)
    private List<Steps> stepsList;

    public Recipes(@NonNull int recipeId, String recipeName, int recipeServings, String recipeImage, List<Ingredients> ingredientsList, List<Steps> stepsList) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeServings = recipeServings;
        this.recipeImage = recipeImage;
        this.ingredientsList = ingredientsList;
        this.stepsList = stepsList;
    }

    protected Recipes(Parcel in) {
        recipeId = in.readInt();
        recipeName = in.readString();
        recipeServings = in.readInt();
        recipeImage = in.readString();
        //noinspection unchecked
        ingredientsList = in.readArrayList(getClass().getClassLoader());
        //noinspection unchecked
        stepsList = in.readArrayList(getClass().getClassLoader());
    }

    public static final Creator<Recipes> CREATOR = new Creator<Recipes>() {
        @Override
        public Recipes createFromParcel(Parcel in) {
            return new Recipes(in);
        }

        @Override
        public Recipes[] newArray(int size) {
            return new Recipes[size];
        }
    };

    public static Creator<Recipes> getCreator() {return CREATOR;}

    @NonNull
    public int getRecipeId() {
        return this.recipeId;
    }

    public void setRecipeId(@NonNull int recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return this.recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public int getRecipeServings() {
        return this.recipeServings;
    }

    public void setRecipeServings(int recipeServings) {
        this.recipeServings = recipeServings;
    }

    public String getRecipeImage() {
        return this.recipeImage;
    }

    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }

    public List<Ingredients> getIngredientsList() {return this.ingredientsList;}

    public void setIngredientsList (List<Ingredients> ingredientsList) { this.ingredientsList = ingredientsList;}

    public List<Steps> getStepsList() {return this.stepsList;}

    public void setStepsList (List<Steps> stepsList) {this.stepsList = stepsList;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(recipeId);
        parcel.writeString(recipeName);
        parcel.writeInt(recipeServings);
        parcel.writeString(recipeImage);
        parcel.writeList(ingredientsList);
        parcel.writeList(stepsList);
    }
}
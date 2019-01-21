package com.example.android.bakingapp.RoomDatabase;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class RecipesTypeConverters {
    @TypeConverter
    public static List<Ingredients> stringToIngredientsList(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredients>>() {}.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public static String ingredientsListToString(List<Ingredients> list) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredients>>() {}.getType();
        return gson.toJson(list, type);
    }

    @TypeConverter
    public static List<Steps> stringToStepsList(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Steps>>() {}.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public static String stepsListToString(List<Steps> list) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Steps>>() {}.getType();
        return gson.toJson(list, type);
    }
}

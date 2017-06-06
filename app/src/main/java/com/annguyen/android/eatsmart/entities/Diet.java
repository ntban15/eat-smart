package com.annguyen.android.eatsmart.entities;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Created by annguyen on 6/4/2017.
 */

@IgnoreExtraProperties
public class Diet {
    private String title;
    private String dietType;
    private String excludedIngredients; //separated by comma
    private int maxCalories;
    private int minCalories;
    private int maxCarbs;
    private int minCarbs;
    private int maxFat;
    private int minFat;
    private int maxProtein;
    private int minProtein;
    private int currentCalories;
    private int currentCarbs;
    private int currentFat;
    private int currrentProtein;
    List<Long> recipeIds;

    public Diet() {}

    @Exclude
    public void addRecipe(Recipe recipe) {
        //check if recipe is already in the list
        if (!recipeIds.contains(recipe.getId())) {
            recipeIds.add(recipe.getId());
        }
    }
}

package com.annguyen.android.eatsmart.entities;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private int currentProtein;
    private List<Long> recipes = new ArrayList<>();

    public Diet() {}

    @Exclude
    public void addRecipe(Recipe recipe) {
        //check if recipe is already in the list
        if (!recipes.contains(recipe.getId())) {
            recipes.add(recipe.getId());
            currentCalories += recipe.getCalories();
            currentCarbs += recipe.getCarbsValue();
            currentFat += recipe.getFatValue();
            currentProtein += recipe.getProteinValue();
        }
    }

    @Exclude
    public void removeRecipe(Recipe recipe) {
        if (!recipes.contains(recipe.getId())) {
            recipes.remove(recipe.getId());
            currentCalories -= recipe.getCalories();
            currentCarbs -= recipe.getCarbsValue();
            currentFat -= recipe.getFatValue();
            currentProtein -= recipe.getProteinValue();
        }
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("dietType", dietType);
        result.put("excludedIngredients", excludedIngredients);
        result.put("maxCalories", maxCalories);
        result.put("minCalories", minCalories);
        result.put("maxCarbs", maxCarbs);
        result.put("minCarbs", minCarbs);
        result.put("maxFat", maxFat);
        result.put("minFat", minFat);
        result.put("maxProtein", maxProtein);
        result.put("minProtein", minProtein);
        result.put("currentCalories", currentCalories);
        result.put("currentCarbs", currentCarbs);
        result.put("currentFat", currentFat);
        result.put("currentProtein", currentProtein);
        result.put("recipes", recipes);

        return result;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDietType() {
        return dietType;
    }

    public void setDietType(String dietType) {
        this.dietType = dietType;
    }

    public String getExcludedIngredients() {
        return excludedIngredients;
    }

    public void setExcludedIngredients(String excludedIngredients) {
        this.excludedIngredients = excludedIngredients;
    }

    public int getMaxCalories() {
        return maxCalories;
    }

    public void setMaxCalories(int maxCalories) {
        this.maxCalories = maxCalories;
    }

    public int getMinCalories() {
        return minCalories;
    }

    public void setMinCalories(int minCalories) {
        this.minCalories = minCalories;
    }

    public int getMaxCarbs() {
        return maxCarbs;
    }

    public void setMaxCarbs(int maxCarbs) {
        this.maxCarbs = maxCarbs;
    }

    public int getMinCarbs() {
        return minCarbs;
    }

    public void setMinCarbs(int minCarbs) {
        this.minCarbs = minCarbs;
    }

    public int getMaxFat() {
        return maxFat;
    }

    public void setMaxFat(int maxFat) {
        this.maxFat = maxFat;
    }

    public int getMinFat() {
        return minFat;
    }

    public void setMinFat(int minFat) {
        this.minFat = minFat;
    }

    public int getMaxProtein() {
        return maxProtein;
    }

    public void setMaxProtein(int maxProtein) {
        this.maxProtein = maxProtein;
    }

    public int getMinProtein() {
        return minProtein;
    }

    public void setMinProtein(int minProtein) {
        this.minProtein = minProtein;
    }

    public int getCurrentCalories() {
        return currentCalories;
    }

    public void setCurrentCalories(int currentCalories) {
        this.currentCalories = currentCalories;
    }

    public int getCurrentCarbs() {
        return currentCarbs;
    }

    public void setCurrentCarbs(int currentCarbs) {
        this.currentCarbs = currentCarbs;
    }

    public int getCurrentFat() {
        return currentFat;
    }

    public void setCurrentFat(int currentFat) {
        this.currentFat = currentFat;
    }

    public int getCurrentProtein() {
        return currentProtein;
    }

    public void setCurrentProtein(int currentProtein) {
        this.currentProtein = currentProtein;
    }

    public List<Long> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Long> recipes) {
        this.recipes = recipes;
    }
}

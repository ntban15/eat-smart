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
    private String title = "";
    private String dietType = "";
    private String excludedIngredients = ""; //separated by comma
    private int maxCalories = 0;
    private int minCalories = 0;
    private int maxCarbs = 0;
    private int minCarbs = 0;
    private int maxFat = 0;
    private int minFat = 0;
    private int maxProtein = 0;
    private int minProtein = 0;
    @Exclude
    private String dietKey = "";    //use this to keep track of which diet user clicks
    @Exclude
    private boolean active = false;

    public Diet() {}

    @Exclude
    public boolean isActive() {
        return active;
    }

    @Exclude
    public void setActive(boolean active) {
        this.active = active;
    }

    @Exclude
    public void setDietKey(String dietKey) {
        this.dietKey = dietKey;
    }

    @Exclude
    public String getDietKey() {
        return this.dietKey;
    }

    @Exclude
    public Map<String, Object> toFullMap() {
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
        return result;
    }

    @Exclude
    public Map<String, Object> toMetaMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("dietType", dietType);
        result.put("excludedIngredients", excludedIngredients);
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
}

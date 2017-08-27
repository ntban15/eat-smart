package com.annguyen.android.eatsmart.recipes.events;

import com.annguyen.android.eatsmart.entities.Diet;
import com.annguyen.android.eatsmart.entities.Recipe;

import java.util.List;

/**
 * Created by annguyen on 24/08/2017.
 */

public class RecipesEvent {

    public static final int GET_RECIPES_FAIL = 444;
    public static final int GET_RECIPES_COMPLETE = 555;
    public static final int GET_NEW_RECIPES_COMPLETE = 666;

    private int eventCode;
    private List<Recipe> recipes;
    private Diet curDiet;

    public RecipesEvent(int eventCode, List<Recipe> recipes, Diet curDiet) {
        this.eventCode = eventCode;
        this.recipes = recipes;
        this.curDiet = curDiet;
    }

    public int getEventCode() {
        return eventCode;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public Diet getCurDiet() {
        return curDiet;
    }
}

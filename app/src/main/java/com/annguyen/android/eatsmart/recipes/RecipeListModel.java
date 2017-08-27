package com.annguyen.android.eatsmart.recipes;

import com.annguyen.android.eatsmart.entities.Recipe;

/**
 * Created by annguyen on 6/2/2017.
 */

public interface RecipeListModel {

    void stop();

    void getRecipes();

    void removeFromDiet(long id);

    void addToDiet(Recipe recipe);

    void getNewRecipes(String query);
}

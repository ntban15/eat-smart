package com.annguyen.android.eatsmart.recipes.ui;

import com.annguyen.android.eatsmart.entities.Diet;
import com.annguyen.android.eatsmart.entities.Recipe;

/**
 * Created by annguyen on 6/2/2017.
 */

public interface RecipeListView {

    void prepareUI(Diet curDiet);

    void onSuccess(String msg);

    void onError(String msg);

    void showProgressBar();

    void hideProgressBar();

    void addNewRecipe(Recipe recipe);

    void clearRecipes();
}

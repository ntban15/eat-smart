package com.annguyen.android.eatsmart.recipes;

import com.annguyen.android.eatsmart.entities.Recipe;
import com.annguyen.android.eatsmart.recipes.events.RecipesEvent;

/**
 * Created by annguyen on 6/2/2017.
 */

public interface RecipeListPresenter {

    void start();

    void stop();

    void onRecipesRequest();

    void onRecipesEvent(RecipesEvent recipesEvent);

    void onSearchQueryEvent(String query);

    void onRecipeRemoveClick(long id);

    void onRecipeAddClick(Recipe recipe);

    void onNewQuery(String query);
}
